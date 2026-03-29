package com.wjddusrb03.displayapi.manager;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Saves and loads persistent displays to/from YAML.
 */
public class PersistenceManager {

    private final JavaPlugin plugin;
    private final File dataFile;

    public PersistenceManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "displays.yml");
    }

    public void save() {
        FileConfiguration config = new YamlConfiguration();
        DisplayManager manager = DisplayAPI.getManager();

        for (SpawnedDisplay sd : manager.getPersistentDisplays()) {
            Display entity = sd.getEntity();
            String id = sd.getId();
            Location loc = entity.getLocation();

            config.set(id + ".world", loc.getWorld().getName());
            config.set(id + ".x", loc.getX());
            config.set(id + ".y", loc.getY());
            config.set(id + ".z", loc.getZ());
            config.set(id + ".yaw", loc.getYaw());
            config.set(id + ".pitch", loc.getPitch());
            config.set(id + ".billboard", entity.getBillboard().name());
            config.set(id + ".view-range", entity.getViewRange());

            if (entity.isGlowing()) {
                config.set(id + ".glowing", true);
                if (entity.getGlowColorOverride() != null) {
                    config.set(id + ".glow-color", entity.getGlowColorOverride().asRGB());
                }
            }

            if (entity instanceof TextDisplay td) {
                config.set(id + ".type", "TEXT");
                Component text = td.text();
                if (text != null) {
                    config.set(id + ".text", GsonComponentSerializer.gson().serialize(text));
                }
                config.set(id + ".shadowed", td.isShadowed());
                config.set(id + ".see-through", td.isSeeThrough());
                config.set(id + ".alignment", td.getAlignment().name());
                if (td.getBackgroundColor() != null) {
                    config.set(id + ".background-color", td.getBackgroundColor().asARGB());
                }
            } else if (entity instanceof BlockDisplay bd) {
                config.set(id + ".type", "BLOCK");
                config.set(id + ".block-data", bd.getBlock().getAsString());
            } else if (entity instanceof ItemDisplay itemDisplay) {
                config.set(id + ".type", "ITEM");
                if (itemDisplay.getItemStack() != null) {
                    config.set(id + ".material", itemDisplay.getItemStack().getType().name());
                }
                config.set(id + ".item-transform", itemDisplay.getItemDisplayTransform().name());
            }
        }

        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save displays.yml", e);
        }
    }

    public void load() {
        if (!dataFile.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

        for (String id : config.getKeys(false)) {
            try {
                loadDisplay(config, id);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load display '" + id + "': " + e.getMessage());
            }
        }
    }

    private void loadDisplay(FileConfiguration config, String id) {
        String worldName = config.getString(id + ".world");
        if (worldName == null) return;
        var world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("World '" + worldName + "' not found for display '" + id + "'");
            return;
        }

        double x = config.getDouble(id + ".x");
        double y = config.getDouble(id + ".y");
        double z = config.getDouble(id + ".z");
        float yaw = (float) config.getDouble(id + ".yaw");
        float pitch = (float) config.getDouble(id + ".pitch");
        Location loc = new Location(world, x, y, z, yaw, pitch);

        String type = config.getString(id + ".type", "TEXT");
        String billboardStr = config.getString(id + ".billboard", "CENTER");
        Display.Billboard billboard = Display.Billboard.valueOf(billboardStr);
        float viewRange = (float) config.getDouble(id + ".view-range", 1.0);

        switch (type) {
            case "TEXT" -> {
                String textJson = config.getString(id + ".text", "");
                Component text;
                try {
                    text = GsonComponentSerializer.gson().deserialize(textJson);
                } catch (Exception e) {
                    text = Component.text(textJson);
                }
                Component finalText = text;
                boolean shadowed = config.getBoolean(id + ".shadowed", false);
                boolean seeThrough = config.getBoolean(id + ".see-through", false);
                String alignStr = config.getString(id + ".alignment", "CENTER");
                TextDisplay.TextAlignment align;
                try {
                    align = TextDisplay.TextAlignment.valueOf(alignStr);
                } catch (IllegalArgumentException e) {
                    align = TextDisplay.TextAlignment.CENTER;
                }

                var textBuilder = DisplayAPI.text(loc)
                        .text(finalText)
                        .billboard(billboard)
                        .viewRange(viewRange)
                        .shadowed(shadowed)
                        .seeThrough(seeThrough)
                        .alignment(align)
                        .persistent(true)
                        .id(id);

                if (config.contains(id + ".background-color")) {
                    int argb = config.getInt(id + ".background-color");
                    textBuilder.background(Color.fromARGB(argb));
                }

                textBuilder.spawn();
            }
            case "BLOCK" -> {
                String blockDataStr = config.getString(id + ".block-data", "minecraft:stone");
                DisplayAPI.block(loc)
                        .block(blockDataStr)
                        .billboard(billboard)
                        .viewRange(viewRange)
                        .persistent(true)
                        .id(id)
                        .spawn();
            }
            case "ITEM" -> {
                String materialStr = config.getString(id + ".material", "STONE");
                Material mat;
                try {
                    mat = Material.valueOf(materialStr);
                } catch (IllegalArgumentException e) {
                    mat = Material.STONE;
                    plugin.getLogger().warning("Unknown material '" + materialStr + "' for display '" + id + "', using STONE");
                }
                DisplayAPI.item(loc)
                        .item(mat)
                        .billboard(billboard)
                        .viewRange(viewRange)
                        .persistent(true)
                        .id(id)
                        .spawn();
            }
        }
    }
}
