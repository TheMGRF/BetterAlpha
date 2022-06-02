package net.minecraft.server;

import net.minecraft.server.block.Block;

public class ItemAxe extends ItemTool {

    private static Block[] bb = new Block[] { Block.WOOD, Block.BOOKSHELF, Block.LOG, Block.CHEST};

    public ItemAxe(int i, int j) {
        super(i, 3, j, bb);
    }
}
