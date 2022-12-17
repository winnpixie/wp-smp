package io.github.winnpixie.smp.commands.impl.admin;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigurationCommand extends BaseCommand {
    private final String reloadMessage = ChatHelper.format("&aThe configuration has been reloaded.");

    public ReloadConfigurationCommand(SmpCore plugin) {
        super(plugin, "reload-configuration");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatHelper.NO_PERMISSION);
            return true;
        }

        this.getPlugin().reloadConfig();
        this.getPlugin().annoc.setConfiguration(this.getPlugin().getConfig()).load();
        sender.sendMessage(reloadMessage);
        return true;
    }
}
