package net.minecraft.server.world.entity.impl;

import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.World;
import net.minecraft.server.world.entity.Entity;

public class EntityFallingSand extends Entity {

    public int a;
    public int b = 0;

    public EntityFallingSand(World world) {
        super(world);
    }

    public EntityFallingSand(World world, float f, float f1, float f2, int i) {
        super(world);
        this.a = i;
        this.i = true;
        this.a(0.98F, 0.98F);
        this.G = this.I / 2.0F;
        this.a((double) f, (double) f1, (double) f2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.L = false;
        this.lastX = (double) f;
        this.lastY = (double) f1;
        this.lastZ = (double) f2;
    }

    public boolean c_() {
        return !this.dead;
    }

    public void b_() {
        if (this.a == 0) {
            this.die();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            ++this.b;
            this.motY -= 0.03999999910593033D;
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.9800000190734863D;
            this.motY *= 0.9800000190734863D;
            this.motZ *= 0.9800000190734863D;
            int i = MathHelper.b(this.locX);
            int j = MathHelper.b(this.locY);
            int k = MathHelper.b(this.locZ);

            if (this.world.a(i, j, k) == this.a) {
                this.world.d(i, j, k, 0);
            }

            if (this.onGround) {
                this.motX *= 0.699999988079071D;
                this.motZ *= 0.699999988079071D;
                this.motY *= -0.5D;
                this.die();
                if (!this.world.a(this.a, i, j, k, true) || !this.world.d(i, j, k, this.a)) {
                    this.a(this.a, 1);
                }
            } else if (this.b > 100) {
                this.a(this.a, 1);
                this.die();
            }
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Tile", (byte) this.a);
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.a = nbttagcompound.b("Tile") & 255;
    }
}
