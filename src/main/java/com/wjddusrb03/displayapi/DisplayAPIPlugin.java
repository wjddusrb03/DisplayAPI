package com.wjddusrb03.displayapi;

import com.wjddusrb03.displayapi.command.DisplayAPICommand;
import com.wjddusrb03.displayapi.listener.DisplayListener;
import com.wjddusrb03.displayapi.listener.InteractionListener;
import com.wjddusrb03.displayapi.manager.DisplayManager;
import com.wjddusrb03.displayapi.manager.PersistenceManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DisplayAPIPlugin extends JavaPlugin {

    private DisplayManager displayManager;
    private PersistenceManager persistenceManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        displayManager = new DisplayManager();
        persistenceManager = new PersistenceManager(this);

        // Initialize static API
        DisplayAPI.init(this, displayManager, persistenceManager);

        // Register commands
        var cmd = new DisplayAPICommand();
        getCommand("displayapi").setExecutor(cmd);
        getCommand("displayapi").setTabCompleter(cmd);

        // Register listeners
        getServer().getPluginManager().registerEvents(new DisplayListener(), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);

        // Load persistent displays after all worlds are loaded
        new BukkitRunnable() {
            @Override
            public void run() {
                persistenceManager.load();
                int count = displayManager.getPersistentDisplays().size();
                if (count > 0) {
                    getLogger().info("Loaded " + count + " persistent displays.");
                }
            }
        }.runTaskLater(this, 1L);

        // Auto-save and cleanup task
        long saveInterval = getConfig().getLong("auto-save-interval", 6000L);
        new BukkitRunnable() {
            @Override
            public void run() {
                displayManager.cleanup();
                persistenceManager.save();
            }
        }.runTaskTimer(this, saveInterval, saveInterval);

        getLogger().info("DisplayAPI v" + getDescription().getVersion() + " enabled.");
        if (com.wjddusrb03.displayapi.util.PlaceholderUtil.isAvailable()) {
            getLogger().info("PlaceholderAPI detected - placeholder support enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (persistenceManager != null) {
            persistenceManager.save();
        }
        if (displayManager != null) {
            displayManager.removeAll();
        }
        getLogger().info("DisplayAPI disabled.");
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }
}
