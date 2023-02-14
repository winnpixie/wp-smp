package io.github.winnpixie.smp.listeners.impl;

import io.github.winnpixie.smp.Config;
import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.listeners.BaseListener;
import io.github.winnpixie.smp.utilities.TextHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener extends BaseListener {
    public ConnectionListener(SmpCore plugin) {
        super(plugin);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if (Config.BROADCAST_JOINS) {
            if (!event.getPlayer().hasPlayedBefore() && !Config.FIRST_TIMER_MESSAGE.isEmpty()) {
                // Send first-timer's message
                event.setJoinMessage(TextHelper.translateColorCode(Config.FIRST_TIMER_MESSAGE
                        .replace("{player_name}", event.getPlayer().getName())));
            } else if (!Config.JOIN_MESSAGE.isEmpty()) {
                // Send normal custom join message
                event.setJoinMessage(TextHelper.translateColorCode(Config.JOIN_MESSAGE
                        .replace("{player_name}", event.getPlayer().getName())));
            }
        } else {
            event.setJoinMessage(null);
        }

        for (var line : Config.MOTD_LINES) {
            event.getPlayer().spigot().sendMessage(TextHelper.toComponents(TextHelper.translateColorCode(line)
                    .replace("{player_name}", event.getPlayer().getDisplayName())));
        }
    }

    @EventHandler
    private void onLogin(PlayerLoginEvent event) {
        if (!Config.WHITELIST_IGNORE_FULL) return;
        if (event.getResult() != PlayerLoginEvent.Result.KICK_FULL) return;
        if (!event.getPlayer().isWhitelisted()) return;

        event.allow();
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        getPlugin().removePlayerData(event.getPlayer().getUniqueId());

        if (!Config.BROADCAST_QUITS) {
            event.setQuitMessage(null);
            return;
        }

        if (Config.QUIT_MESSAGE.isEmpty()) return;

        event.setQuitMessage(TextHelper.translateColorCode(Config.QUIT_MESSAGE
                .replace("{player_name}", event.getPlayer().getDisplayName())));
    }
}
