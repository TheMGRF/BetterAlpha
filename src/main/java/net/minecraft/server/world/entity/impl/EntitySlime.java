package net.minecraft.server.world.entity.impl;

import net.minecraft.server.item.Item;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.World;
import net.minecraft.server.world.chunk.Chunk;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.IMonster;

public class EntitySlime extends EntityLiving implements IMonster {

    public float a;
    public float b;
    private int d = 0;
    public int c = 1;

    public EntitySlime(World world) {
        super(world);
        this.aF = "/mob/slime.png";
        this.c = 1 << this.V.nextInt(3);
        this.G = 0.0F;
        this.d = this.V.nextInt(20) + 10;
        this.c(this.c);
    }

    public void c(int i) {
        this.c = i;
        this.a(0.6F * (float) i, 0.6F * (float) i);
        this.health = i * i;
        this.a(this.p, this.q, this.r);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Size", this.c - 1);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        this.c = nbttagcompound.d("Size") + 1;
    }

    public void b_() {
        this.b = this.a;
        boolean flag = this.A;

        super.b_();
        if (this.A && !flag) {
            for (int i = 0; i < this.c * 8; ++i) {
                float f = this.V.nextFloat() * 3.1415927F * 2.0F;
                float f1 = this.V.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.a(f) * (float) this.c * 0.5F * f1;
                float f3 = MathHelper.b(f) * (float) this.c * 0.5F * f1;

                this.world.a("slime", this.p + (double) f2, this.boundingBox.b, this.r + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            if (this.c > 2) {
                this.world.a(this, "mob.slime", this.h(), ((this.V.nextFloat() - this.V.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.a = -0.5F;
        }

        this.a *= 0.6F;
    }

    protected void c() {
        EntityHuman entityhuman = this.world.a(this, 16.0D);

        if (entityhuman != null) {
            this.b(entityhuman, 10.0F);
        }

        if (this.A && this.d-- <= 0) {
            this.d = this.V.nextInt(20) + 10;
            if (entityhuman != null) {
                this.d /= 3;
            }

            this.bj = true;
            if (this.c > 1) {
                this.world.a(this, "mob.slime", this.h(), ((this.V.nextFloat() - this.V.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.a = 1.0F;
            this.bg = 1.0F - this.V.nextFloat() * 2.0F;
            this.bh = (float) (1 * this.c);
        } else {
            this.bj = false;
            if (this.A) {
                this.bg = this.bh = 0.0F;
            }
        }
    }

    public void l() {
        if (this.c > 1 && this.health == 0) {
            for (int i = 0; i < 4; ++i) {
                float f = ((float) (i % 2) - 0.5F) * (float) this.c / 4.0F;
                float f1 = ((float) (i / 2) - 0.5F) * (float) this.c / 4.0F;
                EntitySlime entityslime = new EntitySlime(this.world);

                entityslime.c(this.c / 2);
                entityslime.c(this.p + (double) f, this.q + 0.5D, this.r + (double) f1, this.V.nextFloat() * 360.0F, 0.0F);
                this.world.a((Entity) entityslime);
            }
        }

        super.l();
    }

    public void a(EntityHuman entityhuman) {
        if (this.c > 1 && this.g(entityhuman) && (double) this.a((Entity) entityhuman) < 0.6D * (double) this.c && entityhuman.a(this, this.c)) {
            this.world.a(this, "mob.slimeattack", 1.0F, (this.V.nextFloat() - this.V.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected String e() {
        return "mob.slime";
    }

    protected String f() {
        return "mob.slime";
    }

    protected int g() {
        return this.c == 1 ? Item.SLIME_BALL.aW : 0;
    }

    public boolean a() {
        Chunk chunk = this.world.b(MathHelper.b(this.p), MathHelper.b(this.q));

        return (this.c == 1 || this.world.k > 0) && this.V.nextInt(10) == 0 && chunk.a(987234911L).nextInt(10) == 0 && this.q < 16.0D;
    }

    protected float h() {
        return 0.6F;
    }
}
