package net.minecraft.server.item;

import net.minecraft.server.world.World;
import net.minecraft.server.world.block.Block;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.impl.EntityHuman;
import net.minecraft.server.world.entity.impl.EntityMinecart;

public class ItemMinecart extends Item {

    public int a;

    public ItemMinecart(int i, int j) {
        super(i);
        this.aX = 1;
        this.a = j;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int i1 = world.a(i, j, k);

        if (i1 == Block.RAILS.bi) {
            world.trackEntity((Entity) (new EntityMinecart(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.a)));
            --itemstack.a;
            return true;
        } else {
            return false;
        }
    }
}
