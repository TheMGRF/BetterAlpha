package net.minecraft.server.world.block;

import net.minecraft.server.item.Item;
import net.minecraft.server.world.block.material.Material;

import java.util.Random;

public class BlockOre extends Block {

    public BlockOre(int i, int j) {
        super(i, j, Material.d);
    }

    public int a(int i, Random random) {
        return this.bi == Block.COAL_ORE.bi ? Item.COAL.aW : (this.bi == Block.DIAMOND_ORE.bi ? Item.DIAMOND.aW : this.bi);
    }

    public int a(Random random) {
        return 1;
    }
}
