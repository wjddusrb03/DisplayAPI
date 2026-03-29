# ItemDisplay

Creates item displays using Minecraft's ItemDisplay entity.

## Basic Usage

```java
SpawnedDisplay display = DisplayAPI.item(location)
    .item(new ItemStack(Material.DIAMOND_SWORD))
    .spawn();
```

## Methods

### `.item(ItemStack item)`
Sets the displayed item.

```java
.item(new ItemStack(Material.DIAMOND_SWORD))

// Custom item with enchantment glow
ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
sword.addEnchantment(Enchantment.SHARPNESS, 5);
.item(sword)
```

### `.item(Material material)`
Shorthand for a simple item.

```java
.item(Material.GOLDEN_APPLE)
```

### `.transform(ItemDisplayTransform transform)`
Sets how the item is rendered.

Options: `NONE`, `THIRDPERSON_LEFTHAND`, `THIRDPERSON_RIGHTHAND`, `FIRSTPERSON_LEFTHAND`, `FIRSTPERSON_RIGHTHAND`, `HEAD`, `GUI`, `GROUND`, `FIXED`

```java
.transform(ItemDisplay.ItemDisplayTransform.GROUND)
.transform(ItemDisplay.ItemDisplayTransform.GUI)
```

## Examples

### Floating Reward Item
```java
SpawnedDisplay item = DisplayAPI.item(location)
    .item(new ItemStack(Material.NETHER_STAR))
    .billboard(Billboard.CENTER)
    .glow(Color.YELLOW)
    .spawn();

DisplayAPI.animate(item)
    .floating(0.3f, 40)
    .loop(true)
    .play();
```

See also: [[Common-Properties]] for shared builder options.
