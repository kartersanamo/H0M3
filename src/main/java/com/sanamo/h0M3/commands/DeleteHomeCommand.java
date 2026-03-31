package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.command.annotations.PlayerOnly;
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

@PlayerOnly
public class DeleteHomeCommand extends CoreCommand {

    private final HomeManager homeManager;

    public DeleteHomeCommand(HomeManager homeManager) {
        super(
                "deletehome",
                "Deletes a player's home",
                "/deletehome <home>",
                "delhome", "dh"
        );
        this.homeManager = homeManager;
    }

    @Override
    protected boolean onExecute(CommandContext context) {
        Player player = context.getPlayer();
        // Ensure they entered a name
        if (!context.hasArgs()) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.commandUsage, "%usage%", this.getUsage())
            ));
            return true;
        }

        // Validate home name
        String homeName = context.getArg(0);
        if (!homeManager.exists(player.getUniqueId(), homeName)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNotFoundName, "%name%", homeName)
            ));
            return true;
        }

        homeManager.deleteHome(player.getUniqueId(), homeName);
        player.sendMessage(ChatFormat.info(
                PlaceholderUtil.replace(MessagesUtil.homeDeleted, "%name%", homeName)
        ));
        EffectUtil.play(
                player,
                ConfigUtil.deleteHomeSound,
                ConfigUtil.deleteHomeSoundVolume,
                ConfigUtil.deleteHomeSoundPitch,
                ConfigUtil.deleteHomeParticle,
                ConfigUtil.deleteHomeParticleCount,
                ConfigUtil.deleteHomeParticleRadius
        );

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
