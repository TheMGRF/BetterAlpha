package net.minecraft.server.world.block;

import net.minecraft.server.world.block.material.Material;

import java.util.Random;

public class BlockStone extends Block {

    public BlockStone(int i, int j) {
        super(i, j, Material.d);
    }

    public int a(int i, Random random) {
        return Block.COBBLESTONE.bi;
    }
}
