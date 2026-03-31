package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.command.annotations.PlayerOnly;
import com.sanamo.h0M3.api.util.ConfigUtil;
import com.sanamo.h0M3.api.util.EffectUtil;
import com.sanamo.h0M3.api.util.LocationUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.managers.HomeManager;
import com.sanamo.h0M3.models.Home;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@PlayerOnly
public class SetHomeCommand extends CoreCommand {
    private final HomeManager homeManager;

    public SetHomeCommand(HomeManager homeManager) {
        super(
                "sethome",
                "Sets a new home for the player",
                "/sethome <name>"
        );
        this.homeManager = homeManager;
    }

    @Override
    protected boolean onExecute(CommandContext context) {
        // Ensure they entered a name
        Player player = context.getPlayer();
        Location location = player.getLocation();
        if (!context.hasArgs()) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.commandUsage, "%usage%", this.getUsage())
            ));
            return true;
        }

        // Get & validate name
        String name = context.getArg(0);
        if (homeManager.isHomeNameCorrectSize(name)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(
                            MessagesUtil.homeNameSize,
                            "%min%", String.valueOf(ConfigUtil.homeNameMinLength),
                            "%max%", String.valueOf(ConfigUtil.homeNameMaxLength)
                    )
            ));
            return true;
        }

        // Ensure no color codes in it
        if (homeManager.homeNameHasColor(name)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNameColorCodes)
            ));
            return true;
        }

        // Ensure they have not reached the limit
        if (homeManager.getHomeCount(player.getUniqueId()) >= ConfigUtil.maxNumberOfHomes) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeLimitReached, "%max%", String.valueOf(ConfigUtil.maxNumberOfHomes))
            ));
            return true;
        }

        // Home already exists, just update the location
        if (homeManager.exists(player.getUniqueId(), name)) {
            Home home = homeManager.getHome(player.getUniqueId(), name);
            home.setLocation(player.getLocation());
            homeManager.update(home);

            // Home does not exist, create a new one and add it
        } else {
            String id = "home_" + player.getUniqueId() + "_" + UUID.randomUUID();

            Home home = new Home(
                    id,
                    player,
                    name,
                    new ArrayList<>(),
                    ConfigUtil.defaultHomeMaterial,
                    location,
                    System.currentTimeMillis(),
                    0
            );

            homeManager.addHome(player.getUniqueId(), home);
        }

        // Confirmation
        EffectUtil.play(
                player,
                ConfigUtil.setHomeSound,
                ConfigUtil.setHomeSoundVolume,
                ConfigUtil.setHomeSoundPitch,
                ConfigUtil.setHomeParticle,
                ConfigUtil.setHomeParticleCount,
                ConfigUtil.setHomeParticleRadius
        );
        player.sendMessage(ChatFormat.info(
                PlaceholderUtil.replace(MessagesUtil.homeSet, "%name%", name, "%location%", LocationUtil.format(location))
        ));

        return true;
    }
}
