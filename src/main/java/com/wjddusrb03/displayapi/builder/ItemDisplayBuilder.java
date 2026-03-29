package com.wjddusrb03.displayapi.builder;

import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public class ItemDisplayBuilder extends AbstractDisplayBuilder<ItemDisplayBuilder> {

    private ItemStack itemStack;
    private ItemDisplay.ItemDisplayTransform displayTransform = ItemDisplay.ItemDisplayTransform.NONE;

    public ItemDisplayBuilder(Location location) {
        super(location);
    }

    public ItemDisplayBuilder item(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemDisplayBuilder item(Material material) {
        this.itemStack = new ItemStack(material);
        return this;
    }

    public ItemDisplayBuilder transform(ItemDisplay.ItemDisplayTransform transform) {
        this.displayTransform = transform;
        return this;
    }

    @Override
    public SpawnedDisplay spawn() {
        if (itemStack == null) throw new IllegalStateException("ItemStack not set");
        org.bukkit.World world = location.getWorld();
        if (world == null) throw new IllegalStateException("Location has no world");

        ItemDisplay display = world.spawn(location, ItemDisplay.class, id -> {
            id.setItemStack(itemStack);
            id.setItemDisplayTransform(displayTransform);
            applyCommon(id);
        });

        return register(display);
    }
}
