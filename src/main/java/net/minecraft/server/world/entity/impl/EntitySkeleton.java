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

            if (f > 0.5F && this.world.g(MathHelper.b(this.p), MathHelper.b(this.q), MathHelper.b(this.r)) && this.V.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.Y = 300;
            }
        }

        super.D();
    }

    protected void a(Entity entity, float f) {
        if (f < 10.0F) {
            double d0 = entity.p - this.p;
            double d1 = entity.r - this.r;

            if (this.attackTime == 0) {
                EntityArrow entityarrow = new EntityArrow(this.world, this);

                ++entityarrow.q;
                double d2 = entity.q - 0.20000000298023224D - entityarrow.q;
                float f1 = MathHelper.a(d0 * d0 + d1 * d1) * 0.2F;

                this.world.a(this, "random.bow", 1.0F, 1.0F / (this.V.nextFloat() * 0.4F + 0.8F));
                this.world.a((Entity) entityarrow);
                entityarrow.a(d0, d2 + (double) f1, d1, 0.6F, 12.0F);
                this.attackTime = 30;
            }

            this.v = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
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
