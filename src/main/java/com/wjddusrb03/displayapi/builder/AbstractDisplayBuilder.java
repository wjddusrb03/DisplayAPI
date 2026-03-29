package com.wjddusrb03.displayapi.builder;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;

/**
 * Base builder with common Display entity properties.
 *
 * @param <T> the concrete builder type for fluent chaining
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDisplayBuilder<T extends AbstractDisplayBuilder<T>> {

    protected final Location location;
    protected Billboard billboard = Billboard.CENTER;
    protected float viewRange = -1f; // -1 = use config default
    protected Set<UUID> viewers = null; // null = visible to all
    protected boolean persistent = false;
    protected String id = null;
    protected Color glowColor = null;
    protected boolean glowing = false;
    protected Brightness brightness = null;
    protected float shadowRadius = 0f;
    protected float shadowStrength = 0f;
    protected Vector3f scale = null;
    protected Vector3f translation = null;

    protected AbstractDisplayBuilder(Location location) {
        this.location = location.clone();
    }

    public T billboard(Billboard billboard) {
        this.billboard = billboard;
        return (T) this;
    }

    public T viewRange(float range) {
        this.viewRange = range;
        return (T) this;
    }

    public T visibleTo(Player... players) {
        this.viewers = new HashSet<>();
        for (Player p : players) {
            this.viewers.add(p.getUniqueId());
        }
        return (T) this;
    }

    public T visibleTo(Collection<Player> players) {
        this.viewers = new HashSet<>();
        for (Player p : players) {
            this.viewers.add(p.getUniqueId());
        }
        return (T) this;
    }

    public T persistent(boolean persistent) {
        this.persistent = persistent;
        return (T) this;
    }

    public T id(String id) {
        this.id = id;
        return (T) this;
    }

    public T glow(Color color) {
        this.glowing = true;
        this.glowColor = color;
        return (T) this;
    }

    public T brightness(int blockLight, int skyLight) {
        this.brightness = new Brightness(blockLight, skyLight);
        return (T) this;
    }

    public T shadow(float radius, float strength) {
        this.shadowRadius = radius;
        this.shadowStrength = strength;
        return (T) this;
    }

    public T scale(float uniform) {
        this.scale = new Vector3f(uniform, uniform, uniform);
        return (T) this;
    }

    public T scale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
        return (T) this;
    }

    public T translation(float x, float y, float z) {
        this.translation = new Vector3f(x, y, z);
        return (T) this;
    }

    protected void applyCommon(Display display) {
        display.setBillboard(billboard);

        float range = viewRange >= 0 ? viewRange : DisplayAPI.getDefaultViewRange();
        display.setViewRange(range);

        if (glowing) {
            display.setGlowing(true);
            if (glowColor != null) {
                display.setGlowColorOverride(glowColor);
            }
        }

        if (brightness != null) {
            display.setBrightness(brightness);
        }

        if (shadowRadius > 0) {
            display.setShadowRadius(shadowRadius);
            display.setShadowStrength(shadowStrength);
        }

        if (scale != null || translation != null) {
            Vector3f s = scale != null ? scale : new Vector3f(1f, 1f, 1f);
            Vector3f t = translation != null ? translation : new Vector3f(0f, 0f, 0f);
            AxisAngle4f noRot = new AxisAngle4f(0f, 0f, 1f, 0f);
            display.setTransformation(new Transformation(t, noRot, s, noRot));
        }

        display.setPersistent(false); // we manage persistence ourselves

        if (viewers != null) {
            display.setVisibleByDefault(false);
        }
    }

    protected SpawnedDisplay register(Display display) {
        Plugin plugin = DisplayAPI.getPlugin();
        String resolvedId = id != null ? id : UUID.randomUUID().toString().substring(0, 8);

        SpawnedDisplay spawned = new SpawnedDisplay(display, plugin, resolvedId, viewers, persistent);

        // Show to specific viewers
        if (viewers != null) {
            for (UUID uuid : viewers) {
                Player p = org.bukkit.Bukkit.getPlayer(uuid);
                if (p != null && p.isOnline()) {
                    p.showEntity(plugin, display);
                }
            }
        }

        // Register with manager
        DisplayAPI.getManager().register(spawned);

        return spawned;
    }

    public abstract SpawnedDisplay spawn();
}
