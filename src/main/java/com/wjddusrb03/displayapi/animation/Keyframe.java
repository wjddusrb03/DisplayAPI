package com.wjddusrb03.displayapi.animation;

import org.joml.AxisAngle4f;
import org.joml.Vector3f;

/**
 * A single keyframe in an animation sequence.
 * Only non-null fields are applied during interpolation.
 */
public class Keyframe {

    private final int tick;
    private Vector3f translation;
    private Vector3f scale;
    private AxisAngle4f leftRotation;
    private Byte textOpacity;

    private Keyframe(int tick) {
        this.tick = tick;
    }

    public static Keyframe at(int tick) {
        return new Keyframe(tick);
    }

    public Keyframe translation(float x, float y, float z) {
        this.translation = new Vector3f(x, y, z);
        return this;
    }

    public Keyframe scale(float uniform) {
        this.scale = new Vector3f(uniform, uniform, uniform);
        return this;
    }

    public Keyframe scale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
        return this;
    }

    public Keyframe rotation(float angleDegrees, float axisX, float axisY, float axisZ) {
        this.leftRotation = new AxisAngle4f(
                (float) Math.toRadians(angleDegrees), axisX, axisY, axisZ);
        return this;
    }

    public Keyframe rotationY(float angleDegrees) {
        return rotation(angleDegrees, 0, 1, 0);
    }

    public Keyframe rotationX(float angleDegrees) {
        return rotation(angleDegrees, 1, 0, 0);
    }

    public Keyframe rotationZ(float angleDegrees) {
        return rotation(angleDegrees, 0, 0, 1);
    }

    public Keyframe opacity(int opacity) {
        this.textOpacity = (byte) Math.clamp(opacity, 0, 255);
        return this;
    }

    // Getters

    public int getTick() {
        return tick;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public AxisAngle4f getLeftRotation() {
        return leftRotation;
    }

    public Byte getTextOpacity() {
        return textOpacity;
    }

    /**
     * Interpolate between this keyframe and the next.
     */
    public static Vector3f lerpVec(Vector3f a, Vector3f b, float t) {
        if (a == null) return b;
        if (b == null) return a;
        return new Vector3f(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t
        );
    }

    public static AxisAngle4f lerpRotation(AxisAngle4f a, AxisAngle4f b, float t) {
        if (a == null) return b;
        if (b == null) return a;
        float angle = a.angle + (b.angle - a.angle) * t;
        return new AxisAngle4f(angle, b.x, b.y, b.z);
    }
}
