package io.github.winnpixie.smp.utilities;

import org.bukkit.ChatColor;

public class ChatHelper {
    public static final String INVALID_TARGET = format("&cUnable to locate target.");
    public static final String PLAYERS_ONLY = format("&cThis action is only available to players.");
    public static final String MISSING_ARGUMENTS = format("&cMissing arguments.");
    public static final String NO_PERMISSION = format("&cInsufficient permissions.");

    public static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
