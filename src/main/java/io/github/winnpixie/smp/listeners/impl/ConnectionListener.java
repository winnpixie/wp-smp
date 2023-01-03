package io.github.winnpixie.smp.listeners.impl;

import io.github.winnpixie.smp.Config;
import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.listeners.BaseListener;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ConnectionListener extends BaseListener {
    public ConnectionListener(SmpCore plugin) {
        super(plugin);

        String motd = plugin.getServer().getMotd();
        int port = plugin.getServer().getPort();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (!Config.BROADCAST_TO_LAN) return;

            // According to https://wiki.vg/Server_List_Ping#Ping_via_LAN_.28Open_to_LAN_in_Singleplayer.29
            try (DatagramSocket socket = new DatagramSocket()) {
                var payload = String.format("[MOTD]%s[/MOTD][AD]%d[/AD]", motd, port).getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(payload, payload.length,
                        InetAddress.getByName("224.0.2.60"), 4445);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 30);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if (Config.BROADCAST_JOINS) {
            if (!event.getPlayer().hasPlayedBefore() && !Config.FIRST_TIMER_MESSAGE.isEmpty()) {
                // Send first-timer's message
                event.setJoinMessage(ChatHelper.format(Config.FIRST_TIMER_MESSAGE
                        .replace("{player_name}", event.getPlayer().getName())));
            } else if (!Config.JOIN_MESSAGE.isEmpty()) {
                // Send normal custom join message
                event.setJoinMessage(ChatHelper.format(Config.JOIN_MESSAGE
                        .replace("{player_name}", event.getPlayer().getName())));
            }
        } else {
            event.setJoinMessage(null);
        }

        for (var line : Config.MOTD_LINES) {
            event.getPlayer().sendMessage(ChatHelper.format(line
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

        event.setQuitMessage(ChatHelper.format(Config.QUIT_MESSAGE
                .replace("{player_name}", event.getPlayer().getDisplayName())));
    }
}
