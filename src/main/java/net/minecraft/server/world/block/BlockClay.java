package net.minecraft.server.world.block;

import net.minecraft.server.item.Item;
import net.minecraft.server.world.block.material.Material;

import java.util.Random;

public class BlockClay extends Block {

    public BlockClay(int i, int j) {
        super(i, j, Material.v);
    }

    public int a(int i, Random random) {
        return Item.CLAY_BALL.aW;
    }

    public int a(Random random) {
        return 4;
    }
}
