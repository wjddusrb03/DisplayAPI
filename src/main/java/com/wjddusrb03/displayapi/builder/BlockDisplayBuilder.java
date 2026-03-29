package com.wjddusrb03.displayapi.builder;

import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;

public class BlockDisplayBuilder extends AbstractDisplayBuilder<BlockDisplayBuilder> {

    private BlockData blockData;

    public BlockDisplayBuilder(Location location) {
        super(location);
    }

    public BlockDisplayBuilder block(Material material) {
        this.blockData = material.createBlockData();
        return this;
    }

    public BlockDisplayBuilder block(BlockData blockData) {
        this.blockData = blockData;
        return this;
    }

    public BlockDisplayBuilder block(String blockDataString) {
        this.blockData = Bukkit.createBlockData(blockDataString);
        return this;
    }

    @Override
    public SpawnedDisplay spawn() {
        if (blockData == null) throw new IllegalStateException("Block data not set");
        org.bukkit.World world = location.getWorld();
        if (world == null) throw new IllegalStateException("Location has no world");

        BlockDisplay display = world.spawn(location, BlockDisplay.class, bd -> {
            bd.setBlock(blockData);
            applyCommon(bd);
        });

        return register(display);
    }
}
