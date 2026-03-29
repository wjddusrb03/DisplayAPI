# Easing Functions

DisplayAPI provides 12 easing functions that control the acceleration and deceleration of animations.

## Available Easings

| Easing | Description | Best For |
|--------|-------------|----------|
| `LINEAR` | Constant speed | Spin, continuous motion |
| `EASE_IN` | Starts slow, ends fast | Exit animations |
| `EASE_OUT` | Starts fast, ends slow | Entrance animations |
| `EASE_IN_OUT` | Slow start and end | Pulse, floating |
| `CUBIC_IN` | Stronger ease-in | Dramatic starts |
| `CUBIC_OUT` | Stronger ease-out | Dramatic stops |
| `CUBIC_IN_OUT` | Stronger both ends | Smooth transitions |
| `QUART_IN` | Very strong ease-in | Heavy objects |
| `QUART_OUT` | Very strong ease-out | Snappy stops |
| `QUART_IN_OUT` | Very strong both | Dramatic motion |
| `EASE_IN_BACK` | Pulls back then accelerates | Wind-up effect |
| `EASE_OUT_BACK` | Overshoots then settles | Bouncy arrival |
| `BOUNCE` | Bounces at the end | Drop + bounce |
| `ELASTIC` | Elastic overshoot | Spring-like motion |

## Usage

```java
DisplayAPI.animate(display)
    .keyframe(Keyframe.at(0).scale(0f))
    .keyframe(Keyframe.at(20).scale(1f))
    .easing(Easing.EASE_OUT_BACK)    // Overshoot then settle
    .play();
```

## Visual Reference

```
LINEAR:        ─────────────── (constant)
EASE_IN:       ........──────── (slow → fast)
EASE_OUT:      ────────........ (fast → slow)
EASE_IN_OUT:   ....────────.... (slow → fast → slow)
BOUNCE:        ────╮ ╭╮╭─      (bounces at end)
ELASTIC:       ────~≈~─        (springs back and forth)
EASE_OUT_BACK: ──────╮╭─       (overshoots then returns)
```

## Pre-built Animation Defaults

| Animation | Default Easing |
|-----------|---------------|
| `pulse()` | EASE_IN_OUT |
| `spin()` | LINEAR |
| `bounce()` | BOUNCE |
| `floating()` | EASE_IN_OUT |
| `fadeIn()` | EASE_OUT |
| `fadeOut()` | EASE_IN |
| `growIn()` | EASE_OUT_BACK |
| `shake()` | LINEAR |
