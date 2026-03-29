# Animation

DisplayAPI includes a keyframe-based animation engine with 12 easing functions and 8 preset animations.

## Pre-built Animations

### Pulse
Scales between min and max size.
```java
DisplayAPI.animate(display).pulse(0.8f, 1.5f, 30).loop(true).play();
//                              minScale  maxScale  ticks
```

### Spin
Rotates 360 degrees on an axis.
```java
DisplayAPI.animate(display).spin(AnimationBuilder.Axis.Y, 40).loop(true).play();
//                                                axis   ticks
// Axis options: X, Y, Z
```

### Bounce
Moves up and drops back down.
```java
DisplayAPI.animate(display).bounce(0.3f, 20).loop(true).play();
//                               height  ticks
```

### Floating
Gentle bobbing up and down.
```java
DisplayAPI.animate(display).floating(0.3f, 40).loop(true).play();
//                                amplitude  ticks
```

### Fade In
Fades from invisible to visible (TextDisplay only).
```java
DisplayAPI.animate(display).fadeIn(20).play();
```

### Fade Out
Fades from visible to invisible (TextDisplay only).
```java
DisplayAPI.animate(display).fadeOut(20).play();
```

### Grow In
Scales from 0 to target size with overshoot effect.
```java
DisplayAPI.animate(display).growIn(1.0f, 20).play();
//                               targetScale  ticks
```

### Shake
Rapid horizontal oscillation with decay.
```java
DisplayAPI.animate(display).shake(0.15f, 20).play();
//                              intensity  ticks
```

## Custom Keyframe Animation

For full control, use the keyframe API:

```java
DisplayAPI.animate(display)
    .keyframe(Keyframe.at(0).scale(1f).translation(0, 0, 0))
    .keyframe(Keyframe.at(10).scale(1.5f).translation(0, 0.5f, 0))
    .keyframe(Keyframe.at(20).scale(1f).translation(0, 0, 0))
    .easing(Easing.EASE_IN_OUT)
    .loop(true)
    .play();
```

### Keyframe Properties

```java
Keyframe.at(tick)                       // Tick position
    .scale(float)                       // Uniform scale
    .scale(x, y, z)                     // Non-uniform scale
    .translation(x, y, z)              // Position offset
    .rotationX(degrees)                // X-axis rotation
    .rotationY(degrees)                // Y-axis rotation
    .rotationZ(degrees)                // Z-axis rotation
    .opacity(int)                       // Text opacity (0-255)
```

## Controlling Animations

```java
// play() returns a DisplayAnimation controller
DisplayAnimation anim = DisplayAPI.animate(display)
    .pulse(0.8f, 1.5f, 30)
    .loop(true)
    .play();

// Stop later
anim.stop();
```

## Easing Functions

See [[Easing-Functions]] for detailed descriptions of all 12 easing curves.

## Combining Animations

You can play multiple animations on the same display:

```java
SpawnedDisplay item = DisplayAPI.item(location)
    .item(new ItemStack(Material.NETHER_STAR))
    .spawn();

// Float + Spin simultaneously
DisplayAPI.animate(item).floating(0.3f, 40).loop(true).play();
DisplayAPI.animate(item).spin(AnimationBuilder.Axis.Y, 60).loop(true).play();
```
