package net.minecraft.server.world.block.tile;

import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.AxisAlignedBB;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.EntityTypes;
import net.minecraft.server.world.entity.impl.EntityLiving;

public class TileEntityMobSpawner extends TileEntity {

    public int e = -1;
    public String f = "Pig";
    public double g;
    public double h = 0.0D;

    public TileEntityMobSpawner() {
        this.e = 20;
    }

    public boolean a() {
        return this.world.a((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D, 16.0D) != null;
    }

    public void b() {
        this.h = this.g;
        if (this.a()) {
            double d0 = (double) ((float) this.x + this.world.l.nextFloat());
            double d1 = (double) ((float) this.y + this.world.l.nextFloat());
            double d2 = (double) ((float) this.z + this.world.l.nextFloat());

            this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);

            for (this.g += (double) (1000.0F / ((float) this.e + 200.0F)); this.g > 360.0D; this.h -= 360.0D) {
                this.g -= 360.0D;
            }

            if (this.e == -1) {
                this.d();
            }

            if (this.e > 0) {
                --this.e;
            } else {
                byte b0 = 4;

                for (int i = 0; i < b0; ++i) {
                    EntityLiving entityliving = (EntityLiving) ((EntityLiving) EntityTypes.a(this.f, this.world));

                    if (entityliving == null) {
                        return;
                    }

                    int j = this.world.a(entityliving.getClass(), AxisAlignedBB.b((double) this.x, (double) this.y, (double) this.z, (double) (this.x + 1), (double) (this.y + 1), (double) (this.z + 1)).b(8.0D, 4.0D, 8.0D)).size();

                    if (j >= 6) {
                        this.d();
                        return;
                    }

                    if (entityliving != null) {
                        double d3 = (double) this.x + (this.world.l.nextDouble() - this.world.l.nextDouble()) * 4.0D;
                        double d4 = (double) (this.y + this.world.l.nextInt(3) - 1);
                        double d5 = (double) this.z + (this.world.l.nextDouble() - this.world.l.nextDouble()) * 4.0D;

                        entityliving.c(d3, d4, d5, this.world.l.nextFloat() * 360.0F, 0.0F);
                        if (entityliving.a()) {
                            this.world.trackEntity((Entity) entityliving);

                            for (int k = 0; k < 20; ++k) {
                                d0 = (double) this.x + 0.5D + ((double) this.world.l.nextFloat() - 0.5D) * 2.0D;
                                d1 = (double) this.y + 0.5D + ((double) this.world.l.nextFloat() - 0.5D) * 2.0D;
                                d2 = (double) this.z + 0.5D + ((double) this.world.l.nextFloat() - 0.5D) * 2.0D;
                                this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                                this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                            }

                            entityliving.I();
                            this.d();
                        }
                    }
                }

                super.b();
            }
        }
    }

    private void d() {
        this.e = 200 + this.world.l.nextInt(600);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.f = nbttagcompound.h("EntityId");
        this.e = nbttagcompound.c("Delay");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("EntityId", this.f);
        nbttagcompound.a("Delay", (short) this.e);
    }
}
