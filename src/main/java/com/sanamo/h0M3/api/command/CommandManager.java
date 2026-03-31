/**
 * <h1>CommandManager - Command Registration and Management</h1>
 * <p>
 * The CommandManager class handles the registration, execution, and management
 * of all commands in SanamoCore. It provides a centralized system for command
 * handling with support for sub-commands, permissions, and tab completion.
 * </p>
 *
 * @author Karter Sanamo
 * @version 1.0-SNAPSHOT
 * @since 1.0.0
 */
package com.sanamo.h0M3.api.command;

import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {

    private static CommandManager instance;
    private final Plugin plugin;
    private final Map<String, CoreCommand> commands;
    private final Map<String, List<SubCommand>> subCommands;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        this.subCommands = new HashMap<>();
    }

    public static void initialize(Plugin plugin) {
        if (instance == null) {
            instance = new CommandManager(plugin);
        }
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public static void shutdown() {
        if (instance != null) {
            instance.commands.clear();
            instance.subCommands.clear();
            instance = null;
        }
    }

    public void registerCommand(CoreCommand command) {
        commands.put(command.getName().toLowerCase(), command);

        // Register with Bukkit
        String commandName = command.getName();
        org.bukkit.command.PluginCommand pluginCommand = plugin.getServer().getPluginCommand(commandName);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }

        // Register aliases
        for (String alias : command.getAliases()) {
            commands.put(alias.toLowerCase(), command);
        }
    }

    public void registerSubCommand(String parentCommand, SubCommand subCommand) {
        String key = parentCommand.toLowerCase();
        subCommands.computeIfAbsent(key, k -> new ArrayList<>()).add(subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        CoreCommand coreCommand = commands.get(commandName);

        if (coreCommand == null) {
            return false;
        }

        // Check for sub-commands
        if (args.length > 0) {
            List<SubCommand> subs = subCommands.get(commandName);
            if (subs != null) {
                String subName = args[0].toLowerCase();
                for (SubCommand sub : subs) {
                    if (sub.matches(subName)) {
                        // Create context with remaining args
                        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                        CommandContext context = new CommandContext(sender, label, subArgs);

                        // Check permission
                        if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
                            sender.sendMessage(PlaceholderUtil.replace(MessagesUtil.subCommandNoPermission));
                            return true;
                        }

                        return sub.execute(context);
                    }
                }
            }
        }

        CommandContext context = new CommandContext(sender, label, args);
        return coreCommand.execute(context);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        CoreCommand coreCommand = commands.get(commandName);

        if (coreCommand == null) {
            return new ArrayList<>();
        }

        // Check for sub-commands
        if (args.length == 1) {
            List<SubCommand> subs = subCommands.get(commandName);
            if (subs != null) {
                List<String> completions = new ArrayList<>();
                String partial = args[0].toLowerCase();
                for (SubCommand sub : subs) {
                    if (sub.getPermission() == null || sender.hasPermission(sub.getPermission())) {
                        if (sub.getName().toLowerCase().startsWith(partial)) {
                            completions.add(sub.getName());
                        }
                        for (String alias : sub.getAliases()) {
                            if (alias.toLowerCase().startsWith(partial)) {
                                completions.add(alias);
                            }
                        }
                    }
                }
                return completions;
            }
        }

        CommandContext context = new CommandContext(sender, label, args);
        return coreCommand.tabComplete(context);
    }

    public CoreCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    public Collection<CoreCommand> getCommands() {
        return new ArrayList<>(commands.values());
    }
}
