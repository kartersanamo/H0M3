package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.util.ConfigUtil;
import com.sanamo.h0M3.api.util.EffectUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.managers.HomeManager;
import com.sanamo.h0M3.models.Home;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenameHomeCommand extends CoreCommand {

    private final HomeManager homeManager;

    public RenameHomeCommand(HomeManager homeManager) {
        super(
                "renamehome",
                "Renames a player's home",
                "/renamehome <name> <new_name>",
                ""
        );
        this.homeManager = homeManager;
    }

    @Override
    protected boolean onExecute(CommandContext context) {

        Player player = context.getPlayer();
        if (!context.hasArgs() || context.getArgCount() == 1) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.commandUsage, "%usage%", this.getUsage())
            ));
            return true;
        }

        String homeName = context.getArg(0);
        String newName = context.getArg(1);

        // Get the home
        Home home = homeManager.getHome(player.getUniqueId(), homeName);
        if (home == null) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNotFoundName, "%name%", homeName)
            ));
            return true;
        }

        if (homeManager.homeNameHasColor(newName)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNameColorCodes)
            ));
            return true;
        }

        if (homeManager.exists(player.getUniqueId(), newName)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNameExists)
            ));
            return true;
        }

        if (homeManager.isHomeNameCorrectSize(newName)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(
                            MessagesUtil.homeNameSize,
                            "%min%", String.valueOf(ConfigUtil.homeNameMinLength),
                            "%max%", String.valueOf(ConfigUtil.homeNameMaxLength)
                    )
            ));
            return true;
        }

        // Update home name
        home.setDisplayName(newName);
        homeManager.update(home);
        player.sendMessage(ChatFormat.info(
                PlaceholderUtil.replace(
                        MessagesUtil.homeNameChanged,
                        "%old%", homeName,
                        "%new%", newName
                )
        ));
        EffectUtil.play(
                player,
                ConfigUtil.renameHomeSound,
                ConfigUtil.renameHomeSoundVolume,
                ConfigUtil.renameHomeSoundPitch,
                ConfigUtil.renameHomeParticle,
                ConfigUtil.renameHomeParticleCount,
                ConfigUtil.renameHomeParticleRadius
        );

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandContext context) {
        Player player = context.getPlayer();

        if (context.getArgCount() == 1) {
            String partial = context.getArg(0);
            Map<String, Home> homes = homeManager.getHomes(player.getUniqueId());
            if (homes != null) {
                List<String> matches = new ArrayList<>();

                for (Home home : homes.values()) {
                    if (home.getDisplayName().toLowerCase().startsWith(partial)) {
                        matches.add(home.getDisplayName());
                    }
                }
                return matches;
            }
        }
        return new ArrayList<>();
    }
}
