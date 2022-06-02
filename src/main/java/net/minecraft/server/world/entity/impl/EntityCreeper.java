package net.minecraft.server.world.entity.impl;

import net.minecraft.server.item.Item;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.world.World;
import net.minecraft.server.world.entity.Entity;

public class EntityCreeper extends EntityMonster {

    int a;
    int b;
    int c = 30;
    int d = -1;

    public EntityCreeper(World world) {
        super(world);
        this.aF = "/mob/creeper.png";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected void c() {
        this.b = this.a;
        if (this.a > 0 && this.d < 0) {
            --this.a;
        }

        if (this.d >= 0) {
            this.d = 2;
        }

        super.c();
        if (this.d != 1) {
            this.d = -1;
        }
    }

    protected String e() {
        return "mob.creeper";
    }

    protected String f() {
        return "mob.creeperdeath";
    }

    public void die(Entity entity) {
        super.die(entity);
        if (entity instanceof EntitySkeleton) {
            this.a(Item.GOLD_RECORD.aW + this.V.nextInt(2), 1);
        }
    }

    protected void a(Entity entity, float f) {
        if (this.d <= 0 && f < 3.0F || this.d > 0 && f < 7.0F) {
            if (this.a == 0) {
                this.world.a(this, "random.fuse", 1.0F, 0.5F);
            }

            this.d = 1;
            ++this.a;
            if (this.a == this.c) {
                this.world.a(this, this.p, this.q, this.r, 3.0F);
                this.l();
            }

            this.ai = true;
        }
    }

    protected int g() {
        return Item.SULPHUR.aW;
    }
}
