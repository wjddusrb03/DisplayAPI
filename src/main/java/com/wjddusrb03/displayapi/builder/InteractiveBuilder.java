package com.wjddusrb03.displayapi.builder;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.InteractiveDisplay;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Builder for creating interactive (clickable) displays.
 *
 * <p>Pairs a Display entity with an Interaction entity for click detection.
 */
public class InteractiveBuilder {

    private final Location location;
    private float hitboxWidth = 1.0f;
    private float hitboxHeight = 1.0f;
    private Consumer<Player> leftClickHandler;
    private Consumer<Player> rightClickHandler;
    private long cooldownMs = 200;
    private boolean responsive = true;

    // Display config
    private enum DisplayType { TEXT, BLOCK, ITEM }
    private DisplayType type = DisplayType.TEXT;
    private Component text = Component.text("Click me!");
    private Material blockMaterial;
    private ItemStack itemStack;
    private Billboard billboard = Billboard.CENTER;
    private Color glowColor;
    private float scale = 1.0f;

    public InteractiveBuilder(Location location) {
        this.location = location.clone();
    }

    // Hitbox
    public InteractiveBuilder hitbox(float width, float height) {
        this.hitboxWidth = width;
        this.hitboxHeight = height;
        return this;
    }

    // Click handlers
    public InteractiveBuilder onClick(Consumer<Player> handler) {
        this.leftClickHandler = handler;
        return this;
    }

    public InteractiveBuilder onRightClick(Consumer<Player> handler) {
        this.rightClickHandler = handler;
        return this;
    }

    public InteractiveBuilder cooldown(long ms) {
        this.cooldownMs = ms;
        return this;
    }

    public InteractiveBuilder responsive(boolean responsive) {
        this.responsive = responsive;
        return this;
    }

    // Display type setters
    public InteractiveBuilder text(Component text) {
        this.type = DisplayType.TEXT;
        this.text = text;
        return this;
    }

    public InteractiveBuilder text(String text) {
        return text(Component.text(text));
    }

    public InteractiveBuilder block(Material material) {
        this.type = DisplayType.BLOCK;
        this.blockMaterial = material;
        return this;
    }

    public InteractiveBuilder item(ItemStack item) {
        this.type = DisplayType.ITEM;
        this.itemStack = item;
        return this;
    }

    public InteractiveBuilder billboard(Billboard billboard) {
        this.billboard = billboard;
        return this;
    }

    public InteractiveBuilder glow(Color color) {
        this.glowColor = color;
        return this;
    }

    public InteractiveBuilder scale(float scale) {
        this.scale = scale;
        return this;
    }

    public InteractiveDisplay spawn() {
        var world = location.getWorld();
        if (world == null) throw new IllegalStateException("Location has no world");

        // Spawn the display
        SpawnedDisplay spawned = switch (type) {
            case TEXT -> {
                var builder = DisplayAPI.text(location)
                        .text(text)
                        .billboard(billboard)
                        .noBackground()
                        .shadowed(true)
                        .scale(scale);
                if (glowColor != null) builder.glow(glowColor);
                yield builder.spawn();
            }
            case BLOCK -> {
                var builder = DisplayAPI.block(location)
                        .block(blockMaterial)
                        .billboard(billboard)
                        .scale(scale);
                if (glowColor != null) builder.glow(glowColor);
                yield builder.spawn();
            }
            case ITEM -> {
                var builder = DisplayAPI.item(location)
                        .item(itemStack)
                        .billboard(billboard)
                        .scale(scale);
                if (glowColor != null) builder.glow(glowColor);
                yield builder.spawn();
            }
        };

        // Spawn the interaction entity at the same location
        Interaction interaction = world.spawn(location, Interaction.class, i -> {
            i.setInteractionWidth(hitboxWidth);
            i.setInteractionHeight(hitboxHeight);
            i.setPersistent(false);
            i.setResponsive(responsive);
        });

        InteractiveDisplay interactive = new InteractiveDisplay(spawned, interaction);
        interactive.setOnClick(leftClickHandler);
        interactive.setOnRightClick(rightClickHandler);
        interactive.setCooldown(cooldownMs);

        // Register with manager for event routing
        DisplayAPI.getManager().registerInteractive(interactive);

        return interactive;
    }
}
