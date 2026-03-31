package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.command.annotations.PlayerOnly;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.managers.HomeManager;
import com.sanamo.h0M3.models.Home;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@PlayerOnly
public class HomeCommand extends CoreCommand {

    private final HomeManager homeManager;

    public HomeCommand(HomeManager homeManager) {
        super(
                "home",
                "Teleports a player to their home",
                "/home <name>"
        );
        this.homeManager = homeManager;
    }

    @Override
    protected boolean onExecute(CommandContext context) {
        // Ensure they entered a name
        Player player = context.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!context.hasArgs()) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.commandUsage, "%usage%", this.getUsage())
            ));
            return true;
        }
        String name = context.getArg(0);

        // Ensure no color codes in it
        if (homeManager.homeNameHasColor(name)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNameColorCodes)
            ));
            return true;
        }

        // Confirm name
        if (!homeManager.exists(uuid, name)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNotFoundName, "%name%", name)
            ));
            return true;
        }


        // Get home & teleport
        Home home = homeManager.getHome(uuid, name);
        if (home == null) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeGetFailed, "%name%", name)
            ));
            return true;
        }
        home.teleport();
        homeManager.update(home);

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandContext context) {
        Player player = context.getPlayer();

        if (context.getArgCount() == 1) {
            String partial = context.getArg(0, "").toLowerCase();
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
