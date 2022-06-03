package net.minecraft.server.world.entity.impl;

import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.World;
import net.minecraft.server.world.block.EnumSkyBlock;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.IMonster;

public class EntityMonster extends EntityCreature implements IMonster {

    protected int e = 2;

    public EntityMonster(World world) {
        super(world);
        this.health = 20;
    }

    public void D() {
        float f = this.b(1.0F);

        if (f > 0.5F) {
            this.bf += 2;
        }

        super.D();
    }

    public void b_() {
        super.b_();
        if (this.world.k == 0) {
            this.l();
        }
    }

    protected Entity k() {
        EntityHuman entityhuman = this.world.a(this, 16.0D);

        return entityhuman != null && this.g(entityhuman) ? entityhuman : null;
    }

    public boolean a(Entity entity, int i) {
        if (super.a(entity, i)) {
            if (this.j != entity && this.k != entity) {
                if (entity != this) {
                    this.f = entity;
                }

                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected void a(Entity entity, float f) {
        if ((double) f < 2.5D && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTime = 20;
            entity.a(this, this.e);
        }
    }

    protected float a(int i, int j, int k) {
        return 0.5F - this.world.j(i, j, k);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public boolean a() {
        int i = MathHelper.b(this.p);
        int j = MathHelper.b(this.boundingBox.b);
        int k = MathHelper.b(this.r);

        if (this.world.a(EnumSkyBlock.SKY, i, j, k) > this.V.nextInt(32)) {
            return false;
        } else {
            int l = this.world.h(i, j, k);

            return l <= this.V.nextInt(8) && super.a();
        }
    }
}
