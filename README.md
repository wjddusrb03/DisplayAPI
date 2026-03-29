# DisplayAPI

**Lightweight Display Entity API for Paper 1.21.4+**

A developer-friendly framework for creating and managing Display entities (TextDisplay, BlockDisplay, ItemDisplay) with zero external dependencies.

[한국어 README](README_KO.md)

---

## Features

- **Fluent Builder API** — Create text, block, and item displays in one line with intuitive chaining
- **Animation Engine** — 8 preset animations (pulse, spin, bounce, float, fade, shake...) + custom keyframes with 12 easing functions
- **Click Detection** — Pair displays with Interaction entities for left/right click handling
- **Entity Following** — Smoothly attach displays above players or mobs
- **Per-Player Visibility** — Show or hide displays for individual players
- **Display Groups** — Manage multiple displays as a single unit (move, remove together)
- **Leaderboards** — Auto-updating ranked displays with dynamic data
- **Popup System** — Rising + fading text for damage numbers, XP notifications, etc.
- **Persistence** — Save displays to YAML, survive server restarts
- **PlaceholderAPI** — Optional integration for dynamic placeholder text
- **Zero Dependencies** — Only requires Paper API, nothing else

## Quick Start

### For Plugin Developers

Add DisplayAPI as a dependency in your `plugin.yml`:
```yaml
depend:
  - DisplayAPI
```

### Basic Usage

```java
// Text hologram
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("Hello!").color(NamedTextColor.GOLD))
    .billboard(Billboard.CENTER)
    .shadowed(true)
    .noBackground()
    .spawn();

// Block display
DisplayAPI.block(location)
    .block(Material.DIAMOND_BLOCK)
    .scale(0.5f)
    .glow(Color.AQUA)
    .spawn();

// Item display
DisplayAPI.item(location)
    .item(new ItemStack(Material.DIAMOND_SWORD))
    .billboard(Billboard.CENTER)
    .spawn();
```

### Damage Popup

```java
DisplayAPI.popup(entity.getLocation().add(0, 2, 0))
    .text(Component.text("-25").color(NamedTextColor.RED))
    .duration(30)
    .startScale(1.5f)
    .endScale(0.3f)
    .visibleTo(attacker)
    .spawn();
```

### Animations

```java
// Pre-built animations
DisplayAPI.animate(display).pulse(0.8f, 1.5f, 30).loop(true).play();
DisplayAPI.animate(display).spin(AnimationBuilder.Axis.Y, 40).loop(true).play();
DisplayAPI.animate(display).floating(0.3f, 40).loop(true).play();
DisplayAPI.animate(display).shake(0.15f, 20).play();
DisplayAPI.animate(display).fadeIn(20).play();
DisplayAPI.animate(display).fadeOut(20).play();
DisplayAPI.animate(display).growIn(1.0f, 20).play();
DisplayAPI.animate(display).bounce(0.3f, 20).loop(true).play();

// Custom keyframe animation
DisplayAPI.animate(display)
    .keyframe(Keyframe.at(0).scale(1f).translation(0, 0, 0))
    .keyframe(Keyframe.at(20).scale(2f).translation(0, 1f, 0))
    .keyframe(Keyframe.at(40).scale(1f).translation(0, 0, 0))
    .easing(Easing.EASE_IN_OUT)
    .loop(true)
    .play();
```

**12 Easing Functions:** LINEAR, EASE_IN, EASE_OUT, EASE_IN_OUT, CUBIC_IN/OUT/IN_OUT, QUART_IN/OUT/IN_OUT, EASE_IN_BACK, EASE_OUT_BACK, BOUNCE, ELASTIC

### Clickable Display

```java
DisplayAPI.interactive(location)
    .text(Component.text("Click me!").color(NamedTextColor.GREEN))
    .hitbox(1.5f, 1.0f)
    .onClick(player -> player.sendMessage("Left clicked!"))
    .onRightClick(player -> player.sendMessage("Right clicked!"))
    .cooldown(200)
    .spawn();
```

