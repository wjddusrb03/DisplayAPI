package com.wjddusrb03.displayapi.display;

import com.wjddusrb03.displayapi.DisplayAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Supplier;

/**
 * Auto-updating leaderboard built from TextDisplay entities.
 *
 * <p>Usage:
 * <pre>
 * DisplayAPI.leaderboard(location)
 *     .title(Component.text("Kill Rankings").color(NamedTextColor.GOLD))
 *     .dataSupplier(() -> getKillMap())
 *     .maxRows(10)
 *     .updateInterval(100)
 *     .spawn();
 * </pre>
 */
public class Leaderboard {

    private final Location location;
    private Component title = Component.text("Leaderboard").color(NamedTextColor.GOLD)
            .decoration(TextDecoration.BOLD, true);
    private int maxRows = 10;
    private float lineSpacing = 0.3f;
    private int updateIntervalTicks = 100;
    private Billboard billboard = Billboard.CENTER;
    private Supplier<Map<String, Integer>> dataSupplier;
    private Map<String, Integer> staticData;
    private Set<UUID> viewers = null;

    // Runtime state
    private DisplayGroup group;
    private BukkitRunnable updateTask;

    public Leaderboard(Location location) {
        this.location = location.clone();
    }

    public Leaderboard title(Component title) {
        this.title = title;
        return this;
    }

    public Leaderboard title(String title) {
        this.title = Component.text(title).color(NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true);
        return this;
    }

    public Leaderboard maxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public Leaderboard lineSpacing(float spacing) {
        this.lineSpacing = spacing;
        return this;
    }

    public Leaderboard updateInterval(int ticks) {
        this.updateIntervalTicks = ticks;
        return this;
    }

    public Leaderboard billboard(Billboard billboard) {
        this.billboard = billboard;
        return this;
    }

    public Leaderboard dataSupplier(Supplier<Map<String, Integer>> supplier) {
        this.dataSupplier = supplier;
        return this;
    }

    public Leaderboard data(Map<String, Integer> data) {
        this.staticData = new LinkedHashMap<>(data);
        return this;
    }

    public Leaderboard entry(String name, int value) {
        if (staticData == null) staticData = new LinkedHashMap<>();
        staticData.put(name, value);
        return this;
    }

    public Leaderboard visibleTo(Player... players) {
        this.viewers = new HashSet<>();
        for (Player p : players) {
            this.viewers.add(p.getUniqueId());
        }
        return this;
    }

    public Leaderboard spawn() {
        buildDisplay();

        // Auto-update if data supplier exists
        if (dataSupplier != null && updateIntervalTicks > 0) {
            updateTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (group == null || !group.isAlive()) {
                        cancel();
                        return;
                    }
                    rebuild();
                }
            };
            updateTask.runTaskTimer(DisplayAPI.getPlugin(), updateIntervalTicks, updateIntervalTicks);
        }

        return this;
    }

    private void buildDisplay() {
        if (group != null) {
            group.remove();
            DisplayAPI.getManager().removeGroup(group.getId());
        }

        String groupId = "lb-" + UUID.randomUUID().toString().substring(0, 8);
        group = new DisplayGroup(groupId, location);

        // Title line
        var titleBuilder = DisplayAPI.text(location)
                .text(title)
                .billboard(billboard)
                .noBackground()
                .shadowed(true);
        if (viewers != null) {
            titleBuilder.visibleTo(viewers.stream()
                    .map(org.bukkit.Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .toList());
        }
        group.add(titleBuilder.spawn());

        // Separator
        Location sepLoc = location.clone().add(0, -lineSpacing, 0);
        var sepBuilder = DisplayAPI.text(sepLoc)
                .text(Component.text("─────────────").color(NamedTextColor.DARK_GRAY))
                .billboard(billboard)
                .noBackground();
        if (viewers != null) {
            sepBuilder.visibleTo(viewers.stream()
                    .map(org.bukkit.Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .toList());
        }
        group.add(sepBuilder.spawn());

        // Data rows
        Map<String, Integer> data = dataSupplier != null ? dataSupplier.get() : staticData;
        if (data == null) data = Map.of();

        List<Map.Entry<String, Integer>> sorted = data.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(maxRows)
                .toList();

        int rank = 1;
        for (Map.Entry<String, Integer> entry : sorted) {
            float yOffset = -(lineSpacing * (rank + 1));
            Location rowLoc = location.clone().add(0, yOffset, 0);

            NamedTextColor rankColor = switch (rank) {
                case 1 -> NamedTextColor.GOLD;
                case 2 -> NamedTextColor.GRAY;
                case 3 -> NamedTextColor.RED;
                default -> NamedTextColor.WHITE;
            };

            Component row = Component.text("#" + rank + " ", rankColor)
                    .append(Component.text(entry.getKey()).color(NamedTextColor.YELLOW))
                    .append(Component.text(" - ").color(NamedTextColor.DARK_GRAY))
                    .append(Component.text(String.valueOf(entry.getValue())).color(NamedTextColor.AQUA));

            var rowBuilder = DisplayAPI.text(rowLoc)
                    .text(row)
                    .billboard(billboard)
                    .noBackground();
            if (viewers != null) {
                rowBuilder.visibleTo(viewers.stream()
                        .map(org.bukkit.Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .toList());
            }
            group.add(rowBuilder.spawn());
            rank++;
        }

        DisplayAPI.getManager().registerGroup(group);
    }

    public void rebuild() {
        buildDisplay();
    }

    public void remove() {
        if (updateTask != null) {
            try {
                updateTask.cancel();
            } catch (IllegalStateException ignored) {}
        }
        if (group != null) {
            group.remove();
            DisplayAPI.getManager().removeGroup(group.getId());
        }
    }

    public DisplayGroup getGroup() {
        return group;
    }

    public boolean isAlive() {
        return group != null && group.isAlive();
    }
}
