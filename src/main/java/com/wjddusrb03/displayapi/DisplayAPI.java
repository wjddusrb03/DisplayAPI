package com.wjddusrb03.displayapi;

import com.wjddusrb03.displayapi.animation.AnimationBuilder;
import com.wjddusrb03.displayapi.builder.*;
import com.wjddusrb03.displayapi.display.*;
import com.wjddusrb03.displayapi.manager.DisplayManager;
import com.wjddusrb03.displayapi.manager.PersistenceManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Main entry point for the DisplayAPI.
 *
 * <p>Usage examples:
 * <pre>
 * // Text hologram
 * DisplayAPI.text(location)
 *     .text(Component.text("Hello!").color(NamedTextColor.GOLD))
 *     .billboard(Billboard.CENTER)
 *     .spawn();
 *
 * // Block display
 * DisplayAPI.block(location)
 *     .block(Material.DIAMOND_BLOCK)
 *     .scale(0.5f)
 *     .spawn();
 *
 * // Damage popup
 * DisplayAPI.popup(location)
 *     .text(Component.text("-25").color(NamedTextColor.RED))
 *     .duration(30)
 *     .visibleTo(player)
 *     .spawn();
 * </pre>
 */
public final class DisplayAPI {

    private static DisplayAPIPlugin plugin;
    private static DisplayManager manager;
    private static PersistenceManager persistenceManager;

    private DisplayAPI() {}

    static void init(DisplayAPIPlugin pluginInstance, DisplayManager mgr, PersistenceManager persistence) {
        plugin = pluginInstance;
        manager = mgr;
        persistenceManager = persistence;
    }

    // ========================
    // Builder factory methods
    // ========================

    public static TextDisplayBuilder text(Location location) {
        checkInit();
        return new TextDisplayBuilder(location);
    }

    public static BlockDisplayBuilder block(Location location) {
        checkInit();
        return new BlockDisplayBuilder(location);
    }

    public static ItemDisplayBuilder item(Location location) {
        checkInit();
        return new ItemDisplayBuilder(location);
    }

    public static PopupBuilder popup(Location location) {
        checkInit();
        return new PopupBuilder(location);
    }

    // ========================
    // Animation factory
    // ========================

    public static AnimationBuilder animate(SpawnedDisplay display) {
        checkInit();
        return new AnimationBuilder(display);
    }

    public static Leaderboard leaderboard(Location location) {
        checkInit();
        return new Leaderboard(location);
    }

    public static InteractiveBuilder interactive(Location location) {
        checkInit();
        return new InteractiveBuilder(location);
    }

    public static FollowDisplay follow(SpawnedDisplay display, Entity target) {
        checkInit();
        return new FollowDisplay(display, target);
    }

    // ========================
    // Group factory
    // ========================

    public static DisplayGroup group(String id, Location anchor) {
        checkInit();
        return new DisplayGroup(id, anchor);
    }

    // ========================
    // Management methods
    // ========================

    public static SpawnedDisplay getById(String id) {
        checkInit();
        return manager.getById(id);
    }

    public static void remove(String id) {
        checkInit();
        manager.remove(id);
    }

    public static void removeAll() {
        checkInit();
        manager.removeAll();
    }

    // ========================
    // Internal accessors
    // ========================

    public static Plugin getPlugin() {
        checkInit();
        return plugin;
    }

    public static DisplayManager getManager() {
        checkInit();
        return manager;
    }

    public static PersistenceManager getPersistenceManager() {
        checkInit();
        return persistenceManager;
    }

    public static float getDefaultViewRange() {
        if (plugin != null) {
            return (float) plugin.getConfig().getDouble("default-view-range", 1.0);
        }
        return 1.0f;
    }

    private static void checkInit() {
        if (plugin == null) {
            throw new IllegalStateException("DisplayAPI is not initialized. Is the DisplayAPI plugin enabled?");
        }
    }
}
