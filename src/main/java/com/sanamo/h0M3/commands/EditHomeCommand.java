package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.guis.ManageHomeGUI;
import com.sanamo.h0M3.managers.HomeManager;
import com.sanamo.h0M3.models.Home;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EditHomeCommand extends CoreCommand {

    private final HomeManager homeManager;

    public EditHomeCommand(HomeManager homeManager) {
        super(
                "edithome",
                "Edits a player's home information",
                "/edithome <home>"
        );
        this.homeManager = homeManager;
    }

    @Override
    protected boolean onExecute(CommandContext context) {

        Player player = context.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (!context.hasArgs()) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.commandUsage, "%usage%", this.getUsage())
            ));
            return true;
        }

        String homeName = context.getArg(0);
        if (!homeManager.exists(playerUUID, homeName)) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeNotFoundName, "%name%", homeName)
            ));
            return true;
        }

        Home home = homeManager.getHome(player.getUniqueId(), homeName);
        if (home == null) {
            player.sendMessage(ChatFormat.error(
                    PlaceholderUtil.replace(MessagesUtil.homeResultNull)
            ));
            return true;
        }

        ManageHomeGUI manageHomeGUI = new ManageHomeGUI(homeManager, home, player);
        manageHomeGUI.open(player);

        return true;
    }
}
