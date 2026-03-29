# TextDisplay

Creates floating text holograms using Minecraft's TextDisplay entity.

## Basic Usage

```java
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("Hello!").color(NamedTextColor.GOLD))
    .spawn();
```

## Methods

### `.text(Component text)`
Sets the display text using Adventure Component API.

```java
.text(Component.text("Colored text").color(NamedTextColor.RED))

// Multi-line
.text(Component.text("Line 1\nLine 2\nLine 3"))

// Styled
.text(Component.text("Bold!").decorate(TextDecoration.BOLD))
```

### `.text(String text)`
Shorthand for plain text.

```java
.text("Simple text")
```

### `.background(Color color)`
Sets the background color (ARGB).

```java
.background(Color.fromARGB(128, 0, 0, 0))  // Semi-transparent black
.background(Color.fromARGB(200, 255, 0, 0)) // Red with alpha
```

### `.background(int r, int g, int b, int a)`
Sets background with separate RGBA values.

```java
.background(0, 0, 0, 128)  // Semi-transparent black
```

### `.noBackground()`
Makes the background fully transparent.

```java
.noBackground()  // No background at all
```

### `.opacity(int opacity)`
Sets text opacity (0 = invisible, 255 = fully visible).

```java
.opacity(128)  // 50% transparent
```

### `.shadowed(boolean)`
Enables text shadow (like Minecraft's text rendering).

```java
.shadowed(true)
```

### `.seeThrough(boolean)`
Makes text visible through blocks.

```java
.seeThrough(true)
```

### `.alignment(TextAlignment)`
Text alignment: `CENTER`, `LEFT`, `RIGHT`.

```java
.alignment(TextAlignment.LEFT)
```

### `.lineWidth(int)`
Maximum line width before wrapping (default: 200).

```java
.lineWidth(100)  // Narrower text wrapping
```

## Examples

### Clean Hologram
```java
DisplayAPI.text(location)
    .text(Component.text("Welcome!")
        .color(NamedTextColor.GOLD)
        .decorate(TextDecoration.BOLD))
    .noBackground()
    .shadowed(true)
    .billboard(Billboard.CENTER)
    .spawn();
```

### Info Board
```java
DisplayAPI.text(location)
    .text(Component.text("Rules:\n1. Be kind\n2. No griefing\n3. Have fun!")
        .color(NamedTextColor.WHITE))
    .background(0, 0, 0, 180)
    .alignment(TextAlignment.LEFT)
    .lineWidth(150)
    .billboard(Billboard.FIXED)
    .spawn();
```

### Glowing Text
```java
DisplayAPI.text(location)
    .text(Component.text("IMPORTANT!").color(NamedTextColor.RED))
    .noBackground()
    .glow(Color.RED)
    .spawn();
```

See also: [[Common-Properties]] for shared builder options.
