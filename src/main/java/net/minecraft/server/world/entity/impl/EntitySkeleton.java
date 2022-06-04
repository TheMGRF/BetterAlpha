package net.minecraft.server.world.entity.impl;

import net.minecraft.server.item.Item;
import net.minecraft.server.item.ItemStack;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.World;
import net.minecraft.server.world.entity.Entity;

public class EntitySkeleton extends EntityMonster {

    private static final ItemStack a = new ItemStack(Item.BOW, 1);

    public EntitySkeleton(World world) {
        super(world);
        this.aF = "/mob/skeleton.png";
    }

    protected String d() {
        return "mob.skeleton";
    }

    protected String e() {
        return "mob.skeletonhurt";
    }

    protected String f() {
        return "mob.skeletonhurt";
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

    protected void a(Entity entity, float f) {
        if (f < 10.0F) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;

            if (this.attackTime == 0) {
                EntityArrow entityarrow = new EntityArrow(this.world, this);

                ++entityarrow.locY;
                double d2 = entity.locY - 0.20000000298023224D - entityarrow.locY;
                float f1 = MathHelper.a(d0 * d0 + d1 * d1) * 0.2F;

                this.world.a(this, "random.bow", 1.0F, 1.0F / (this.V.nextFloat() * 0.4F + 0.8F));
                this.world.trackEntity((Entity) entityarrow);
                entityarrow.a(d0, d2 + (double) f1, d1, 0.6F, 12.0F);
                this.attackTime = 30;
            }

            this.yaw = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            this.ai = true;
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected int g() {
        return Item.ARROW.aW;
    }
}
