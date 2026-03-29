# BlockDisplay

Creates block displays using Minecraft's BlockDisplay entity.

## Basic Usage

```java
SpawnedDisplay display = DisplayAPI.block(location)
    .block(Material.DIAMOND_BLOCK)
    .spawn();
```

## Methods

### `.block(Material material)`
Sets the block type.

```java
.block(Material.DIAMOND_BLOCK)
.block(Material.BEACON)
.block(Material.TNT)
```

### `.block(BlockData blockData)`
Sets block with specific state data.

```java
.block(Material.OAK_STAIRS.createBlockData("[facing=north,half=top]"))
```

### `.block(String blockDataString)`
Sets block from a string representation.

```java
.block("minecraft:oak_stairs[facing=north]")
```

## Examples

### Mini Block Showcase
```java
DisplayAPI.block(location)
    .block(Material.DIAMOND_BLOCK)
    .scale(0.3f)
    .billboard(Billboard.CENTER)
    .glow(Color.AQUA)
    .spawn();
```

### Spinning Block
```java
SpawnedDisplay block = DisplayAPI.block(location)
    .block(Material.NETHER_STAR)
    .scale(0.5f)
    .spawn();

DisplayAPI.animate(block)
    .spin(AnimationBuilder.Axis.Y, 40)
    .loop(true)
    .play();
```

See also: [[Common-Properties]] for shared builder options.
