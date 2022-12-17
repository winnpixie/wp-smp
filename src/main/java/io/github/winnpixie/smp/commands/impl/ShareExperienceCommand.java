package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShareExperienceCommand extends BaseCommand {
    private final String notEnoughExperience = ChatHelper.format("&cInsufficient experience.");

    public ShareExperienceCommand(SmpCore plugin) {
        super(plugin, "share-experience");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatHelper.PLAYERS_ONLY);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatHelper.MISSING_ARGUMENTS);
            return true;
        }

        Player player = getPlugin().getServer().getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(ChatHelper.INVALID_TARGET);
            return true;
        }

        return true;
    }
}
