package net.minecraft.server.world.entity.impl;

import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.AxisAlignedBB;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.utils.MovingObjectPosition;
import net.minecraft.server.utils.Vec3D;
import net.minecraft.server.world.Explosion;
import net.minecraft.server.world.World;
import net.minecraft.server.world.entity.Entity;

import java.util.List;

public class EntityFireball extends Entity {

    private int e = -1;
    private int f = -1;
    private int ai = -1;
    private int aj = 0;
    private boolean ak = false;
    public int a = 0;
    private EntityLiving al;
    private int am;
    private int an = 0;
    public double b;
    public double c;
    public double d;

    public EntityFireball(World world) {
        super(world);
        this.a(1.0F, 1.0F);
    }

    public EntityFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world);
        this.al = entityliving;
        this.a(1.0F, 1.0F);
        this.c(entityliving.locX, entityliving.locY, entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.a(this.locX, this.locY, this.locZ);
        this.G = 0.0F;
        this.motX = this.motY = this.motZ = 0.0D;
        d0 += this.V.nextGaussian() * 0.4D;
        d1 += this.V.nextGaussian() * 0.4D;
        d2 += this.V.nextGaussian() * 0.4D;
        double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

        this.b = d0 / d3 * 0.1D;
        this.c = d1 / d3 * 0.1D;
        this.d = d2 / d3 * 0.1D;
    }

    public void b_() {
        super.b_();
        this.fire = 10;
        if (this.a > 0) {
            --this.a;
        }

        if (this.ak) {
            int i = this.world.a(this.e, this.f, this.ai);

            if (i == this.aj) {
                ++this.am;
                if (this.am == 1200) {
                    this.l();
                }

                return;
            }

            this.ak = false;
            this.motX *= (double) (this.V.nextFloat() * 0.2F);
            this.motY *= (double) (this.V.nextFloat() * 0.2F);
            this.motZ *= (double) (this.V.nextFloat() * 0.2F);
            this.am = 0;
            this.an = 0;
        } else {
            ++this.an;
        }

        Vec3D vec3d = Vec3D.b(this.locX, this.locY, this.locZ);
        Vec3D vec3d1 = Vec3D.b(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

        vec3d = Vec3D.b(this.locX, this.locY, this.locZ);
        vec3d1 = Vec3D.b(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.b(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }

        Entity entity = null;
        List list = this.world.b((Entity) this, this.boundingBox.a(this.motX, this.motY, this.motZ).b(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;

        float f;

        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity) list.get(j);

            if (entity1.c_() && (entity1 != this.al || this.an >= 25)) {
                f = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.b((double) f, (double) f, (double) f);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                if (movingobjectposition1 != null) {
                    double d1 = vec3d.a(movingobjectposition1.f);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.g != null && movingobjectposition.g.hurt(this.al, 0)) {
                ;
            }

            Explosion explosion = new Explosion();

            explosion.a = true;
            explosion.a(this.world, this, this.locX, this.locY, this.locZ, 1.0F);
            this.l();
        }

        this.locX += this.motX;
        this.locY += this.motY;
        this.locZ += this.motZ;
        float f1 = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);

        this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

        for (this.pitch = (float) (Math.atan2(this.motY, (double) f1) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
            ;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
        this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
        float f2 = 0.95F;

        f = 0.0060F;
        if (this.r()) {
            for (int k = 0; k < 4; ++k) {
                float f3 = 0.25F;

                this.world.a("bubble", this.locX - this.motX * (double) f3, this.locY - this.motY * (double) f3, this.locZ - this.motZ * (double) f3, this.motX, this.motY, this.motZ);
            }

            f2 = 0.8F;
        }

        this.motX += this.b;
        this.motY += this.c;
        this.motZ += this.d;
        this.motX *= (double) f2;
        this.motY *= (double) f2;
        this.motZ *= (double) f2;
        this.world.a("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
        this.a(this.locX, this.locY, this.locZ);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) this.e);
        nbttagcompound.a("yTile", (short) this.f);
        nbttagcompound.a("zTile", (short) this.ai);
        nbttagcompound.a("inTile", (byte) this.aj);
        nbttagcompound.a("shake", (byte) this.a);
        nbttagcompound.a("inGround", (byte) (this.ak ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.c("xTile");
        this.f = nbttagcompound.c("yTile");
        this.ai = nbttagcompound.c("zTile");
        this.aj = nbttagcompound.b("inTile") & 255;
        this.a = nbttagcompound.b("shake") & 255;
        this.ak = nbttagcompound.b("inGround") == 1;
    }

    public boolean c_() {
        return true;
    }

    public boolean hurt(Entity entity, int i) {
        if (entity != null) {
            Vec3D vec3d = entity.B();

            if (vec3d != null) {
                this.motX = vec3d.a;
                this.motY = vec3d.b;
                this.motZ = vec3d.c;
                this.b = this.motX * 0.1D;
                this.c = this.motY * 0.1D;
                this.d = this.motZ * 0.1D;
            }

            return true;
        } else {
            return false;
        }
    }
}
