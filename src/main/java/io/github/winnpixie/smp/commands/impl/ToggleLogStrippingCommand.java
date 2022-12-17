package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.ChatHelper;
import io.github.winnpixie.smp.utilities.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleLogStrippingCommand extends BaseCommand {
    private final String permittedMessage = ChatHelper.format("&aYou can now strip logs with an axe again.");
    private final String deniedMessage = ChatHelper.format("&cYou will no longer strip logs with an axe &4(for this session)&c.");

    public ToggleLogStrippingCommand(SmpCore plugin) {
        super(plugin, "togglelogstripping");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatHelper.PLAYERS_ONLY);
            return true;
        }

        PlayerData playerData = getPlugin().getPlayerData(player.getUniqueId());
        playerData.canStripLogs = !playerData.canStripLogs;

        if (playerData.canStripLogs) {
            player.sendMessage(permittedMessage);
        } else {
            player.sendMessage(deniedMessage);
        }

        return true;
    }
}
