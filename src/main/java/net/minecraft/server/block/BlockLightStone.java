package net.minecraft.server.block;

import net.minecraft.server.Item;
import net.minecraft.server.Material;

import java.util.Random;

public class BlockLightStone extends Block {

    public BlockLightStone(int i, int j, Material material) {
        super(i, j, material);
    }

    public int a(int i, Random random) {
        return Item.GLOWSTONE_DUST.aW;
    }
}
