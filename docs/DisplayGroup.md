# DisplayGroup

Groups multiple SpawnedDisplays together for batch operations (move, remove).

## Basic Usage

```java
DisplayGroup group = DisplayAPI.group("my-group", anchorLocation);
group.add(display1);
group.add(display2);
group.add(display3);

DisplayAPI.getManager().registerGroup(group);
```

## Methods

| Method | Description |
|--------|-------------|
| `.add(SpawnedDisplay)` | Add a display to the group |
| `.remove(SpawnedDisplay)` | Remove a specific display |
| `.teleport(Location)` | Move all displays (preserving relative positions) |
| `.remove()` | Remove all displays in group |
| `.cleanup()` | Remove dead displays from tracking |
| `.size()` | Number of displays |
| `.isAlive()` | True if any display is alive |
| `.getId()` | Group identifier |
| `.getDisplays()` | List of all displays |

## How Teleport Works

When you call `group.teleport(newLocation)`, it calculates the offset from the anchor and moves each display by the same delta, preserving their relative positions.

## Examples

### Multi-line Hologram
```java
Location anchor = location.clone().add(0, 3, 0);
DisplayGroup group = DisplayAPI.group("info", anchor);

group.add(DisplayAPI.text(anchor)
    .text(Component.text("Title").color(NamedTextColor.GOLD))
    .noBackground().spawn());

group.add(DisplayAPI.text(anchor.clone().add(0, -0.3, 0))
    .text(Component.text("Line 2").color(NamedTextColor.GRAY))
    .noBackground().spawn());

group.add(DisplayAPI.text(anchor.clone().add(0, -0.6, 0))
    .text(Component.text("Line 3").color(NamedTextColor.GRAY))
    .noBackground().spawn());

DisplayAPI.getManager().registerGroup(group);
```

### Moving a Group
```java
DisplayGroup group = DisplayAPI.getManager().getGroupById("info");
group.teleport(newLocation);
```

### Removing a Group
```java
DisplayAPI.getManager().removeGroup("info");
```
