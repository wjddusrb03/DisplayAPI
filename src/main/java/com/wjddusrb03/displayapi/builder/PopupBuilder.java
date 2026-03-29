package com.wjddusrb03.displayapi.builder;

import com.wjddusrb03.displayapi.DisplayAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Creates animated text popups that rise and fade.
 * Commonly used for damage numbers, healing indicators, etc.
 */
public class PopupBuilder {

    private final Location location;
    private Component text = Component.empty();
    private int durationTicks = 20;
    private float riseSpeed = 0.05f;
    private float startScale = 1.0f;
    private float endScale = 0.5f;
    private Billboard billboard = Billboard.CENTER;
    private Set<UUID> viewers = null;
    private Color backgroundColor = null;
    private boolean shadowed = true;

    public PopupBuilder(Location location) {
        this.location = location.clone();
    }

    public PopupBuilder text(Component text) {
        this.text = text;
        return this;
    }

    public PopupBuilder text(String text) {
        this.text = Component.text(text);
        return this;
    }

    public PopupBuilder duration(int ticks) {
        this.durationTicks = ticks;
        return this;
    }

    public PopupBuilder riseSpeed(float speed) {
        this.riseSpeed = speed;
        return this;
    }

    public PopupBuilder startScale(float scale) {
        this.startScale = scale;
        return this;
    }

    public PopupBuilder endScale(float scale) {
        this.endScale = scale;
        return this;
    }

    public PopupBuilder billboard(Billboard billboard) {
        this.billboard = billboard;
        return this;
    }

    public PopupBuilder visibleTo(Player... players) {
        this.viewers = new HashSet<>();
        for (Player p : players) {
            this.viewers.add(p.getUniqueId());
        }
        return this;
    }

    public PopupBuilder background(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public PopupBuilder noBackground() {
        this.backgroundColor = Color.fromARGB(0, 0, 0, 0);
        return this;
    }

    public PopupBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }

    public void spawn() {
        org.bukkit.World world = location.getWorld();
        if (world == null) return;

        TextDisplay display = world.spawn(location, TextDisplay.class, td -> {
            td.text(text);
            td.setBillboard(billboard);
            td.setShadowed(shadowed);
            td.setPersistent(false);

            if (backgroundColor != null) {
                td.setBackgroundColor(backgroundColor);
            } else {
                td.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            }

            Vector3f s = new Vector3f(startScale, startScale, startScale);
            Vector3f t = new Vector3f(0f, 0f, 0f);
            AxisAngle4f noRot = new AxisAngle4f(0f, 0f, 1f, 0f);
            td.setTransformation(new Transformation(t, noRot, s, noRot));

            if (viewers != null) {
                td.setVisibleByDefault(false);
            }
        });

        // Show to specific players
        if (viewers != null) {
            var plugin = DisplayAPI.getPlugin();
            for (UUID uuid : viewers) {
                Player p = org.bukkit.Bukkit.getPlayer(uuid);
                if (p != null && p.isOnline()) {
                    p.showEntity(plugin, display);
                }
            }
        }

        // Animate: rise + scale down + fade
        new BukkitRunnable() {
            int tick = 0;
            float currentY = 0f;

            @Override
            public void run() {
                if (tick >= durationTicks || display.isDead()) {
                    if (!display.isDead()) display.remove();
                    cancel();
                    return;
                }

                float progress = (float) tick / durationTicks; // 0.0 -> 1.0
                currentY += riseSpeed;

                // Interpolated scale
                float currentScale = startScale + (endScale - startScale) * progress;

                // Opacity fade (255 -> 0)
                byte opacity = (byte) ((1f - progress) * 255);

                display.setTextOpacity(opacity);

                Vector3f s = new Vector3f(currentScale, currentScale, currentScale);
                Vector3f t = new Vector3f(0f, currentY, 0f);
                AxisAngle4f noRot = new AxisAngle4f(0f, 0f, 1f, 0f);
                display.setInterpolationDelay(0);
                display.setInterpolationDuration(2);
                display.setTransformation(new Transformation(t, noRot, s, noRot));

                tick++;
            }
        }.runTaskTimer(DisplayAPI.getPlugin(), 0L, 1L);
    }
}
