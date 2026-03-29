package com.wjddusrb03.displayapi.display;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * Wrapper around a spawned Display entity with visibility and lifecycle control.
 */
public class SpawnedDisplay {

    private final Display entity;
    private final Plugin ownerPlugin;
    private final String id;
    private final Set<UUID> viewers; // null = visible to all
    private final boolean persistent;

    public SpawnedDisplay(Display entity, Plugin ownerPlugin, String id, Set<UUID> viewers, boolean persistent) {
        this.entity = entity;
        this.ownerPlugin = ownerPlugin;
        this.id = id;
        this.viewers = viewers;
        this.persistent = persistent;
    }

    public Display getEntity() {
        return entity;
    }

    public String getId() {
        return id;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isAlive() {
        return entity != null && !entity.isDead() && entity.isValid();
    }

    public Location getLocation() {
        return entity.getLocation();
    }

    public void teleport(Location location) {
        if (isAlive()) {
            entity.teleport(location);
        }
    }

    public void smoothTeleport(Location location, int durationTicks) {
        if (isAlive()) {
            entity.setTeleportDuration(Math.min(durationTicks, 59));
            entity.teleport(location);
        }
    }

    public void remove() {
        if (isAlive()) {
            entity.remove();
        }
    }

    public boolean isPerPlayer() {
        return viewers != null;
    }

    public Set<UUID> getViewers() {
        return viewers != null ? Collections.unmodifiableSet(viewers) : null;
    }

    public void showTo(Player player) {
        if (!isAlive()) return;
        player.showEntity(ownerPlugin, entity);
        if (viewers != null) {
            viewers.add(player.getUniqueId());
        }
    }

    public void hideFrom(Player player) {
        if (!isAlive()) return;
        player.hideEntity(ownerPlugin, entity);
        if (viewers != null) {
            viewers.remove(player.getUniqueId());
        }
    }

    public void applyToNewPlayer(Player player) {
        if (!isAlive()) return;
        if (viewers == null) return;
        if (viewers.contains(player.getUniqueId())) {
            player.showEntity(ownerPlugin, entity);
        }
    }

    // ========================
    // Update methods
    // ========================

    public void updateText(Component text) {
        if (!isAlive() || !(entity instanceof TextDisplay td)) return;
        td.text(text);
    }

    public void updateText(String text) {
        updateText(Component.text(text));
    }

    public void updateBlock(Material material) {
        if (!isAlive() || !(entity instanceof BlockDisplay bd)) return;
        bd.setBlock(material.createBlockData());
    }

    public void updateBlock(BlockData blockData) {
        if (!isAlive() || !(entity instanceof BlockDisplay bd)) return;
        bd.setBlock(blockData);
    }

    public void updateItem(ItemStack item) {
        if (!isAlive() || !(entity instanceof ItemDisplay id)) return;
        id.setItemStack(item);
    }

    public void updateItem(Material material) {
        updateItem(new ItemStack(material));
    }

    public void setGlowing(boolean glowing) {
        if (!isAlive()) return;
        entity.setGlowing(glowing);
    }

    public void setGlowColor(org.bukkit.Color color) {
        if (!isAlive()) return;
        entity.setGlowColorOverride(color);
    }

    public void setBillboard(Display.Billboard billboard) {
        if (!isAlive()) return;
        entity.setBillboard(billboard);
    }
}
