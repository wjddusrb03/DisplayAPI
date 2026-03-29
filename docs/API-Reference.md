# API Reference

## Entry Point: `DisplayAPI`

All API access starts from the static `DisplayAPI` class.

### Factory Methods

```java
DisplayAPI.text(Location)           → TextDisplayBuilder
DisplayAPI.block(Location)          → BlockDisplayBuilder
DisplayAPI.item(Location)           → ItemDisplayBuilder
DisplayAPI.popup(Location)          → PopupBuilder
DisplayAPI.interactive(Location)    → InteractiveBuilder
DisplayAPI.animate(SpawnedDisplay)  → AnimationBuilder
DisplayAPI.follow(SpawnedDisplay, Entity) → FollowDisplay
DisplayAPI.leaderboard(Location)    → Leaderboard
DisplayAPI.group(String id, Location) → DisplayGroup
```

### Management

```java
DisplayAPI.getById(String id)       → SpawnedDisplay
DisplayAPI.remove(String id)        → void
DisplayAPI.removeAll()              → void
DisplayAPI.getManager()             → DisplayManager
DisplayAPI.getPersistenceManager()  → PersistenceManager
DisplayAPI.getPlugin()              → Plugin
DisplayAPI.getDefaultViewRange()    → float
```

## Class Overview

### Builders (`com.wjddusrb03.displayapi.builder`)

| Class | Description |
|-------|-------------|
| `AbstractDisplayBuilder<T>` | Base builder with common properties |
| `TextDisplayBuilder` | Text hologram builder |
| `BlockDisplayBuilder` | Block display builder |
| `ItemDisplayBuilder` | Item display builder |
| `PopupBuilder` | Animated popup builder |
| `InteractiveBuilder` | Clickable display builder |

### Display (`com.wjddusrb03.displayapi.display`)

| Class | Description |
|-------|-------------|
| `SpawnedDisplay` | Wrapper around a spawned Display entity |
| `DisplayGroup` | Groups multiple displays |
| `InteractiveDisplay` | Display + Interaction entity pair |
| `FollowDisplay` | Display that follows an entity |
| `Leaderboard` | Auto-updating ranked display |

### Animation (`com.wjddusrb03.displayapi.animation`)

| Class | Description |
|-------|-------------|
| `AnimationBuilder` | Builder for animations (presets + custom) |
| `DisplayAnimation` | Running animation controller |
| `Keyframe` | Single animation keyframe |
| `Easing` | 12 easing function enum |

### Manager (`com.wjddusrb03.displayapi.manager`)

| Class | Description |
|-------|-------------|
| `DisplayManager` | Tracks all active displays, groups, interactives |
| `PersistenceManager` | YAML save/load for persistent displays |

### Utility (`com.wjddusrb03.displayapi.util`)

| Class | Description |
|-------|-------------|
| `PlaceholderUtil` | Safe PlaceholderAPI integration |

### Listener (`com.wjddusrb03.displayapi.listener`)

| Class | Description |
|-------|-------------|
| `DisplayListener` | Per-player visibility on join |
| `InteractionListener` | Routes click events to InteractiveDisplay |

## Package Structure

```
com.wjddusrb03.displayapi
├── DisplayAPI.java              (static entry point)
├── DisplayAPIPlugin.java        (plugin main class)
├── builder/
│   ├── AbstractDisplayBuilder   (base builder)
│   ├── TextDisplayBuilder
│   ├── BlockDisplayBuilder
│   ├── ItemDisplayBuilder
│   ├── PopupBuilder
│   └── InteractiveBuilder
├── display/
│   ├── SpawnedDisplay           (spawned entity wrapper)
│   ├── DisplayGroup             (group management)
│   ├── InteractiveDisplay       (click detection)
│   ├── FollowDisplay            (entity tracking)
│   └── Leaderboard              (ranked display)
├── animation/
│   ├── AnimationBuilder         (animation factory)
│   ├── DisplayAnimation         (animation runner)
│   ├── Keyframe                 (animation data)
│   └── Easing                   (12 easing curves)
├── manager/
│   ├── DisplayManager           (display tracking)
│   └── PersistenceManager       (YAML persistence)
├── listener/
│   ├── DisplayListener          (visibility events)
│   └── InteractionListener      (click events)
├── util/
│   └── PlaceholderUtil          (PlaceholderAPI)
└── command/
    └── DisplayAPICommand        (admin commands)
```
