package net.minecraft.server.world.entity;

import net.minecraft.server.item.ItemStack;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.nbt.NBTTagDouble;
import net.minecraft.server.nbt.NBTTagFloat;
import net.minecraft.server.nbt.NBTTagList;
import net.minecraft.server.utils.AxisAlignedBB;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.utils.Vec3D;
import net.minecraft.server.world.World;
import net.minecraft.server.world.block.Block;
import net.minecraft.server.world.block.BlockFluids;
import net.minecraft.server.world.block.material.Material;
import net.minecraft.server.world.block.sound.StepSound;
import net.minecraft.server.world.entity.impl.EntityHuman;
import net.minecraft.server.world.entity.impl.EntityItem;

import java.util.List;
import java.util.Random;

public abstract class Entity {

    private static int a = 0;
    public int g;
    public double h;
    public boolean i;
    public Entity j;
    public Entity k;
    public World world;
    public double lastX;
    public double lastY;
    public double lastZ;
    public double locX;
    public double locY;
    public double locZ;
    public double motX;
    public double motY;
    public double motZ;
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;
    public final AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean positionChanged;
    public boolean C; // hasCollided horizontal?
    public boolean D; // hasCollided vertical?
    public boolean E; // velocityChanged (motion)
    public boolean dead; // customVelocity (motion) (OR DEAD?)
    public float G; // height
    public float H; // width
    public float I; // length
    public float J;
    public float K;
    public boolean L;
    public float fallDistance;
    private int b; // nextStepDistance
    public double N; // lastTickX
    public double O; // lastTickY
    public double P; // lastTickZ
    public float Q; // ySize(?)
    public float R; // stepHeight
    public boolean S; // hasCollisions (enabling collisions with world) ?
    public float T; // reduced velocity amount?
    public boolean U; // idk maybe not used?
    public Random V;
    public int W; // enterWaterTick?
    public int X; // fireTicks?
    public int fire;
    public int Z;
    public boolean aa;
    public int ab;
    public int air;
    private boolean c;
    public boolean ad;
    private double d;
    private double e;
    public boolean ae;
    public int af;
    public int ag;
    public int ah;

    public Entity(World world) {
        this.g = a++;
        this.h = 1.0D;
        this.i = false;
        this.boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.D = false;
        this.E = true;
        this.dead = false;
        this.G = 0.0F;
        this.H = 0.6F;
        this.I = 1.8F;
        this.J = 0.0F;
        this.K = 0.0F;
        this.L = true;
        this.fallDistance = 0.0F;
        this.b = 1;
        this.Q = 0.0F;
        this.R = 0.0F;
        this.S = false;
        this.T = 0.0F;
        this.U = false;
        this.V = new Random();
        this.W = 0;
        this.X = 1;
        this.fire = 0;
        this.Z = 300;
        this.aa = false;
        this.ab = 0;
        this.air = 300;
        this.c = true;
        this.ad = false;
        this.ae = false;
        this.world = world;
        this.a(0.0D, 0.0D, 0.0D);
    }

    public boolean equals(Object object) {
        return object instanceof Entity && ((Entity) object).g == this.g;
    }

    public int hashCode() {
        return this.g;
    }

    public void die() {
        this.dead = true;
    }

    public void a(float f, float f1) {
        this.H = f;
        this.I = f1;
    }

    public void b(float f, float f1) {
        this.yaw = f;
        this.pitch = f1;
    }

    public void a(double d0, double d1, double d2) {
        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        float f = this.H / 2.0F;
        float f1 = this.I;

        this.boundingBox.c(d0 - (double) f, d1 - (double) this.G + (double) this.Q, d2 - (double) f, d0 + (double) f, d1 - (double) this.G + (double) this.Q + (double) f1, d2 + (double) f);
    }

    public void b_() {
        this.m();
    }

