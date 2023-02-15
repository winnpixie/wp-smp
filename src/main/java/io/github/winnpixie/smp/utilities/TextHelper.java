package io.github.winnpixie.smp.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class TextHelper {
    public static final BaseComponent[] INVALID_TARGET = new ComponentBuilder("Unable to locate target.")
            .color(ChatColor.RED)
            .create();
    public static final BaseComponent[] PLAYERS_ONLY = new ComponentBuilder("This action is only available to players.")
            .color(ChatColor.RED)
            .create();
    public static final BaseComponent[] MISSING_ARGUMENTS = new ComponentBuilder("Missing arguments.")
            .color(ChatColor.RED)
            .create();
    public static final BaseComponent[] NO_PERMISSION = new ComponentBuilder("Insufficient permissions.")
            .color(ChatColor.RED)
            .create();

    public static String formatColors(String text) {
        return toSpigotHex(ChatColor.translateAlternateColorCodes('&', text));
    }

    public static BaseComponent[] toComponents(String text) {
        return TextComponent.fromLegacyText(text);
    }

    // Converts &#RRGGBB to &x&R&R&G&G&B&B
    private static String toSpigotHex(String text) {
        StringBuilder sb = new StringBuilder();

        for (var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);

            if ((c == '\u00A7' || c == '&') && i + 7 < text.length() && text.charAt(i + 1) == '#') {
                var hex = text.substring(i + 2, i + 8);

                if (hex.matches("[a-fA-F0-9]{6}")) {
                    sb.append("\u00A7x");
                    for (int o = 0; o < hex.length(); o++) {
                        sb.append('\u00A7').append(hex.charAt(o));
                    }

                    i += 7;
                    continue;
                }
            }

            sb.append(c);
        }

        return sb.toString();
    }
}
