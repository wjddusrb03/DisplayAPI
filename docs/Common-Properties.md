# Common Properties

These properties are available on all display builders (TextDisplay, BlockDisplay, ItemDisplay).

## Methods

| Method | Description | Default |
|--------|-------------|---------|
| `.billboard(Billboard)` | How the display faces the player | `CENTER` |
| `.viewRange(float)` | Render distance multiplier | `1.0` (from config) |
| `.scale(float)` | Uniform scale | `1.0` |
| `.scale(x, y, z)` | Non-uniform scale | `1.0, 1.0, 1.0` |
| `.translation(x, y, z)` | Position offset | `0, 0, 0` |
| `.glow(Color)` | Glowing outline color | disabled |
| `.brightness(block, sky)` | Fixed brightness (0-15) | auto |
| `.shadow(radius, strength)` | Shadow settings | disabled |
| `.visibleTo(Player...)` | Per-player visibility | all players |
| `.persistent(boolean)` | Survive server restarts | `false` |
| `.id(String)` | Unique identifier | auto-generated |

## Billboard Modes

```java
Billboard.CENTER     // Always faces the player (default)
Billboard.FIXED      // Never rotates
Billboard.HORIZONTAL // Rotates horizontally only
Billboard.VERTICAL   // Rotates vertically only
```

## Scale

```java
.scale(0.5f)              // Half size (uniform)
.scale(2.0f)              // Double size (uniform)
.scale(1.0f, 2.0f, 1.0f)  // Tall and narrow
```

## Glow

```java
.glow(Color.RED)
.glow(Color.fromRGB(255, 128, 0))  // Orange
.glow(Color.AQUA)
```

## Brightness

Lock brightness regardless of surrounding light level.

```java
.brightness(15, 15)  // Maximum brightness (like glowstone)
.brightness(0, 0)    // Complete darkness
```

## Shadow

```java
.shadow(1.0f, 1.0f)    // Normal shadow
.shadow(3.0f, 0.5f)    // Large, faint shadow
```

## Visibility & Persistence

See [[Per-Player-Visibility]] and [[Persistence]] for detailed guides.