    public void m() {
        if (this.k != null && this.k.dead) {
            this.k = null;
        }

        ++this.W;
        this.J = this.K;
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
        if (this.r()) {
            if (!this.aa && !this.c) {
                float f = MathHelper.a(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                this.world.a(this, "random.splash", f, 1.0F + (this.V.nextFloat() - this.V.nextFloat()) * 0.4F);
                float f1 = (float) MathHelper.b(this.boundingBox.b);

                int i;
                float f2;
                float f3;

                for (i = 0; (float) i < 1.0F + this.H * 20.0F; ++i) {
                    f2 = (this.V.nextFloat() * 2.0F - 1.0F) * this.H;
                    f3 = (this.V.nextFloat() * 2.0F - 1.0F) * this.H;
                    this.world.a("bubble", this.locX + (double) f2, f1 + 1.0F, this.locZ + (double) f3, this.motX, this.motY - (double) (this.V.nextFloat() * 0.2F), this.motZ);
                }

                for (i = 0; (float) i < 1.0F + this.H * 20.0F; ++i) {
                    f2 = (this.V.nextFloat() * 2.0F - 1.0F) * this.H;
                    f3 = (this.V.nextFloat() * 2.0F - 1.0F) * this.H;
                    this.world.a("splash", this.locX + (double) f2, f1 + 1.0F, this.locZ + (double) f3, this.motX, this.motY, this.motZ);
                }
            }

            this.fallDistance = 0.0F;
            this.aa = true;
            this.fire = 0;
        } else {
            this.aa = false;
        }

        if (this.fire > 0) {
            if (this.ad) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.fire = 0;
                }
            } else {
                if (this.fire % 20 == 0) {
                    this.hurt(null, 1);
                }

                --this.fire;
            }
        }

        if (this.t()) {
            this.n();
        }

        if (this.locY < -64.0D) {
            this.die();
        }

