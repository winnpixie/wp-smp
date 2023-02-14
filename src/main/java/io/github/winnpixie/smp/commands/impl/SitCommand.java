package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.Config;
import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SitCommand extends BaseCommand {
    private final BaseComponent[] disabledMessage = new ComponentBuilder("Sitting is currently disabled.")
            .color(ChatColor.RED)
            .create();
    private final BaseComponent[] invalidBlockMessage = new ComponentBuilder("Target block is not a stair.")
            .color(ChatColor.RED)
            .create();

    public SitCommand(SmpCore plugin) {
        super(plugin, "sit");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.spigot().sendMessage(TextHelper.PLAYERS_ONLY);
            return true;
        }

        if (!Config.ALLOW_SITTING) {
            sender.spigot().sendMessage(disabledMessage);
        }

        Block block = player.getTargetBlockExact(6, FluidCollisionMode.NEVER);
        if (block == null || !Tag.STAIRS.isTagged(block.getType())) {
            sender.spigot().sendMessage(invalidBlockMessage);
            return true;
        }

        return true;
    }
}
