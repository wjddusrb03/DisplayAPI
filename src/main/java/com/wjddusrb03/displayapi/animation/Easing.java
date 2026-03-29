package com.wjddusrb03.displayapi.animation;

import java.util.function.Function;

/**
 * Easing functions for smooth animation transitions.
 * Each function takes a progress value (0.0 ~ 1.0) and returns an eased value (0.0 ~ 1.0).
 */
public enum Easing {

    LINEAR(t -> t),

    EASE_IN(t -> t * t),
    EASE_OUT(t -> t * (2 - t)),
    EASE_IN_OUT(t -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t),

    EASE_IN_CUBIC(t -> t * t * t),
    EASE_OUT_CUBIC(t -> {
        float f = t - 1;
        return f * f * f + 1;
    }),
    EASE_IN_OUT_CUBIC(t -> t < 0.5f ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1),

    EASE_IN_QUART(t -> t * t * t * t),
    EASE_OUT_QUART(t -> {
        float f = t - 1;
        return 1 - f * f * f * f;
    }),

    EASE_IN_BACK(t -> t * t * (2.70158f * t - 1.70158f)),
    EASE_OUT_BACK(t -> {
        float f = t - 1;
        return 1 + f * f * (2.70158f * f + 1.70158f);
    }),

    BOUNCE(t -> {
        float out;
        if (t < 1 / 2.75f) {
            out = 7.5625f * t * t;
        } else if (t < 2 / 2.75f) {
            float f = t - 1.5f / 2.75f;
            out = 7.5625f * f * f + 0.75f;
        } else if (t < 2.5f / 2.75f) {
            float f = t - 2.25f / 2.75f;
            out = 7.5625f * f * f + 0.9375f;
        } else {
            float f = t - 2.625f / 2.75f;
            out = 7.5625f * f * f + 0.984375f;
        }
        return out;
    }),

    ELASTIC(t -> {
        if (t == 0 || t == 1) return t;
        return (float) (-Math.pow(2, 10 * (t - 1)) * Math.sin((t - 1.1f) * 5 * Math.PI));
    });

    private final Function<Float, Float> function;

    Easing(Function<Float, Float> function) {
        this.function = function;
    }

    /**
     * Apply easing to a progress value.
     *
     * @param t progress from 0.0 to 1.0
     * @return eased value
     */
    public float apply(float t) {
        return function.apply(Math.clamp(t, 0f, 1f));
    }

    /**
     * Interpolate between two values using this easing.
     */
    public float interpolate(float start, float end, float t) {
        return start + (end - start) * apply(t);
    }
}