        this.c = false;
    }

    public void n() {
        if (this.ad) {
            this.fire = 600;
        } else {
            this.hurt(null, 4);
            this.fire = 600;
        }
    }

    public boolean b(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.boundingBox.c(d0, d1, d2);
        List list = this.world.a(this, axisalignedbb);

        return list.size() <= 0 && !this.world.b(axisalignedbb);
    }

    public void c(double d0, double d1, double d2) {
        if (this.S) {
            this.boundingBox.d(d0, d1, d2);
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.G - (double) this.Q;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
        } else {
            double d3 = this.locX;
            double d4 = this.locZ;
            double d5 = d0;
            double d6 = d1;
            double d7 = d2;
            AxisAlignedBB axisalignedbb = this.boundingBox.b();
            boolean flag = this.onGround && this.p();

            if (flag) {
                double d8;

                for (d8 = 0.05D; d0 != 0.0D && this.world.a(this, this.boundingBox.c(d0, -1.0D, 0.0D)).size() == 0; d5 = d0) {
                    if (d0 < d8 && d0 >= -d8) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d8;
                    } else {
                        d0 += d8;
                    }
                }

                for (; d2 != 0.0D && this.world.a(this, this.boundingBox.c(0.0D, -1.0D, d2)).size() == 0; d7 = d2) {
                    if (d2 < d8 && d2 >= -d8) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d8;
                    } else {
                        d2 += d8;
                    }
                }
            }

            List list = this.world.a(this, this.boundingBox.a(d0, d1, d2));

            for (int i = 0; i < list.size(); ++i) {
                d1 = ((AxisAlignedBB) list.get(i)).b(this.boundingBox, d1);
            }

            this.boundingBox.d(0.0D, d1, 0.0D);
            if (!this.E && d6 != d1) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            boolean flag1 = this.onGround || d6 != d1 && d6 < 0.0D;

            int j;

            for (j = 0; j < list.size(); ++j) {
                d0 = ((AxisAlignedBB) list.get(j)).a(this.boundingBox, d0);
            }

            this.boundingBox.d(d0, 0.0D, 0.0D);
            if (!this.E && d5 != d0) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            for (j = 0; j < list.size(); ++j) {
                d2 = ((AxisAlignedBB) list.get(j)).c(this.boundingBox, d2);
            }

            this.boundingBox.d(0.0D, 0.0D, d2);
            if (!this.E && d7 != d2) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            double d9;
            double d10;
            int k;

            if (this.R > 0.0F && flag1 && this.Q < 0.05F && (d5 != d0 || d7 != d2)) {
                d9 = d0;
                d10 = d1;
                double d11 = d2;

                d0 = d5;
                d1 = this.R;
                d2 = d7;
                AxisAlignedBB axisalignedbb1 = this.boundingBox.b();

                this.boundingBox.b(axisalignedbb);
                list = this.world.a(this, this.boundingBox.a(d5, d1, d7));

                for (k = 0; k < list.size(); ++k) {
                    d1 = ((AxisAlignedBB) list.get(k)).b(this.boundingBox, d1);
                }

                this.boundingBox.d(0.0D, d1, 0.0D);
                if (!this.E && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    d0 = ((AxisAlignedBB) list.get(k)).a(this.boundingBox, d0);
                }

                this.boundingBox.d(d0, 0.0D, 0.0D);
                if (!this.E && d5 != d0) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    d2 = ((AxisAlignedBB) list.get(k)).c(this.boundingBox, d2);
                }

                this.boundingBox.d(0.0D, 0.0D, d2);
                if (!this.E && d7 != d2) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                if (d9 * d9 + d11 * d11 >= d0 * d0 + d2 * d2) {
                    d0 = d9;
                    d1 = d10;
                    d2 = d11;
                    this.boundingBox.b(axisalignedbb1);
                } else {
                    this.Q = (float) ((double) this.Q + 0.5D);
                }
            }

            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.G - (double) this.Q;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
            this.positionChanged = d5 != d0 || d7 != d2;
            this.C = d6 != d1;
            this.onGround = d6 != d1 && d6 < 0.0D;
            this.D = this.positionChanged || this.C;
            if (this.onGround) {
                if (this.fallDistance > 0.0F) {
                    this.a(this.fallDistance);
                    this.fallDistance = 0.0F;
                }
            } else if (d1 < 0.0D) {
                this.fallDistance = (float) ((double) this.fallDistance - d1);
            }

            if (d5 != d0) {
                this.motX = 0.0D;
            }

            if (d6 != d1) {
                this.motY = 0.0D;
            }

            if (d7 != d2) {
                this.motZ = 0.0D;
            }

            d9 = this.locX - d3;
            d10 = this.locZ - d4;
            this.K = (float) ((double) this.K + (double) MathHelper.a(d9 * d9 + d10 * d10) * 0.6D);
            int l;
            int i1;
            int j1;

            if (this.L && !flag) {
                l = MathHelper.b(this.locX);
                i1 = MathHelper.b(this.locY - 0.20000000298023224D - (double) this.G);
                j1 = MathHelper.b(this.locZ);
                k = this.world.a(l, i1, j1);
                if (this.K > (float) this.b && k > 0) {
                    ++this.b;
                    StepSound stepsound = Block.n[k].br;

                    if (this.world.a(l, i1 + 1, j1) == Block.SNOW.bi) {
                        stepsound = Block.SNOW.br;
                        this.world.a(this, stepsound.c(), stepsound.a() * 0.15F, stepsound.b());
                    } else if (!Block.n[k].bt.d()) {
                        this.world.a(this, stepsound.c(), stepsound.a() * 0.15F, stepsound.b());
                    }

                    Block.n[k].b(this.world, l, i1, j1, this);
                }
            }

            l = MathHelper.b(this.boundingBox.a);
            i1 = MathHelper.b(this.boundingBox.b);
            j1 = MathHelper.b(this.boundingBox.c);
            k = MathHelper.b(this.boundingBox.d);
            int k1 = MathHelper.b(this.boundingBox.e);
            int l1 = MathHelper.b(this.boundingBox.f);

            for (int i2 = l; i2 <= k; ++i2) {
                for (int j2 = i1; j2 <= k1; ++j2) {
                    for (int k2 = j1; k2 <= l1; ++k2) {
                        int l2 = this.world.a(i2, j2, k2);

                        if (l2 > 0) {
                            Block.n[l2].a(this.world, i2, j2, k2, this);
                        }
                    }
                }
            }

            this.Q *= 0.4F;
            boolean flag2 = this.r();

            if (this.world.c(this.boundingBox)) {
                this.b(1);
                if (!flag2) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.fire = 300;
                    }
                }
            } else if (this.fire <= 0) {
                this.fire = -this.X;
            }

            if (flag2 && this.fire > 0) {
                this.world.a(this, "random.fizz", 0.7F, 1.6F + (this.V.nextFloat() - this.V.nextFloat()) * 0.4F);
                this.fire = -this.X;
            }
        }
    }

    public boolean p() {
        return false;
    }

    public AxisAlignedBB q() {
        return null;
    }

    public void b(int i) {
        this.hurt(null, i);
    }

    public void a(float f) {
    }

    public boolean r() {
        return this.world.a(this.boundingBox.b(0.0D, -0.4000000059604645D, 0.0D), Material.f, this);
    }

    public boolean a(Material material) {
        double d0 = this.locY + (double) this.s();
        int i = MathHelper.b(this.locX);
        int j = MathHelper.d((float) MathHelper.b(d0));
        int k = MathHelper.b(this.locZ);
        int l = this.world.a(i, j, k);

        if (l != 0 && Block.n[l].bt == material) {
            float f = BlockFluids.b(this.world.b(i, j, k)) - 0.11111111F;
            float f1 = (float) (j + 1) - f;

            return d0 < (double) f1;
        } else {
            return false;
        }
    }

    public float s() {
        return 0.0F;
    }

    public boolean t() {
        return this.world.a(this.boundingBox.b(0.0D, -0.4000000059604645D, 0.0D), Material.g);
    }

    public void a(float f, float f1, float f2) {
        float f3 = MathHelper.c(f * f + f1 * f1);

        if (f3 >= 0.01F) {
            if (f3 < 1.0F) {
                f3 = 1.0F;
            }

            f3 = f2 / f3;
            f *= f3;
            f1 *= f3;
            float f4 = MathHelper.a(this.yaw * 3.1415927F / 180.0F);
            float f5 = MathHelper.b(this.yaw * 3.1415927F / 180.0F);

            this.motX += f * f5 - f1 * f4;
            this.motZ += f1 * f5 + f * f4;
        }
    }

    public float b(float f) {
        int i = MathHelper.b(this.locX);
        double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.66D;
        int j = MathHelper.b(this.locY - (double) this.G + d0);
        int k = MathHelper.b(this.locZ);

        return this.world.j(i, j, k);
    }

    public void spawnIn(World world) {
        this.world = world;
    }

    public void b(double d0, double d1, double d2, float f, float f1) {
        this.lastX = this.locX = d0;
        this.lastY = this.locY = d1;
        this.lastZ = this.locZ = d2;
        this.yaw = f;
        this.pitch = f1;
        this.Q = 0.0F;
        double d3 = this.lastYaw - f;

        if (d3 < -180.0D) {
            this.lastYaw += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.lastYaw -= 360.0F;
        }

        this.a(this.locX, this.locY, this.locZ);
    }

    public void c(double d0, double d1, double d2, float f, float f1) {
        this.lastX = this.locX = d0;
        this.lastY = this.locY = d1 + (double) this.G;
        this.lastZ = this.locZ = d2;
        this.yaw = f;
        this.pitch = f1;
        this.a(this.locX, this.locY, this.locZ);
    }

    public float a(Entity entity) {
        float f = (float) (this.locX - entity.locX);
        float f1 = (float) (this.locY - entity.locY);
        float f2 = (float) (this.locZ - entity.locZ);

        return MathHelper.c(f * f + f1 * f1 + f2 * f2);
    }

    public double d(double d0, double d1, double d2) {
        double d3 = this.locX - d0;
        double d4 = this.locY - d1;
        double d5 = this.locZ - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double e(double d0, double d1, double d2) {
        double d3 = this.locX - d0;
        double d4 = this.locY - d1;
        double d5 = this.locZ - d2;

        return MathHelper.a(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double b(Entity entity) {
        double d0 = this.locX - entity.locX;
        double d1 = this.locY - entity.locY;
        double d2 = this.locZ - entity.locZ;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void a(EntityHuman entityhuman) {
    }

    public void c(Entity entity) {
        if (entity.j != this && entity.k != this) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;
            double d2 = MathHelper.a(d0, d1);

            if (d2 >= 0.009999999776482582D) {
                d2 = MathHelper.a(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= 1.0F - this.T;
                d1 *= 1.0F - this.T;
                this.f(-d0, 0.0D, -d1);
                entity.f(d0, 0.0D, d1);
            }
        }
    }

    public void f(double d0, double d1, double d2) {
        this.motX += d0;
        this.motY += d1;
        this.motZ += d2;
    }

    public boolean hurt(Entity entity, int i) {
        return false;
    }

    public boolean c_() {
        return false;
    }

    public boolean u() {
        return false;
    }

    public void b(Entity entity, int i) {
    }

    public boolean c(NBTTagCompound nbttagcompound) {
        String s = this.v();

        if (!this.dead && s != null) {
            nbttagcompound.a("id", s);
            this.d(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public void d(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Pos", this.a(new double[]{this.locX, this.locY, this.locZ}));
        nbttagcompound.a("Motion", this.a(new double[]{this.motX, this.motY, this.motZ}));
        nbttagcompound.a("Rotation", this.a(new float[]{this.yaw, this.pitch}));
        nbttagcompound.a("FallDistance", this.fallDistance);
        nbttagcompound.a("Fire", (short) this.fire);
        nbttagcompound.a("Air", (short) this.air);
        nbttagcompound.a("OnGround", this.onGround);
        this.a(nbttagcompound);
    }

    public void e(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.k("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.k("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.k("Rotation");

        this.a(0.0D, 0.0D, 0.0D);
        this.motX = ((NBTTagDouble) nbttaglist1.a(0)).a;
        this.motY = ((NBTTagDouble) nbttaglist1.a(1)).a;
        this.motZ = ((NBTTagDouble) nbttaglist1.a(2)).a;
        this.lastX = this.N = this.locX = ((NBTTagDouble) nbttaglist.a(0)).a;
        this.lastY = this.O = this.locY = ((NBTTagDouble) nbttaglist.a(1)).a;
        this.lastZ = this.P = this.locZ = ((NBTTagDouble) nbttaglist.a(2)).a;
        this.lastYaw = this.yaw = ((NBTTagFloat) nbttaglist2.a(0)).a;
        this.lastPitch = this.pitch = ((NBTTagFloat) nbttaglist2.a(1)).a;
        this.fallDistance = nbttagcompound.f("FallDistance");
        this.fire = nbttagcompound.c("Fire");
        this.air = nbttagcompound.c("Air");
        this.onGround = nbttagcompound.l("OnGround");
        this.a(this.locX, this.locY, this.locZ);
        this.b(nbttagcompound);
    }

    public final String v() {
        return EntityTypes.b(this);
    }

    public abstract void b(NBTTagCompound nbttagcompound);

    public abstract void a(NBTTagCompound nbttagcompound);

    public NBTTagList a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.a(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    public NBTTagList a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.a(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    public EntityItem a(int i, int j) {
        return this.a(i, j, 0.0F);
    }

    public EntityItem a(int i, int j, float f) {
        EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY + (double) f, this.locZ, new ItemStack(i, j));

        entityitem.c = 10;
        this.world.trackEntity(entityitem);
        return entityitem;
    }

    public boolean w() {
        return !this.dead;
    }

    public boolean x() {
        int i = MathHelper.b(this.locX);
        int j = MathHelper.b(this.locY + (double) this.s());
        int k = MathHelper.b(this.locZ);

        return this.world.d(i, j, k);
    }

    public AxisAlignedBB d(Entity entity) {
        return null;
    }

    public void y() {
        if (this.k.dead) {
            this.k = null;
        } else {
            this.motX = 0.0D;
            this.motY = 0.0D;
            this.motZ = 0.0D;
            this.b_();
            this.k.z();
            this.e += this.k.yaw - this.k.lastYaw;

            for (this.d += this.k.pitch - this.k.lastPitch; this.e >= 180.0D; this.e -= 360.0D) {
            }

            while (this.e < -180.0D) {
                this.e += 360.0D;
            }

            while (this.d >= 180.0D) {
                this.d -= 360.0D;
            }

            while (this.d < -180.0D) {
                this.d += 360.0D;
            }

            double d0 = this.e * 0.5D;
            double d1 = this.d * 0.5D;
            float f = 10.0F;

            if (d0 > (double) f) {
                d0 = f;
            }

            if (d0 < (double) (-f)) {
                d0 = -f;
            }

            if (d1 > (double) f) {
                d1 = f;
            }

            if (d1 < (double) (-f)) {
                d1 = -f;
            }

            this.e -= d0;
            this.d -= d1;
            this.yaw = (float) ((double) this.yaw + d0);
            this.pitch = (float) ((double) this.pitch + d1);
        }
    }

    public void z() {
        this.j.a(this.locX, this.locY + this.j() + this.j.A(), this.locZ);
    }

    public double A() {
        return this.G;
    }

    public double j() {
        return (double) this.I * 0.75D;
    }

    public void e(Entity entity) {
        this.d = 0.0D;
        this.e = 0.0D;
        if (this.k == entity) {
            this.k.j = null;
            this.k = null;
            this.c(entity.locX, entity.boundingBox.b + (double) entity.I, entity.locZ, this.yaw, this.pitch);
        } else {
            if (this.k != null) {
                this.k.j = null;
            }

            if (entity.j != null) {
                entity.j.k = null;
            }

            this.k = entity;
            entity.j = this;
        }
    }

    public Vec3D B() {
        return null;
    }

    public void C() {
    }
}
