package com.sanamo.h0M3.managers;

import com.sanamo.h0M3.H0M3;
import com.sanamo.h0M3.api.chat.ColorUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInputManager implements Listener {

    private final H0M3 plugin;
    private final Map<UUID, ChatInputSession> activeSessions;

    public ChatInputManager(H0M3 plugin) {
        this.plugin = plugin;
        this.activeSessions = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void awaitInput(Player player, String prompt,
                           Consumer<String> handler,
                           Runnable cancelHandler) {
        UUID uuid = player.getUniqueId();

        // Send prompt
        player.sendMessage(ColorUtil.translate(prompt));
        player.sendMessage(ColorUtil.translate(
                PlaceholderUtil.replace(
                        MessagesUtil.chatInputCancelHint,
                        "%keyword%", MessagesUtil.chatInputCancelKeyword
                )
        ));

        // Store session
        activeSessions.put(uuid, new ChatInputSession(handler, cancelHandler));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ChatInputSession session = activeSessions.get(uuid);
        if (session == null) {
            return; // Not waiting for input
        }

        // Cancel the chat message
        event.setCancelled(true);

        String message = event.getMessage().trim();

        // Handle cancellation
        if (message.equalsIgnoreCase(MessagesUtil.chatInputCancelKeyword)) {
            activeSessions.remove(uuid);
            if (session.cancelHandler() != null) {
                session.cancelHandler().run();
            }
            // player.sendMessage(ColorUtil.translate("&cCancelled."));
            return;
        }

        // Process input
        activeSessions.remove(uuid);

        // Run handler on main thread
        plugin.getServer().getScheduler().runTask(plugin, () -> session.handler().accept(message));
    }

    public void cancelInput(Player player) {
        activeSessions.remove(player.getUniqueId());
    }

    public boolean isWaitingForInput(Player player) {
        return activeSessions.containsKey(player.getUniqueId());
    }

    private record ChatInputSession(Consumer<String> handler, Runnable cancelHandler) {
    }
}
