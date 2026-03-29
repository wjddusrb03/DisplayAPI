package com.wjddusrb03.displayapi.display;

import com.wjddusrb03.displayapi.DisplayAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Makes a SpawnedDisplay follow a target entity (player, mob, etc.)
 *
 * <p>Usage:
 * <pre>
 * DisplayAPI.follow(display, player)
 *     .offset(0, 2.5, 0)
 *     .smoothTeleport(3)
 *     .start();
 * </pre>
 */
public class FollowDisplay {

    private final SpawnedDisplay display;
    private final Entity target;
    private double offsetX = 0, offsetY = 2.5, offsetZ = 0;
    private int updateInterval = 1;
    private int teleportDuration = 3;
    private boolean active = false;
    private BukkitRunnable task;

    public FollowDisplay(SpawnedDisplay display, Entity target) {
        this.display = display;
        this.target = target;
    }

    public FollowDisplay offset(double x, double y, double z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    public FollowDisplay updateInterval(int ticks) {
        this.updateInterval = Math.max(1, ticks);
        return this;
    }

    public FollowDisplay smoothTeleport(int durationTicks) {
        this.teleportDuration = durationTicks;
        return this;
    }

    public FollowDisplay start() {
        if (active) return this;
        active = true;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!display.isAlive() || target.isDead() || !target.isValid()) {
                    stop();
                    return;
                }

                if (target instanceof Player p && !p.isOnline()) {
                    stop();
                    return;
                }

                Location targetLoc = target.getLocation();
                if (targetLoc == null) return;
                display.smoothTeleport(targetLoc.add(offsetX, offsetY, offsetZ), teleportDuration);
            }
        };
        task.runTaskTimer(DisplayAPI.getPlugin(), 0L, updateInterval);
        return this;
    }

    public void stop() {
        active = false;
        if (task != null) {
            try {
                task.cancel();
            } catch (IllegalStateException ignored) {}
        }
    }

    public boolean isActive() {
        return active;
    }

    public void remove() {
        stop();
        display.remove();
    }
}
