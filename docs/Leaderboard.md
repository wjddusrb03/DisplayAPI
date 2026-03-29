# Leaderboard

Auto-updating ranked display built from multiple TextDisplay entities.

## Basic Usage

```java
// Static data
DisplayAPI.leaderboard(location)
    .title("Kill Rankings")
    .entry("Player_A", 150)
    .entry("Player_B", 120)
    .entry("Player_C", 95)
    .maxRows(10)
    .spawn();
```

## Methods

| Method | Description | Default |
|--------|-------------|---------|
| `.title(String)` | Leaderboard title | "Leaderboard" |
| `.title(Component)` | Title with styling | gold + bold |
| `.data(Map<String, Integer>)` | Set all data at once | - |
| `.entry(String, int)` | Add single entry | - |
| `.dataSupplier(Supplier)` | Dynamic data source | - |
| `.maxRows(int)` | Maximum entries shown | `10` |
| `.lineSpacing(float)` | Vertical spacing between lines | `0.3` |
| `.updateInterval(int ticks)` | Auto-update frequency | `100` (5 sec) |
| `.billboard(Billboard)` | Facing mode | `CENTER` |
| `.visibleTo(Player...)` | Per-player visibility | all |

## Rank Colors

| Rank | Color |
|------|-------|
| #1 | Gold |
| #2 | Gray |
| #3 | Red |
| #4+ | White |

## Dynamic Leaderboard

Use `dataSupplier()` for data that changes over time:

```java
DisplayAPI.leaderboard(location)
    .title("Top Killers")
    .dataSupplier(() -> {
        Map<String, Integer> kills = new LinkedHashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            kills.put(p.getName(), getKills(p));
        }
        return kills;
    })
    .updateInterval(100)   // Update every 5 seconds
    .maxRows(5)
    .spawn();
```

## Cleanup

```java
Leaderboard lb = DisplayAPI.leaderboard(location)
    .title("Test")
    .data(data)
    .spawn();

// Later
lb.remove();  // Removes all display entities and stops update task
```
