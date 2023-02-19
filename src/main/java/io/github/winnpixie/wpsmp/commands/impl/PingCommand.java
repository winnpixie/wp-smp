package io.github.winnpixie.wpsmp.commands.impl;

import io.github.winnpixie.wpsmp.WPSMPPlugin;
import io.github.winnpixie.wpsmp.commands.BaseCommand;
import io.github.winnpixie.wpsmp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends BaseCommand {
    public PingCommand(WPSMPPlugin plugin) {
        super(plugin, "ping");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.spigot().sendMessage(TextHelper.PLAYERS_ONLY);
                return true;
            }

            sender.spigot().sendMessage(new ComponentBuilder("Your ping is ").color(ChatColor.YELLOW)
                    .append("%dms".formatted(((Player) sender).getPing())).color(ChatColor.RED).create());
            return true;
        }

        var player = getPlugin().getServer().getPlayerExact(args[0]);
        if (player == null) {
            sender.spigot().sendMessage(TextHelper.INVALID_TARGET);
            return true;
        }
        sender.spigot().sendMessage(new ComponentBuilder("%s's".formatted(player.getDisplayName())).color(ChatColor.RED)
                .append(" ping is ").color(ChatColor.YELLOW)
                .append("%dms".formatted(player.getPing())).color(ChatColor.RED).create());
        return true;
    }
}
