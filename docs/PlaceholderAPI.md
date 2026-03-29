# PlaceholderAPI Integration

DisplayAPI provides a utility class for safe PlaceholderAPI integration. Works even without PlaceholderAPI installed.

## Setup

PlaceholderAPI is an **optional** dependency. Install it on your server if you want placeholder support.

## Usage

```java
import com.wjddusrb03.displayapi.util.PlaceholderUtil;

// Check availability
if (PlaceholderUtil.isAvailable()) {
    // PlaceholderAPI is installed
}

// Parse string placeholders
String parsed = PlaceholderUtil.parse(player, "HP: %player_health%");
// Result: "HP: 20.0"

// Parse Component placeholders
Component comp = Component.text("Level: %player_level%");
Component parsed = PlaceholderUtil.parse(player, comp);
```

## Common Placeholders

| Placeholder | Result | Source |
|-------------|--------|--------|
| `%player_name%` | Player name | Built-in |
| `%player_health%` | Current health | Built-in |
| `%player_max_health%` | Max health | Built-in |
| `%player_food_level%` | Hunger level | Built-in |
| `%player_level%` | XP level | Built-in |
| `%player_world%` | Current world | Built-in |
| `%player_ping%` | Network ping | Built-in |
| `%vault_eco_balance%` | Economy balance | Vault |
| `%luckperms_primary_group%` | Permission group | LuckPerms |
| `%server_online%` | Online player count | Built-in |
| `%server_tps%` | Server TPS | Built-in |

Full list: [PlaceholderAPI Placeholders Wiki](https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders)

## Example: Dynamic Health Display

```java
// Create display
SpawnedDisplay display = DisplayAPI.text(player.getLocation())
    .text(Component.text("HP: " + player.getHealth()))
    .noBackground()
    .spawn();

// Follow player
DisplayAPI.follow(display, player)
    .offset(0, 2.5, 0)
    .start();

// Update with placeholders every second
new BukkitRunnable() {
    public void run() {
        if (!display.isAlive()) { cancel(); return; }
        String text = PlaceholderUtil.parse(player, "HP: %player_health% / %player_max_health%");
        display.updateText(Component.text(text).color(NamedTextColor.RED));
    }
}.runTaskTimer(plugin, 0L, 20L);
```

## Safety

`PlaceholderUtil` is designed to be completely safe:
- Returns original text if PlaceholderAPI is not installed
- Returns original text if player is null
- Catches all exceptions silently
- No need for null checks or try-catch in your code
