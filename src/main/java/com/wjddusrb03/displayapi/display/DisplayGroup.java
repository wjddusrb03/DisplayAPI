package com.wjddusrb03.displayapi.display;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A group of SpawnedDisplay entities that can be moved/removed together.
 */
public class DisplayGroup {

    private final String id;
    private final Location anchor;
    private final List<SpawnedDisplay> displays = new ArrayList<>();

    public DisplayGroup(String id, Location anchor) {
        this.id = id;
        this.anchor = anchor.clone();
    }

    public String getId() {
        return id;
    }

    public Location getAnchor() {
        return anchor.clone();
    }

    public void add(SpawnedDisplay display) {
        displays.add(display);
    }

    public List<SpawnedDisplay> getDisplays() {
        return Collections.unmodifiableList(displays);
    }

    public int size() {
        return displays.size();
    }

    public void teleport(Location newAnchor) {
        double dx = newAnchor.getX() - anchor.getX();
        double dy = newAnchor.getY() - anchor.getY();
        double dz = newAnchor.getZ() - anchor.getZ();

        for (SpawnedDisplay sd : displays) {
            if (sd.isAlive()) {
                Location newLoc = sd.getLocation().clone().add(dx, dy, dz);
                sd.teleport(newLoc);
            }
        }

        anchor.setX(newAnchor.getX());
        anchor.setY(newAnchor.getY());
        anchor.setZ(newAnchor.getZ());
        anchor.setWorld(newAnchor.getWorld());
    }

    public void smoothTeleport(Location newAnchor, int durationTicks) {
        double dx = newAnchor.getX() - anchor.getX();
        double dy = newAnchor.getY() - anchor.getY();
        double dz = newAnchor.getZ() - anchor.getZ();

        for (SpawnedDisplay sd : displays) {
            if (sd.isAlive()) {
                Location newLoc = sd.getLocation().clone().add(dx, dy, dz);
                sd.smoothTeleport(newLoc, durationTicks);
            }
        }

        anchor.setX(newAnchor.getX());
        anchor.setY(newAnchor.getY());
        anchor.setZ(newAnchor.getZ());
        anchor.setWorld(newAnchor.getWorld());
    }

    public void showTo(Player player) {
        for (SpawnedDisplay sd : displays) {
            sd.showTo(player);
        }
    }

    public void hideFrom(Player player) {
        for (SpawnedDisplay sd : displays) {
            sd.hideFrom(player);
        }
    }

    public void remove() {
        for (SpawnedDisplay sd : displays) {
            sd.remove();
        }
        displays.clear();
    }

    public boolean isAlive() {
        return displays.stream().anyMatch(SpawnedDisplay::isAlive);
    }

    public void cleanup() {
        displays.removeIf(sd -> !sd.isAlive());
    }
}
