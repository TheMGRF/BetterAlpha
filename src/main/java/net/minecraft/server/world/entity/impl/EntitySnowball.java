package net.minecraft.server.world.entity.impl;

import net.minecraft.server.item.Item;
import net.minecraft.server.item.ItemStack;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.AxisAlignedBB;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.utils.MovingObjectPosition;
import net.minecraft.server.utils.Vec3D;
import net.minecraft.server.world.World;
import net.minecraft.server.world.entity.Entity;

import java.util.List;

public class EntitySnowball extends Entity {

    private int b = -1;
    private int c = -1;
    private int d = -1;
    private int e = 0;
    private boolean f = false;
    public int a = 0;
    private EntityLiving ai;
    private int aj;
    private int ak = 0;

    public EntitySnowball(World world) {
        super(world);
        this.a(0.25F, 0.25F);
    }

    public void b_() {
        super.b_();
        if (this.a > 0) {
            --this.a;
        }

        if (this.f) {
            int i = this.world.a(this.b, this.c, this.d);

            if (i == this.e) {
                ++this.aj;
                if (this.aj == 1200) {
                    this.die();
                }

                return;
            }

            this.f = false;
            this.motX *= (double) (this.V.nextFloat() * 0.2F);
            this.motY *= (double) (this.V.nextFloat() * 0.2F);
            this.motZ *= (double) (this.V.nextFloat() * 0.2F);
            this.aj = 0;
            this.ak = 0;
        } else {
            ++this.ak;
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

        int j;
        float f;

        for (j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity) list.get(j);

            if (entity1.c_() && (entity1 != this.ai || this.ak >= 5)) {
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
            if (movingobjectposition.g != null && movingobjectposition.g.hurt(this.ai, 0)) {
                ;
            }

            for (j = 0; j < 8; ++j) {
                this.world.a("snowballpoof", this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D);
            }

            this.die();
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
        float f2 = 0.99F;

        f = 0.03F;
        if (this.r()) {
            for (int k = 0; k < 4; ++k) {
                float f3 = 0.25F;

                this.world.a("bubble", this.locX - this.motX * (double) f3, this.locY - this.motY * (double) f3, this.locZ - this.motZ * (double) f3, this.motX, this.motY, this.motZ);
            }

            f2 = 0.8F;
        }

        this.motX *= (double) f2;
        this.motY *= (double) f2;
        this.motZ *= (double) f2;
        this.motY -= (double) f;
        this.a(this.locX, this.locY, this.locZ);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) this.b);
        nbttagcompound.a("yTile", (short) this.c);
        nbttagcompound.a("zTile", (short) this.d);
        nbttagcompound.a("inTile", (byte) this.e);
        nbttagcompound.a("shake", (byte) this.a);
        nbttagcompound.a("inGround", (byte) (this.f ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.b = nbttagcompound.c("xTile");
        this.c = nbttagcompound.c("yTile");
        this.d = nbttagcompound.c("zTile");
        this.e = nbttagcompound.b("inTile") & 255;
        this.a = nbttagcompound.b("shake") & 255;
        this.f = nbttagcompound.b("inGround") == 1;
    }

    public void a(EntityHuman entityhuman) {
        if (this.f && this.ai == entityhuman && this.a <= 0 && entityhuman.inventory.a(new ItemStack(Item.ARROW.aW, 1))) {
            this.world.a(this, "random.pop", 0.2F, ((this.V.nextFloat() - this.V.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityhuman.c(this, 1);
            this.die();
        }
    }
}
