package io.github.winnpixie.wpsmp.commands.impl;

import io.github.winnpixie.wpsmp.WPSMPPlugin;
import io.github.winnpixie.wpsmp.commands.BaseCommand;
import io.github.winnpixie.wpsmp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShareExperienceCommand extends BaseCommand {
    private final BaseComponent[] notEnoughExp = new ComponentBuilder("Insufficient experience.")
            .color(ChatColor.RED)
            .create();

    public ShareExperienceCommand(WPSMPPlugin plugin) {
        super(plugin, "share-experience");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(TextHelper.PLAYERS_ONLY);
            return true;
        }

        if (args.length < 1) {
            sender.spigot().sendMessage(TextHelper.MISSING_ARGUMENTS);
            return true;
        }

        Player player = getPlugin().getServer().getPlayerExact(args[0]);
        if (player == null) {
            sender.spigot().sendMessage(TextHelper.INVALID_TARGET);
            return true;
        }

        return true;
    }
}
