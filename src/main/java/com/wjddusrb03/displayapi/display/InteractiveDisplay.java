package com.wjddusrb03.displayapi.display;

import com.wjddusrb03.displayapi.DisplayAPI;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A Display entity paired with an Interaction entity for click detection.
 *
 * <p>Usage:
 * <pre>
 * DisplayAPI.interactive(location)
 *     .text(Component.text("Click me!"))
 *     .hitbox(1.5f, 0.5f)
 *     .onClick(player -> player.sendMessage("Left clicked!"))
 *     .onRightClick(player -> player.sendMessage("Right clicked!"))
 *     .spawn();
 * </pre>
 */
public class InteractiveDisplay {

    private final SpawnedDisplay display;
    private final Interaction interaction;
    private Consumer<Player> leftClickHandler;
    private Consumer<Player> rightClickHandler;
    private long cooldownMs = 200;
    private final Map<UUID, Long> lastInteraction = new ConcurrentHashMap<>();

    public InteractiveDisplay(SpawnedDisplay display, Interaction interaction) {
        this.display = display;
        this.interaction = interaction;
    }

    public SpawnedDisplay getDisplay() {
        return display;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setOnClick(Consumer<Player> handler) {
        this.leftClickHandler = handler;
    }

    public void setOnRightClick(Consumer<Player> handler) {
        this.rightClickHandler = handler;
    }

    public void setCooldown(long ms) {
        this.cooldownMs = ms;
    }

    public boolean handleLeftClick(Player player) {
        if (leftClickHandler == null) return false;
        if (isOnCooldown(player)) return false;
        markCooldown(player);
        leftClickHandler.accept(player);
        return true;
    }

    public boolean handleRightClick(Player player) {
        if (rightClickHandler == null) return false;
        if (isOnCooldown(player)) return false;
        markCooldown(player);
        rightClickHandler.accept(player);
        return true;
    }

    private boolean isOnCooldown(Player player) {
        Long last = lastInteraction.get(player.getUniqueId());
        return last != null && (System.currentTimeMillis() - last) < cooldownMs;
    }

    private void markCooldown(Player player) {
        lastInteraction.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public boolean isAlive() {
        return display.isAlive() && interaction != null && !interaction.isDead();
    }

    public void teleport(Location location) {
        display.teleport(location);
        if (interaction != null && !interaction.isDead()) {
            interaction.teleport(location);
        }
    }

    public void remove() {
        display.remove();
        if (interaction != null && !interaction.isDead()) {
            interaction.remove();
        }
    }

    public UUID getInteractionUUID() {
        return interaction != null ? interaction.getUniqueId() : null;
    }
}
