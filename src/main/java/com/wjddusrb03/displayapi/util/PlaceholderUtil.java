package com.wjddusrb03.displayapi.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility for PlaceholderAPI integration.
 * Safe to call even when PlaceholderAPI is not installed.
 */
public final class PlaceholderUtil {

    private PlaceholderUtil() {}

    /**
     * Check if PlaceholderAPI is available.
     */
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * Parse placeholders in a string for a player.
     * Returns the original string if PlaceholderAPI is not available.
     */
    public static String parse(Player player, String text) {
        if (!isAvailable() || player == null || text == null) return text;
        try {
            return PlaceholderAPI.setPlaceholders(player, text);
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * Parse placeholders in a Component for a player.
     * Converts to plain text, applies placeholders, returns as Component.
     */
    public static Component parse(Player player, Component component) {
        if (!isAvailable() || player == null || component == null) return component;
        try {
            String plain = PlainTextComponentSerializer.plainText().serialize(component);
            if (!plain.contains("%")) return component;
            String parsed = PlaceholderAPI.setPlaceholders(player, plain);
            return LegacyComponentSerializer.legacySection().deserialize(parsed);
        } catch (Exception e) {
            return component;
        }
    }
}
