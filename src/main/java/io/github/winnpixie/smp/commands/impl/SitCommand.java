package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.Config;
import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SitCommand extends BaseCommand {
    private final String globalDisableMessage = ChatHelper.format("&cSitting is currently disabled.");
    private final String invalidBlockMessage = ChatHelper.format("&cTarget block is not a stair.");

    public SitCommand(SmpCore plugin) {
        super(plugin, "sit");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatHelper.PLAYERS_ONLY);
            return true;
        }

        if (!Config.ALLOW_SITTING) {
            sender.sendMessage(globalDisableMessage);
        }

        Block block = player.getTargetBlockExact(6, FluidCollisionMode.NEVER);
        if (block == null || !Tag.STAIRS.isTagged(block.getType())) {
            sender.sendMessage(invalidBlockMessage);
            return true;
        }

        return true;
    }
}
