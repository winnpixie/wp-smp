package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HatCommand extends BaseCommand {
    private final BaseComponent[] noItemMessage = new ComponentBuilder("No item in your main hand.")
            .color(ChatColor.RED)
            .create();

    public HatCommand(SmpCore plugin) {
        super(plugin, "hat");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.spigot().sendMessage(TextHelper.PLAYERS_ONLY);
            return true;
        }

        ItemStack currentItem = player.getInventory().getItemInMainHand();
        if (currentItem.getType() == Material.AIR) {
            player.spigot().sendMessage(noItemMessage);
            return true;
        }

        player.getInventory().setItemInMainHand(null);

        ItemStack helmetItem = player.getInventory().getHelmet();
        if (helmetItem != null && helmetItem.getType() != Material.AIR) {
            player.getInventory().setItemInMainHand(helmetItem);
        }

        player.getInventory().setHelmet(currentItem);
        player.spigot().sendMessage(new ComponentBuilder("You are now wearing ").color(ChatColor.YELLOW)
                .append(currentItem.getType().name()).color(ChatColor.RED).create());
        return true;
    }
}
