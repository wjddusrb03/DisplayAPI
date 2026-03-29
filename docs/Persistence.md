# Persistence

DisplayAPI can save displays to YAML so they survive server restarts.

## Usage

```java
DisplayAPI.text(location)
    .text(Component.text("I survive restarts!"))
    .persistent(true)
    .id("my-permanent-hologram")
    .spawn();
```

## How It Works

1. Displays marked with `.persistent(true)` are tracked by `PersistenceManager`
2. On server shutdown or `/dapi save`, all persistent displays are saved to `plugins/DisplayAPI/displays.yml`
3. On server startup, persistent displays are automatically restored
4. Auto-save runs periodically (configurable in `config.yml`)

## Saved Properties

The following properties are persisted:
- Location (world, x, y, z)
- Display type (text, block, item)
- ID
- Billboard mode
- Scale and translation
- Glow color and glowing state
- Brightness
- Shadow radius and strength
- Text content (serialized via GsonComponentSerializer)
- Text opacity, shadow, see-through, alignment
- Background color
- Block type (Material)
- Item type (Material)

## Not Saved

- Per-player visibility (viewers list)
- Animations
- Interactive click handlers
- Follow targets

## Config

```yaml
# plugins/DisplayAPI/config.yml
auto-save-interval: 6000  # Ticks (6000 = 5 minutes)
```

## Commands

```
/dapi save       # Manual save
/dapi list       # Shows [P] for persistent displays
/dapi remove id  # Removes and deletes from save file
```

## Best Practices

- Always set a meaningful `.id()` for persistent displays so you can find them later
- Use `.persistent(true)` only for permanent fixtures (signs, holograms), not temporary effects
- Animations must be re-applied after server restart (they are not saved)
