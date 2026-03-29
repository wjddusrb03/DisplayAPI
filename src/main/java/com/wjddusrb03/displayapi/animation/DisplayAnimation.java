package com.wjddusrb03.displayapi.animation;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.List;

/**
 * Active animation controller that runs keyframe sequences on a Display entity.
 */
public class DisplayAnimation extends BukkitRunnable {

    private final SpawnedDisplay target;
    private final List<Keyframe> keyframes;
    private final Easing easing;
    private final boolean loop;
    private final int totalDuration;

    private int currentTick = 0;
    private boolean running = false;

    public DisplayAnimation(SpawnedDisplay target, List<Keyframe> keyframes, Easing easing, boolean loop) {
        this.target = target;
        this.keyframes = keyframes;
        this.easing = easing;
        this.loop = loop;
        this.totalDuration = keyframes.isEmpty() ? 0 : keyframes.get(keyframes.size() - 1).getTick();
    }

    public void play() {
        if (running || totalDuration <= 0) return;
        running = true;
        currentTick = 0;
        this.runTaskTimer(DisplayAPI.getPlugin(), 0L, 1L);
    }

    public void stop() {
        running = false;
        try {
            cancel();
        } catch (IllegalStateException ignored) {}
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        if (!target.isAlive()) {
            stop();
            return;
        }

        applyFrame(currentTick);
        currentTick++;

        if (currentTick > totalDuration) {
            if (loop) {
                currentTick = 0;
            } else {
                stop();
            }
        }
    }

    private void applyFrame(int tick) {
        // Find surrounding keyframes
        Keyframe prev = null;
        Keyframe next = null;

        for (int i = 0; i < keyframes.size(); i++) {
            if (keyframes.get(i).getTick() >= tick) {
                next = keyframes.get(i);
                prev = i > 0 ? keyframes.get(i - 1) : next;
                break;
            }
        }

        if (prev == null || next == null) return;

        // Calculate local progress between prev and next keyframe
        float localProgress;
        int segmentLength = next.getTick() - prev.getTick();
        if (segmentLength <= 0) {
            localProgress = 1f;
        } else {
            localProgress = (float) (tick - prev.getTick()) / segmentLength;
        }

        float easedProgress = easing.apply(localProgress);

        // Interpolate transform properties
        Vector3f translation = Keyframe.lerpVec(
                prev.getTranslation(), next.getTranslation(), easedProgress);
        Vector3f scale = Keyframe.lerpVec(
                prev.getScale(), next.getScale(), easedProgress);
        AxisAngle4f rotation = Keyframe.lerpRotation(
                prev.getLeftRotation(), next.getLeftRotation(), easedProgress);

        // Build transformation
        Vector3f t = translation != null ? translation : new Vector3f(0, 0, 0);
        Vector3f s = scale != null ? scale : new Vector3f(1, 1, 1);
        AxisAngle4f r = rotation != null ? rotation : new AxisAngle4f(0, 0, 1, 0);
        AxisAngle4f noRot = new AxisAngle4f(0, 0, 1, 0);

        Display entity = target.getEntity();
        entity.setInterpolationDelay(0);
        entity.setInterpolationDuration(2);
        entity.setTransformation(new Transformation(t, r, s, noRot));

        // Text opacity
        if (entity instanceof TextDisplay td) {
            Byte prevOpacity = prev.getTextOpacity();
            Byte nextOpacity = next.getTextOpacity();
            if (prevOpacity != null && nextOpacity != null) {
                int o = (int) (Byte.toUnsignedInt(prevOpacity)
                        + (Byte.toUnsignedInt(nextOpacity) - Byte.toUnsignedInt(prevOpacity)) * easedProgress);
                td.setTextOpacity((byte) o);
            } else if (nextOpacity != null) {
                td.setTextOpacity(nextOpacity);
            }
        }
    }
}
