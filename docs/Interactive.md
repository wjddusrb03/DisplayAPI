# Interactive (Click Detection)

Create clickable displays using Minecraft's Interaction entity paired with a Display entity.

## Basic Usage

```java
DisplayAPI.interactive(location)
    .text(Component.text("Click me!").color(NamedTextColor.GREEN))
    .onClick(player -> player.sendMessage("Left clicked!"))
    .onRightClick(player -> player.sendMessage("Right clicked!"))
    .spawn();
```

## Methods

| Method | Description | Default |
|--------|-------------|---------|
| `.text(Component)` | Text display content | "Click me!" |
| `.text(String)` | Plain text shorthand | - |
| `.block(Material)` | Use block display instead | - |
| `.item(ItemStack)` | Use item display instead | - |
| `.hitbox(width, height)` | Click detection area size | `1.0, 1.0` |
| `.onClick(Consumer<Player>)` | Left click handler | none |
| `.onRightClick(Consumer<Player>)` | Right click handler | none |
| `.cooldown(long ms)` | Cooldown between clicks | `200ms` |
| `.responsive(boolean)` | Visual feedback on hover | `true` |
| `.billboard(Billboard)` | Facing mode | `CENTER` |
| `.glow(Color)` | Glow color | none |
| `.scale(float)` | Display scale | `1.0` |

## How It Works

InteractiveDisplay spawns two entities at the same location:
1. **Display entity** - The visible part (text, block, or item)
2. **Interaction entity** - Invisible hitbox that detects player clicks

The Interaction entity routes click events to your handlers via `InteractionListener`.

## Examples

### Teleport Button
```java
DisplayAPI.interactive(location)
    .text(Component.text("[TELEPORT]").color(NamedTextColor.AQUA))
    .hitbox(2.0f, 0.8f)
    .scale(1.5f)
    .onClick(player -> {
        player.teleport(spawnLocation);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    })
    .spawn();
```

### Shop NPC Interaction
```java
DisplayAPI.interactive(npcLocation)
    .block(Material.CHEST)
    .hitbox(1.0f, 1.0f)
    .scale(0.8f)
    .onRightClick(player -> openShopGUI(player))
    .cooldown(500)
    .spawn();
```

### Toggle Button
```java
final boolean[] state = {false};

DisplayAPI.interactive(location)
    .text(Component.text("OFF").color(NamedTextColor.RED))
    .hitbox(1.5f, 0.5f)
    .onClick(player -> {
        state[0] = !state[0];
        // Update display text via manager
    })
    .spawn();
```

## Cleanup

InteractiveDisplay removes both entities when cleaned up:
```java
InteractiveDisplay interactive = DisplayAPI.interactive(location)
    .text(Component.text("Temp"))
    .onClick(p -> {})
    .spawn();

// Later: removes both display + interaction entity
interactive.remove();
```
