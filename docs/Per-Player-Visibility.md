# Per-Player Visibility

Show displays to specific players only. Other players won't see them.

## Usage

```java
// Visible to one player
DisplayAPI.text(location)
    .text(Component.text("Only you can see this"))
    .visibleTo(player)
    .spawn();

// Visible to multiple players
DisplayAPI.text(location)
    .text(Component.text("Team display"))
    .visibleTo(player1, player2, player3)
    .spawn();

// From a collection
DisplayAPI.text(location)
    .text(Component.text("Team display"))
    .visibleTo(teamPlayers)
    .spawn();
```

## How It Works

1. The display entity is spawned with `setVisibleByDefault(false)`
2. `player.showEntity(plugin, entity)` is called for each viewer
3. When a viewer joins the server, `DisplayListener` automatically re-shows the entity

## Runtime Visibility Control

```java
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("Secret"))
    .visibleTo(player)
    .spawn();

// Show to additional player
display.showTo(anotherPlayer);

// Hide from a player
display.hideFrom(player);
```

## Notes

- If `.visibleTo()` is not called, the display is visible to all players (default)
- Per-player visibility works with all display types (text, block, item)
- Popups also support `.visibleTo()` for per-player damage numbers
- New players joining will automatically see per-player displays if they're in the viewer list