### Entity Following

```java
DisplayAPI.follow(display, player)
    .offset(0, 2.5, 0)
    .smoothTeleport(3)
    .start();
```

### Display Group

```java
DisplayGroup group = DisplayAPI.group("my-group", anchorLocation);
group.add(display1);
group.add(display2);
group.add(display3);

group.teleport(newLocation);  // Move all together
group.remove();               // Remove all
```

### Leaderboard

```java
// Static data
DisplayAPI.leaderboard(location)
    .title("Kill Rankings")
    .entry("Player_A", 150)
    .entry("Player_B", 120)
    .maxRows(10)
    .spawn();

// Dynamic (auto-updating)
DisplayAPI.leaderboard(location)
    .title("Kill Rankings")
    .dataSupplier(() -> getKillStats())
    .updateInterval(100)
    .spawn();
```

### Per-Player Visibility

```java
DisplayAPI.text(location)
    .text(Component.text("Only you can see this"))
    .visibleTo(player)
    .spawn();
```

### Persistence

```java
DisplayAPI.text(location)
    .text(Component.text("I survive restarts"))
    .persistent(true)
    .id("permanent-hologram")
    .spawn();
```

### Runtime Updates

```java
SpawnedDisplay display = DisplayAPI.getById("my-display");

display.updateText(Component.text("Updated!"));
display.updateBlock(Material.GOLD_BLOCK);
display.updateItem(new ItemStack(Material.BOW));
display.setGlowing(true);
display.setGlowColor(Color.RED);
display.setBillboard(Billboard.FIXED);
display.smoothTeleport(newLocation, 5);
```

### PlaceholderAPI Support

```java
import com.wjddusrb03.displayapi.util.PlaceholderUtil;

// Parse placeholders for a player
String parsed = PlaceholderUtil.parse(player, "Health: %player_health%");
Component comp = PlaceholderUtil.parse(player, component);
```

## Common Properties (All Builders)

| Method | Description |
|--------|-------------|
| `.billboard(Billboard)` | CENTER, FIXED, HORIZONTAL, VERTICAL |
| `.viewRange(float)` | Render distance multiplier |
| `.scale(float)` | Uniform scale |
| `.scale(x, y, z)` | Non-uniform scale |
| `.translation(x, y, z)` | Position offset |
| `.glow(Color)` | Glowing outline with color |
| `.brightness(block, sky)` | Fixed brightness |
| `.shadow(radius, strength)` | Shadow settings |
| `.visibleTo(Player...)` | Per-player visibility |
| `.persistent(boolean)` | Survive server restarts |
| `.id(String)` | Unique identifier |

## Admin Commands

| Command | Description |
|---------|-------------|
| `/dapi create text <text> [id:name]` | Create text display (&color codes supported) |
| `/dapi create block <block> [id]` | Create block display |
| `/dapi create item <item> [id]` | Create item display |
| `/dapi edit <id> <property> <value>` | Edit display (text, block, item, scale, billboard, glow) |
| `/dapi move <id>` | Teleport display to your location |
| `/dapi animate <id> <type>` | Apply animation (pulse, spin, bounce, float, shake, fadein, fadeout, growin) |
| `/dapi popup <text>` | Create a popup text |
| `/dapi near [radius]` | List nearby displays |
| `/dapi remove <id>` | Remove by ID |
| `/dapi removeall` | Remove all displays |
| `/dapi list` | List active displays |
| `/dapi save` | Save persistent displays |
| `/dapi reload` | Reload config |
| `/dapi info` | Plugin info |

Permission: `displayapi.admin` (default: op)

## Requirements

- Paper 1.21.4+
- Java 21+
- (Optional) PlaceholderAPI

## Installation

1. Download `DisplayAPI-1.0.0.jar` from [Releases](https://github.com/wjddusrb03/DisplayAPI/releases)
2. Place in your server's `plugins/` folder
3. Restart the server

## License

MIT License
