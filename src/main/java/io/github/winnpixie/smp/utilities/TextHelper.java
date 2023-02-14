package io.github.winnpixie.smp.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {
    private static final Pattern HEX_PATTERN = Pattern.compile("[\u00A7&]#(?i)[a-f0-9]{6}");

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

    public static String translateColorCode(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static BaseComponent[] toComponents(String text) {
        return TextComponent.fromLegacyText(translateHexFormat(text));
    }

    private static String translateHexFormat(String text) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = HEX_PATTERN.matcher(text);

        while (matcher.find()) {
            if (matcher.start() > 0) {
                sb.append(text, 0, matcher.start() - 1);
            }

            sb.append("\u00A7x");
            for (int i = matcher.start() + 2; i < matcher.end(); i++) {
                sb.append('\u00A7').append(text.charAt(i));
            }

            text = text.substring(matcher.end());
        }

        sb.append(text);
        return sb.toString();
    }
}
