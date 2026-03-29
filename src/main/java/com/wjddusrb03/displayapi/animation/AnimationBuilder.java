package com.wjddusrb03.displayapi.animation;

import com.wjddusrb03.displayapi.display.SpawnedDisplay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Builder for creating Display entity animations.
 *
 * <p>Usage:
 * <pre>
 * // Custom keyframe animation
 * DisplayAPI.animate(display)
 *     .keyframe(Keyframe.at(0).scale(1f))
 *     .keyframe(Keyframe.at(10).scale(1.5f).translation(0, 0.5f, 0))
 *     .keyframe(Keyframe.at(20).scale(1f))
 *     .easing(Easing.EASE_IN_OUT)
 *     .loop(true)
 *     .play();
 *
 * // Pre-built: pulse
 * DisplayAPI.animate(display).pulse(1.0f, 1.3f, 20).loop(true).play();
 *
 * // Pre-built: spin
 * DisplayAPI.animate(display).spin(Axis.Y, 40).loop(true).play();
 *
 * // Pre-built: bounce
 * DisplayAPI.animate(display).bounce(0.3f, 20).loop(true).play();
 *
 * // Pre-built: fade in
 * DisplayAPI.animate(display).fadeIn(20).play();
 * </pre>
 */
public class AnimationBuilder {

    public enum Axis { X, Y, Z }

    private final SpawnedDisplay target;
    private final List<Keyframe> keyframes = new ArrayList<>();
    private Easing easing = Easing.LINEAR;
    private boolean loop = false;

    public AnimationBuilder(SpawnedDisplay target) {
        this.target = target;
    }

    // ========================
    // Low-level keyframe API
    // ========================

    public AnimationBuilder keyframe(Keyframe keyframe) {
        keyframes.add(keyframe);
        return this;
    }

    public AnimationBuilder easing(Easing easing) {
        this.easing = easing;
        return this;
    }

    public AnimationBuilder loop(boolean loop) {
        this.loop = loop;
        return this;
    }

    // ========================
    // Pre-built animations
    // ========================

    /**
     * Pulse animation: scales between minScale and maxScale.
     *
     * @param minScale minimum scale
     * @param maxScale maximum scale
     * @param durationTicks total cycle duration in ticks
     */
    public AnimationBuilder pulse(float minScale, float maxScale, int durationTicks) {
        keyframes.clear();
        int half = durationTicks / 2;
        keyframes.add(Keyframe.at(0).scale(minScale));
        keyframes.add(Keyframe.at(half).scale(maxScale));
        keyframes.add(Keyframe.at(durationTicks).scale(minScale));
        this.easing = Easing.EASE_IN_OUT;
        return this;
    }

    /**
     * Spin animation: rotates 360 degrees on the given axis.
     *
     * @param axis rotation axis
     * @param durationTicks time for one full rotation
     */
    public AnimationBuilder spin(Axis axis, int durationTicks) {
        keyframes.clear();
        int steps = 4;
        int ticksPerStep = durationTicks / steps;
        for (int i = 0; i <= steps; i++) {
            float angle = (360f / steps) * i;
            Keyframe kf = Keyframe.at(ticksPerStep * i);
            switch (axis) {
                case X -> kf.rotationX(angle);
                case Y -> kf.rotationY(angle);
                case Z -> kf.rotationZ(angle);
            }
            keyframes.add(kf);
        }
        this.easing = Easing.LINEAR;
        return this;
    }

    /**
     * Bounce animation: moves up and drops back.
     *
     * @param height bounce height in blocks
     * @param durationTicks total cycle duration
     */
    public AnimationBuilder bounce(float height, int durationTicks) {
        keyframes.clear();
        int half = durationTicks / 2;
        keyframes.add(Keyframe.at(0).translation(0, 0, 0));
        keyframes.add(Keyframe.at(half).translation(0, height, 0));
        keyframes.add(Keyframe.at(durationTicks).translation(0, 0, 0));
        this.easing = Easing.BOUNCE;
        return this;
    }

    /**
     * Float animation: gently bobs up and down.
     *
     * @param amplitude bob height
     * @param durationTicks cycle duration
     */
    public AnimationBuilder floating(float amplitude, int durationTicks) {
        keyframes.clear();
        int quarter = durationTicks / 4;
        keyframes.add(Keyframe.at(0).translation(0, 0, 0));
        keyframes.add(Keyframe.at(quarter).translation(0, amplitude, 0));
        keyframes.add(Keyframe.at(quarter * 2).translation(0, 0, 0));
        keyframes.add(Keyframe.at(quarter * 3).translation(0, -amplitude, 0));
        keyframes.add(Keyframe.at(durationTicks).translation(0, 0, 0));
        this.easing = Easing.EASE_IN_OUT;
        return this;
    }

    /**
     * Fade in animation (TextDisplay only).
     *
     * @param durationTicks fade duration
     */
    public AnimationBuilder fadeIn(int durationTicks) {
        keyframes.clear();
        keyframes.add(Keyframe.at(0).opacity(0).scale(0.8f));
        keyframes.add(Keyframe.at(durationTicks).opacity(255).scale(1.0f));
        this.easing = Easing.EASE_OUT;
        return this;
    }

    /**
     * Fade out animation (TextDisplay only).
     *
     * @param durationTicks fade duration
     */
    public AnimationBuilder fadeOut(int durationTicks) {
        keyframes.clear();
        keyframes.add(Keyframe.at(0).opacity(255).scale(1.0f));
        keyframes.add(Keyframe.at(durationTicks).opacity(0).scale(0.8f));
        this.easing = Easing.EASE_IN;
        return this;
    }

    /**
     * Grow-in animation: scales from 0 to target size.
     *
     * @param targetScale final scale
     * @param durationTicks animation duration
     */
    public AnimationBuilder growIn(float targetScale, int durationTicks) {
        keyframes.clear();
        keyframes.add(Keyframe.at(0).scale(0f));
        keyframes.add(Keyframe.at(durationTicks).scale(targetScale));
        this.easing = Easing.EASE_OUT_BACK;
        return this;
    }

    /**
     * Shake animation: rapid horizontal oscillation.
     *
     * @param intensity shake distance
     * @param durationTicks total duration
     */
    public AnimationBuilder shake(float intensity, int durationTicks) {
        keyframes.clear();
        int segments = 8;
        int ticksPerSeg = durationTicks / segments;
        for (int i = 0; i <= segments; i++) {
            float offset = (i % 2 == 0) ? 0 : ((i % 4 == 1) ? intensity : -intensity);
            float decay = 1f - ((float) i / segments);
            keyframes.add(Keyframe.at(ticksPerSeg * i).translation(offset * decay, 0, 0));
        }
        this.easing = Easing.LINEAR;
        return this;
    }

    // ========================
    // Build and play
    // ========================

    /**
     * Start the animation.
     *
     * @return the animation controller (can be stopped later)
     */
    public DisplayAnimation play() {
        if (keyframes.isEmpty()) {
            throw new IllegalStateException("No keyframes defined");
        }
        keyframes.sort(Comparator.comparingInt(Keyframe::getTick));
        DisplayAnimation animation = new DisplayAnimation(target, new ArrayList<>(keyframes), easing, loop);
        animation.play();
        return animation;
    }
}
