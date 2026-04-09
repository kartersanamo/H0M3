package com.sanamo.h0M3;

import com.sanamo.h0M3.api.command.CommandManager;
import com.sanamo.h0M3.api.command.SubCommand;
import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.gui.GUIManager;
import com.sanamo.h0M3.api.logging.CoreLogger;
import com.sanamo.h0M3.api.logging.LogLevel;
import com.sanamo.h0M3.api.util.ConfigUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.commands.*;
import com.sanamo.h0M3.guis.AdminGUI;
import com.sanamo.h0M3.listeners.PlayerJoinListener;
import com.sanamo.h0M3.listeners.PlayerQuitListener;
import com.sanamo.h0M3.listeners.TeleportMoveListener;
import com.sanamo.h0M3.managers.ChatInputManager;
import com.sanamo.h0M3.managers.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class H0M3 extends JavaPlugin {

    private static H0M3 instance;
    private static CoreLogger logger;

    private HomeManager homeManager;
    private CommandManager commandManager;
    private ChatInputManager chatInputManager;
    private GUIManager guiManager;

    private File messagesFile;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        instance = this;

        initLogger();
        registerManagers();
        registerCommands();
        registerListeners();
        loadHomes();
        saveDefaultConfig();
        ConfigUtil.load(getConfig());
        createConfigs();

        logger.info(getDescription().getName() + "v" + getDescription().getVersion() + " has been enabled!");
    }

    private void createConfigs() {
        // Ensure messages.yml is created
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        MessagesUtil.load(messagesConfig);
        logger.info("Messages cache loaded.");
    }

    private void initLogger() {
        logger = new CoreLogger(this);
        logger.setLevel(LogLevel.INFO);
    }

    private void registerManagers() {
        guiManager = new GUIManager(this);
        homeManager = new HomeManager(this);
        chatInputManager = new ChatInputManager(this);
        commandManager = new CommandManager(this);
    }

    private void registerCommands() {
        commandManager.registerCommand(new H0M3Command());
        commandManager.registerSubCommand("h0m3", new SubCommand(
                "reload",
                "Reloads plugin messages",
                "/h0m3 reload",
                "h0m3.reload",
                context -> {
                    reloadMessages();
                    context.getSender().sendMessage(ChatFormat.success(
                            PlaceholderUtil.replace(MessagesUtil.messagesReloaded)
                    ));
                    return true;
                }
        ));
        commandManager.registerSubCommand("h0m3", new SubCommand(
                "admin",
                "Opens the admin homes manager GUI",
                "/h0m3 admin [player]",
                "h0m3.adminhomes",
                context -> {
                    if (!context.isPlayer()) {
                        context.getSender().sendMessage(PlaceholderUtil.replace(MessagesUtil.commandPlayerOnly));
                        return true;
                    }

                    Player player = context.getPlayer();
                    if (!context.hasArgs()) {
                        new AdminGUI(homeManager, player).open(player);
                        return true;
                    }

                    com.sanamo.h0M3.commands.AdminHomesCommand.openTargetHomes(player, homeManager, context.getArg(0));
                    return true;
                },
                "manage"
        ));
        commandManager.registerCommand(new HomeCommand(homeManager));
        commandManager.registerCommand(new DeleteHomeCommand(homeManager));
        commandManager.registerCommand(new SetHomeCommand(homeManager));
        commandManager.registerCommand(new HomesCommand(homeManager));
        commandManager.registerCommand(new RenameHomeCommand(homeManager));
        commandManager.registerCommand(new MoveHomeCommand(homeManager));
        commandManager.registerCommand(new EditHomeCommand(homeManager));
        commandManager.registerCommand(new HomeInfoCommand(homeManager));
        commandManager.registerCommand(new AdminHomesCommand(homeManager));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(homeManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(homeManager), this);
        Bukkit.getPluginManager().registerEvents(new TeleportMoveListener(), this);
    }

    private void loadHomes() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            homeManager.loadHomes(player.getUniqueId());
        }
    }

    /*
     * Cleanup
     */
    @Override
    public void onDisable() {
        unloadHomes();
        logger.close();
    }

    private void unloadHomes() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            homeManager.unloadHomes(player.getUniqueId());
        }
    }

    // Getters
    public static H0M3 getInstance() {
        return instance;
    }

    public static CoreLogger getLog() {
        return logger;
    }

    public FileConfiguration getMessagesConfig() { return this.messagesConfig;}

    public void reloadMessages() {
        if (messagesFile == null) {
            return;
        }
        reloadConfig();
        ConfigUtil.load(getConfig());
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        MessagesUtil.load(messagesConfig);
    }

    // Getters (Managers)
    public ChatInputManager getChatInputManager() {
        return chatInputManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }
}
