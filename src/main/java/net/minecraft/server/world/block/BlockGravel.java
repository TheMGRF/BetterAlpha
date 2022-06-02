package net.minecraft.server.world.block;

import net.minecraft.server.item.Item;

import java.util.Random;

public class BlockGravel extends BlockSand {

    public BlockGravel(int i, int j) {
        super(i, j);
    }

    public int a(int i, Random random) {
        return random.nextInt(10) == 0 ? Item.FLINT.aW : this.bi;
    }
}
