package io.github.winnpixie.smp.commands.impl;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HatCommand extends BaseCommand {
    private final String noItemMessage = ChatHelper.format("&cNo item in your main hand.");

    public HatCommand(SmpCore plugin) {
        super(plugin, "hat");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatHelper.PLAYERS_ONLY);
            return true;
        }

        ItemStack currentItem = player.getInventory().getItemInMainHand();
        if (currentItem.getType() == Material.AIR) {
            player.sendMessage(noItemMessage);
            return true;
        }

        player.getInventory().setItemInMainHand(null);

        ItemStack helmetItem = player.getInventory().getHelmet();
        if (helmetItem != null && helmetItem.getType() != Material.AIR) {
            player.getInventory().setItemInMainHand(helmetItem);
        }

        player.getInventory().setHelmet(currentItem);
        player.sendMessage(ChatHelper.format(String.format("&eYou are now wearing &c%s", currentItem.getType().name())));
        return true;
    }
}
