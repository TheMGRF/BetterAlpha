package net.minecraft.server.world.entity.impl;

import net.minecraft.server.item.Item;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.AxisAlignedBB;
import net.minecraft.server.world.World;
import net.minecraft.server.world.block.Block;
import net.minecraft.server.world.block.material.Material;
import net.minecraft.server.world.entity.Entity;

import java.util.List;

public class EntityBoat extends Entity {

    public int a = 0;
    public int b = 0;
    public int c = 1;

    public EntityBoat(World world) {
        super(world);
        this.i = true;
        this.a(1.5F, 0.6F);
        this.G = this.I / 2.0F;
        this.L = false;
    }

    public AxisAlignedBB d(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB q() {
        return this.boundingBox;
    }

    public boolean u() {
        return true;
    }

    public double j() {
        return (double) this.I * 0.0D - 0.30000001192092896D;
    }

    public boolean hurt(Entity entity, int i) {
        this.c = -this.c;
        this.b = 10;
        this.a += i * 10;
        if (this.a > 40) {
            int j;

            for (j = 0; j < 3; ++j) {
                this.a(Block.WOOD.bi, 1, 0.0F);
            }

            for (j = 0; j < 2; ++j) {
                this.a(Item.STICK.aW, 1, 0.0F);
            }

            this.die();
        }

        return true;
    }

    public boolean c_() {
        return !this.dead;
    }

    public void b_() {
        super.b_();
        if (this.b > 0) {
            --this.b;
        }

        if (this.a > 0) {
            --this.a;
        }

        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        byte b0 = 5;
        double d0 = 0.0D;

        for (int i = 0; i < b0; ++i) {
            double d1 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 0) / (double) b0 - 0.125D;
            double d2 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 1) / (double) b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.b(this.boundingBox.a, d1, this.boundingBox.c, this.boundingBox.d, d2, this.boundingBox.f);

            if (this.world.b(axisalignedbb, Material.f)) {
                d0 += 1.0D / (double) b0;
            }
        }

        double d3 = d0 * 2.0D - 1.0D;

        this.motY += 0.03999999910593033D * d3;
        if (this.j != null) {
            this.motX += this.j.motX * 0.2D;
            this.motZ += this.j.motZ * 0.2D;
        }

        double d4 = 0.4D;

        if (this.motX < -d4) {
            this.motX = -d4;
        }

        if (this.motX > d4) {
            this.motX = d4;
        }

        if (this.motZ < -d4) {
            this.motZ = -d4;
        }

        if (this.motZ > d4) {
            this.motZ = d4;
        }

        if (this.onGround) {
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
        }

        this.c(this.motX, this.motY, this.motZ);
        double d5 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
        double d6;
        double d7;

        if (d5 > 0.15D) {
            d6 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D);
            d7 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D);

            for (int j = 0; (double) j < 1.0D + d5 * 60.0D; ++j) {
                double d8 = (double) (this.V.nextFloat() * 2.0F - 1.0F);
                double d9 = (double) (this.V.nextInt(2) * 2 - 1) * 0.7D;
                double d10;
                double d11;

                if (this.V.nextBoolean()) {
                    d10 = this.locX - d6 * d8 * 0.8D + d7 * d9;
                    d11 = this.locZ - d7 * d8 * 0.8D - d6 * d9;
                    this.world.a("splash", d10, this.locY - 0.125D, d11, this.motX, this.motY, this.motZ);
                } else {
                    d10 = this.locX + d6 + d7 * d8 * 0.7D;
                    d11 = this.locZ + d7 - d6 * d8 * 0.7D;
                    this.world.a("splash", d10, this.locY - 0.125D, d11, this.motX, this.motY, this.motZ);
                }
            }
        }

        if (this.positionChanged && d5 > 0.15D) {
            this.die();

            int k;

            for (k = 0; k < 3; ++k) {
                this.a(Block.WOOD.bi, 1, 0.0F);
            }

            for (k = 0; k < 2; ++k) {
                this.a(Item.STICK.aW, 1, 0.0F);
            }
        } else {
            this.motX *= 0.9900000095367432D;
            this.motY *= 0.949999988079071D;
            this.motZ *= 0.9900000095367432D;
        }

        this.pitch = 0.0F;
        d6 = (double) this.yaw;
        d7 = this.lastX - this.locX;
        double d12 = this.lastZ - this.locZ;

        if (d7 * d7 + d12 * d12 > 0.0010D) {
            d6 = (double) ((float) (Math.atan2(d12, d7) * 180.0D / 3.141592653589793D));
        }

        double d13;

        for (d13 = d6 - (double) this.yaw; d13 >= 180.0D; d13 -= 360.0D) {
            ;
        }

        while (d13 < -180.0D) {
            d13 += 360.0D;
        }

        if (d13 > 20.0D) {
            d13 = 20.0D;
        }

        if (d13 < -20.0D) {
            d13 = -20.0D;
        }

        this.yaw = (float) ((double) this.yaw + d13);
        this.b(this.yaw, this.pitch);
        List list = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int l = 0; l < list.size(); ++l) {
                Entity entity = (Entity) list.get(l);

                if (entity != this.j && entity.u() && entity instanceof EntityBoat) {
                    entity.c((Entity) this);
                }
            }
        }

        if (this.j != null && this.j.dead) {
            this.j = null;
        }
    }

    public void z() {
        double d0 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
        double d1 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;

        this.j.a(this.locX + d0, this.locY + this.j() + this.j.A(), this.locZ + d1);
    }

    public void a(NBTTagCompound nbttagcompound) {
    }

    public void b(NBTTagCompound nbttagcompound) {
    }
}
