# Follow

Makes a display entity follow a target entity (player, mob, etc.) with smooth movement.

## Basic Usage

```java
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("Name Tag"))
    .noBackground()
    .spawn();

FollowDisplay follow = DisplayAPI.follow(display, player)
    .offset(0, 2.5, 0)
    .smoothTeleport(3)
    .start();
```

## Methods

| Method | Description | Default |
|--------|-------------|---------|
| `.offset(x, y, z)` | Position offset from target | `0, 2.5, 0` |
| `.smoothTeleport(int ticks)` | Interpolation duration for smooth movement | `3` |
| `.updateInterval(int ticks)` | How often to update position | `1` |
| `.start()` | Begin following | - |
| `.stop()` | Stop following | - |
| `.remove()` | Stop and remove display | - |

## Auto-Stop Conditions

FollowDisplay automatically stops when:
- The display entity dies or is removed
- The target entity dies or is invalid
- The target player goes offline

## Examples

### Player Name Above Head
```java
SpawnedDisplay tag = DisplayAPI.text(player.getLocation())
    .text(Component.text(player.getName()).color(NamedTextColor.AQUA))
    .noBackground()
    .billboard(Billboard.CENTER)
    .spawn();

DisplayAPI.follow(tag, player)
    .offset(0, 2.3, 0)
    .start();
```

### Pet Health Bar
```java
SpawnedDisplay hp = DisplayAPI.text(pet.getLocation())
    .text(Component.text("HP: " + pet.getHealth()))
    .noBackground()
    .spawn();

DisplayAPI.follow(hp, pet)
    .offset(0, 1.5, 0)
    .smoothTeleport(2)
    .start();
```

### Arrow Indicator Over Entity
```java
SpawnedDisplay arrow = DisplayAPI.text(target.getLocation())
    .text(Component.text("V").color(NamedTextColor.RED))
    .noBackground()
    .spawn();

FollowDisplay f = DisplayAPI.follow(arrow, target)
    .offset(0, 3.0, 0)
    .start();

// Stop after 10 seconds
new BukkitRunnable() {
    public void run() { f.remove(); }
}.runTaskLater(plugin, 200L);
```
