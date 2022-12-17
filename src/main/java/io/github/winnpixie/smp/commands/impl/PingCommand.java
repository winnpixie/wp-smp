package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends BaseCommand {
    public PingCommand(SmpCore plugin) {
        super(plugin, "ping");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatHelper.PLAYERS_ONLY);
                return true;
            }

            sender.sendMessage(ChatHelper.format(String.format("&eYour ping is &c%dms", ((Player) sender).getPing())));
            return true;
        }

        var player = getPlugin().getServer().getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(ChatHelper.INVALID_TARGET);
            return true;
        }

        sender.sendMessage(ChatHelper.format(String.format("&c%s's &eping is &c%dms", player.getDisplayName(),
                player.getPing())));
        return true;
    }
}
