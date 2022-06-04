package net.minecraft.server.world.entity.impl;

import net.minecraft.server.item.Item;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.World;

public class EntityZombie extends EntityMonster {

    public EntityZombie(World world) {
        super(world);
        this.aF = "/mob/zombie.png";
        this.bl = 0.5F;
        this.e = 5;
    }

    public void D() {
        if (this.world.b()) {
            float f = this.b(1.0F);

            if (f > 0.5F && this.world.g(MathHelper.b(this.locX), MathHelper.b(this.locY), MathHelper.b(this.locZ)) && this.V.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.fire = 300;
            }
        }

        super.D();
    }

    protected String d() {
        return "mob.zombie";
    }

    protected String e() {
        return "mob.zombiehurt";
    }

    protected String f() {
        return "mob.zombiedeath";
    }

    protected int g() {
        return Item.FEATHER.aW;
    }
}
