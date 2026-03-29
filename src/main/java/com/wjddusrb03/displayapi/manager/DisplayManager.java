package com.wjddusrb03.displayapi.manager;

import com.wjddusrb03.displayapi.display.DisplayGroup;
import com.wjddusrb03.displayapi.display.InteractiveDisplay;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks all active displays, groups, and interactive displays.
 */
public class DisplayManager {

    private final Map<String, SpawnedDisplay> displays = new ConcurrentHashMap<>();
    private final Map<String, DisplayGroup> groups = new ConcurrentHashMap<>();
    private final Map<UUID, InteractiveDisplay> interactives = new ConcurrentHashMap<>();

    public void register(SpawnedDisplay display) {
        displays.put(display.getId(), display);
    }

    public void registerGroup(DisplayGroup group) {
        groups.put(group.getId(), group);
    }

    public SpawnedDisplay getById(String id) {
        return displays.get(id);
    }

    public DisplayGroup getGroupById(String id) {
        return groups.get(id);
    }

    public void remove(String id) {
        SpawnedDisplay display = displays.remove(id);
        if (display != null) {
            display.remove();
        }
    }

    public void removeGroup(String id) {
        DisplayGroup group = groups.remove(id);
        if (group != null) {
            group.remove();
        }
    }

    public void registerInteractive(InteractiveDisplay interactive) {
        UUID uuid = interactive.getInteractionUUID();
        if (uuid != null) {
            interactives.put(uuid, interactive);
        }
    }

    public InteractiveDisplay getInteractiveByEntityUUID(UUID entityUUID) {
        return interactives.get(entityUUID);
    }

    public void removeAll() {
        for (SpawnedDisplay display : displays.values()) {
            display.remove();
        }
        displays.clear();

        for (DisplayGroup group : groups.values()) {
            group.remove();
        }
        groups.clear();

        for (InteractiveDisplay interactive : interactives.values()) {
            interactive.remove();
        }
        interactives.clear();
    }

    public Collection<SpawnedDisplay> getAllDisplays() {
        return Collections.unmodifiableCollection(displays.values());
    }

    public Collection<DisplayGroup> getAllGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    public int getDisplayCount() {
        return displays.size();
    }

    public int getGroupCount() {
        return groups.size();
    }

    /**
     * Remove dead displays from tracking.
     */
    public void cleanup() {
        displays.entrySet().removeIf(e -> !e.getValue().isAlive());
        groups.entrySet().removeIf(e -> {
            DisplayGroup g = e.getValue();
            g.cleanup();
            return !g.isAlive();
        });
        interactives.entrySet().removeIf(e -> !e.getValue().isAlive());
    }

    /**
     * Get all persistent displays for saving.
     */
    public List<SpawnedDisplay> getPersistentDisplays() {
        List<SpawnedDisplay> result = new ArrayList<>();
        for (SpawnedDisplay sd : displays.values()) {
            if (sd.isPersistent() && sd.isAlive()) {
                result.add(sd);
            }
        }
        return result;
    }
}
