package net.minecraft.server.world.block;

import net.minecraft.server.world.block.material.Material;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.block.tile.TileEntityMobSpawner;

import java.util.Random;

public class BlockMobSpawner extends BlockContainer {

    protected BlockMobSpawner(int i, int j) {
        super(i, j, Material.d);
    }

    protected TileEntity a_() {
        return new TileEntityMobSpawner();
    }

    public int a(int i, Random random) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }

    public boolean a() {
        return false;
    }
}
