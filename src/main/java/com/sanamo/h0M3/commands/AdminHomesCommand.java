package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.command.annotations.CommandPermission;
import com.sanamo.h0M3.api.command.annotations.PlayerOnly;
import com.sanamo.h0M3.guis.AdminGUI;
import com.sanamo.h0M3.managers.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@PlayerOnly
@CommandPermission("h0m3.adminhomes")
public class AdminHomesCommand extends CoreCommand {

    private final HomeManager homeManager;

    public AdminHomesCommand(HomeManager homeManager) {
        super(
                "adminhomes",
                "Opens the admin homes manager",
                "/adminhomes [player]"
        );
        this.homeManager = homeManager;
    }

    @Override
    protected boolean onExecute(CommandContext context) {
        Player player = context.getPlayer();
        if (!context.hasArgs()) {
            new AdminGUI(homeManager, player).open(player);
            return true;
        }

        openTargetHomes(player, homeManager, context.getArg(0));
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandContext context) {
        if (context.getArgCount() != 1) {
            return new ArrayList<>();
        }

        String partial = context.getArg(0, "").toLowerCase();
        Set<String> suggestions = new HashSet<>();

        for (Player online : Bukkit.getOnlinePlayers()) {
            String name = online.getName();
            if (name != null && name.toLowerCase().startsWith(partial)) {
                suggestions.add(name);
            }
        }

        for (String name : homeManager.getKnownHomeOwnerNames()) {
            if (name.toLowerCase().startsWith(partial)) {
                suggestions.add(name);
            }
        }

        return suggestions.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public static void openTargetHomes(Player admin, HomeManager homeManager, String targetName) {
        if (targetName == null || targetName.isBlank()) {
            new AdminGUI(homeManager, admin).open(admin);
            return;
        }

        UUID targetId = homeManager.resolvePlayerUuid(targetName);
        if (targetId == null) {
            admin.sendMessage(ChatFormat.error("&cPlayer not found: &f" + targetName));
            return;
        }

        new AdminGUI(homeManager, admin, targetId).open(admin);
    }
}

