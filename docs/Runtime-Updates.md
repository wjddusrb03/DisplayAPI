# Runtime Updates

Update display properties after spawning.

## Methods

```java
SpawnedDisplay display = DisplayAPI.getById("my-display");
```

### Text Updates
```java
display.updateText(Component.text("New text").color(NamedTextColor.RED));
display.updateText("Plain text shorthand");
```

### Block Updates
```java
display.updateBlock(Material.GOLD_BLOCK);
display.updateBlock(Material.OAK_STAIRS.createBlockData("[facing=north]"));
```

### Item Updates
```java
display.updateItem(new ItemStack(Material.DIAMOND_SWORD));
display.updateItem(Material.BOW);
```

### Visual Properties
```java
display.setGlowing(true);
display.setGlowColor(Color.RED);
display.setBillboard(Billboard.FIXED);
```

### Movement
```java
// Instant teleport
display.teleport(newLocation);

// Smooth teleport (interpolated over ticks, max 59)
display.smoothTeleport(newLocation, 5);
```

### Lifecycle
```java
display.isAlive();     // Check if entity still exists
display.remove();      // Remove the entity
display.getEntity();   // Access underlying Display entity
display.getLocation(); // Current location
```

## Finding Displays

```java
// By ID
SpawnedDisplay display = DisplayAPI.getById("my-id");

// Remove by ID
DisplayAPI.remove("my-id");

// Remove all
DisplayAPI.removeAll();
```

## Example: Cycling Text

```java
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("Message 1"))
    .noBackground()
    .id("cycling")
    .spawn();

String[] messages = {"Message 1", "Message 2", "Message 3"};
new BukkitRunnable() {
    int index = 0;
    public void run() {
        if (!display.isAlive()) { cancel(); return; }
        display.updateText(Component.text(messages[index % messages.length]));
        index++;
    }
}.runTaskTimer(plugin, 0L, 60L); // Every 3 seconds
```
