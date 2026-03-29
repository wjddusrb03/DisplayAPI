# Popup

Animated floating text that rises, shrinks, and fades away automatically. Perfect for damage numbers, healing indicators, XP notifications, etc.

## Basic Usage

```java
DisplayAPI.popup(location)
    .text(Component.text("-25").color(NamedTextColor.RED))
    .duration(30)
    .spawn();
```

## Methods

| Method | Description | Default |
|--------|-------------|---------|
| `.text(Component)` | Popup text | empty |
| `.text(String)` | Plain text shorthand | empty |
| `.duration(int ticks)` | How long before disappearing | `20` (1 sec) |
| `.riseSpeed(float)` | Vertical rise speed per tick | `0.05` |
| `.startScale(float)` | Initial size | `1.0` |
| `.endScale(float)` | Final size before disappearing | `0.5` |
| `.billboard(Billboard)` | Facing mode | `CENTER` |
| `.visibleTo(Player...)` | Show to specific players only | all |
| `.background(Color)` | Background color | transparent |
| `.noBackground()` | Force transparent background | - |
| `.shadowed(boolean)` | Text shadow | `true` |

## Behavior

The popup automatically:
1. Rises upward at `riseSpeed` per tick
2. Scales from `startScale` to `endScale`
3. Fades opacity from 255 to 0
4. Removes itself after `duration` ticks

No cleanup is needed - the entity is fully managed.

## Examples

### Damage Number
```java
DisplayAPI.popup(entity.getLocation().add(0, 1.5, 0))
    .text(Component.text("-" + damage).color(NamedTextColor.RED))
    .duration(25)
    .startScale(1.5f)
    .endScale(0.3f)
    .visibleTo(attacker)
    .spawn();
```

### Healing Number
```java
DisplayAPI.popup(player.getLocation().add(0, 2, 0))
    .text(Component.text("+" + healAmount).color(NamedTextColor.GREEN))
    .duration(20)
    .startScale(1.0f)
    .endScale(0.5f)
    .visibleTo(player)
    .spawn();
```

### XP Notification
```java
DisplayAPI.popup(player.getLocation().add(0, 2.5, 0))
    .text(Component.text("+50 XP").color(NamedTextColor.YELLOW))
    .duration(40)
    .riseSpeed(0.03f)
    .spawn();
```
