package net.minecraft.server.world.block;

import net.minecraft.server.item.Item;
import net.minecraft.server.world.block.material.Material;

import java.util.Random;

public class BlockLightStone extends Block {

    public BlockLightStone(int i, int j, Material material) {
        super(i, j, material);
    }

    public int a(int i, Random random) {
        return Item.GLOWSTONE_DUST.aW;
    }
}
