package net.minecraft.server.world.block;

import net.minecraft.server.world.block.material.Material;

public class BlockLeavesBase extends net.minecraft.server.world.block.Block {

    protected boolean a;

    protected BlockLeavesBase(int i, int j, Material material, boolean flag) {
        super(i, j, material);
        this.a = flag;
    }

    public boolean a() {
        return false;
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.a(i, j, k);

        return !this.a && i1 == this.bi ? false : super.a(iblockaccess, i, j, k, l);
    }
}
