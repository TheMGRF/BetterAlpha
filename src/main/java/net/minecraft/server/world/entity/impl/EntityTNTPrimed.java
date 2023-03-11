package net.minecraft.server.world.entity.impl;

import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.World;
import net.minecraft.server.world.entity.Entity;

public class EntityTNTPrimed extends Entity {

    public int a;

    public EntityTNTPrimed(World world) {
        super(world);
        this.a = 0;
        this.i = true;
        this.a(0.98F, 0.98F);
        this.G = this.I / 2.0F;
    }

    public EntityTNTPrimed(World world, float f, float f1, float f2) {
        this(world);
        this.a((double) f, (double) f1, (double) f2);
        float f3 = (float) (Math.random() * 3.1415927410125732D * 2.0D);

        this.motX = (double) (-MathHelper.a(f3 * 3.1415927F / 180.0F) * 0.02F);
        this.motY = 0.20000000298023224D;
        this.motZ = (double) (-MathHelper.b(f3 * 3.1415927F / 180.0F) * 0.02F);
        this.L = false;
        this.a = 80;
        this.lastX = (double) f;
        this.lastY = (double) f1;
        this.lastZ = (double) f2;
    }

    public boolean c_() {
        return !this.dead;
    }

    public void b_() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.03999999910593033D;
        this.c(this.motX, this.motY, this.motZ);
        this.motX *= 0.9800000190734863D;
        this.motY *= 0.9800000190734863D;
        this.motZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motX *= 0.699999988079071D;
            this.motZ *= 0.699999988079071D;
            this.motY *= -0.5D;
        }

        if (this.a-- <= 0) {
            this.die();
            this.c();
        } else {
            this.world.a("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void c() {
        float f = 4.0F;

        this.world.a((Entity) null, this.locX, this.locY, this.locZ, f);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Fuse", (byte) this.a);
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.a = nbttagcompound.b("Fuse");
    }
}
