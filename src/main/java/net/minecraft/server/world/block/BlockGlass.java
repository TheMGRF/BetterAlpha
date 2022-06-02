package net.minecraft.server.world.block;

import net.minecraft.server.world.block.material.Material;

import java.util.Random;

public class BlockGlass extends BlockBreakable {

    public BlockGlass(int i, int j, Material material, boolean flag) {
        super(i, j, material, flag);
    }

    public int a(Random random) {
        return 0;
    }
}
