package net.minecraft.server.world.entity.impl;

import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.utils.MovingObjectPosition;
import net.minecraft.server.utils.Vec3D;
import net.minecraft.server.world.World;
import net.minecraft.server.world.block.Block;
import net.minecraft.server.world.block.material.Material;
import net.minecraft.server.world.block.sound.StepSound;
import net.minecraft.server.world.entity.Entity;

import java.util.List;

public class EntityLiving extends Entity {

    public int au = 20;
    public float av;
    public float aw;
    public float ax;
    public float ay = 0.0F;
    public float az = 0.0F;
    protected float aA;
    protected float aB;
    protected float aC;
    protected float aD;
    protected boolean aE = true;
    protected String aF = "/char.png";
    protected boolean aG = true;
    protected float aH = 0.0F;
    protected String aI = null;
    protected float aJ = 1.0F;
    protected int aK = 0;
    protected float aL = 0.0F;
    public boolean aM = false;
    public float aN;
    public float aO;
    public int health = 10;
    public int aQ;
    private int a;
    public int hurtTime;
    public int aS;
    public float aT = 0.0F;
    public int deathTime = 0;
    public int attackTime = 0;
    public float aW;
    public float aX;
    protected boolean aY = false;
    public int aZ = -1;
    public float ba = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float bb;
    public float bc;
    public float bd;
    private int b;
    private double c;
    private double d;
    private double e;
    private double f;
    private double ai;
    float be = 0.0F;
    protected int bf = 0;
    protected float bg;
    protected float bh;
    protected float bi;
    protected boolean bj = false;
    protected float bk = 0.0F;
    protected float bl = 0.7F;
    private Entity aj;
    private int ak = 0;

    public EntityLiving(World world) {
        super(world);
        this.i = true;
        this.ax = (float) (Math.random() + 1.0D) * 0.01F;
        this.a(this.locX, this.locY, this.locZ);
        this.av = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.aw = 1.0F;
        this.R = 0.5F;
    }

    protected boolean g(Entity entity) {
        return this.world.a(Vec3D.b(this.locX, this.locY + (double) this.s(), this.locZ), Vec3D.b(entity.locX, entity.locY + (double) entity.s(), entity.locZ)) == null;
    }

    public boolean c_() {
        return !this.dead;
    }

    public boolean u() {
        return !this.dead;
    }

    public float s() {
        return this.I * 0.85F;
    }

    public int b() {
        return 80;
    }

    public void m() {
        this.aN = this.aO;
        super.m();
        if (this.V.nextInt(1000) < this.a++) {
            this.a = -this.b();
            String s = this.d();

            if (s != null) {
                this.world.a(this, s, this.h(), (this.V.nextFloat() - this.V.nextFloat()) * 0.2F + 1.0F);
            }
        }

        if (this.w() && this.x()) {
            this.hurt(null, 1);
        }

        int i;

        if (this.w() && this.a(Material.f)) {
            --this.air;
            if (this.air == -20) {
                this.air = 0;

                for (i = 0; i < 8; ++i) {
                    float f = this.V.nextFloat() - this.V.nextFloat();
                    float f1 = this.V.nextFloat() - this.V.nextFloat();
                    float f2 = this.V.nextFloat() - this.V.nextFloat();

                    this.world.a("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }

                this.hurt(null, 2);
            }

            this.fire = 0;
        } else {
            this.air = this.Z;
        }

        this.aW = this.aX;
        if (this.attackTime > 0) {
            --this.attackTime;
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.ab > 0) {
            --this.ab;
        }

        if (this.health <= 0) {
            ++this.deathTime;
            if (this.deathTime > 20) {
                this.K();
                this.die();

                for (i = 0; i < 20; ++i) {
                    double d0 = this.V.nextGaussian() * 0.02D;
                    double d1 = this.V.nextGaussian() * 0.02D;
                    double d2 = this.V.nextGaussian() * 0.02D;

                    this.world.a("explode", this.locX + (double) (this.V.nextFloat() * this.H * 2.0F) - (double) this.H, this.locY + (double) (this.V.nextFloat() * this.I), this.locZ + (double) (this.V.nextFloat() * this.H * 2.0F) - (double) this.H, d0, d1, d2);
                }
            }
        }

        this.aD = this.aC;
        this.az = this.ay;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void I() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.V.nextGaussian() * 0.02D;
            double d1 = this.V.nextGaussian() * 0.02D;
            double d2 = this.V.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.V.nextFloat() * this.H * 2.0F) - (double) this.H - d0 * d3, this.locY + (double) (this.V.nextFloat() * this.I) - d1 * d3, this.locZ + (double) (this.V.nextFloat() * this.H * 2.0F) - (double) this.H - d2 * d3, d0, d1, d2);
        }
    }

    public void y() {
        super.y();
        this.aA = this.aB;
        this.aB = 0.0F;
    }

    public void b_() {
        super.b_();
        this.D();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = this.ay;
        float f2 = 0.0F;

        this.aA = this.aB;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            f1 = (float) Math.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aO > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aB += (f3 - this.aB) * 0.3F;

        float f4;

        for (f4 = f1 - this.ay; f4 < -180.0F; f4 += 360.0F) {
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.ay += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.ay; f5 < -180.0F; f5 += 360.0F) {
        }

        while (f5 >= 180.0F) {
            f5 -= 360.0F;
        }

        boolean flag = f5 < -90.0F || f5 >= 90.0F;

        if (f5 < -75.0F) {
            f5 = -75.0F;
        }

        if (f5 >= 75.0F) {
            f5 = 75.0F;
        }

        this.ay = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.ay += f5 * 0.2F;
        }

        if (flag) {
            f2 *= -1.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.ay - this.az < -180.0F) {
            this.az -= 360.0F;
        }

        while (this.ay - this.az >= 180.0F) {
            this.az += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.aC += f2;
    }

    public void a(float f, float f1) {
        super.a(f, f1);
    }

    public void a(int i) {
        if (this.health > 0) {
            this.health += i;
            if (this.health > 20) {
                this.health = 20;
            }

            this.ab = this.au / 2;
        }
    }

    public boolean hurt(Entity entity, int i) {
        if (this.world.z) {
            i = 0;
        }

        this.bf = 0;
        if (this.health <= 0) {
            return false;
        } else {
            this.bc = 1.5F;
            if ((float) this.ab > (float) this.au / 2.0F) {
                if (this.aQ - i >= this.health) {
                    return false;
                }

                this.health = this.aQ - i;
            } else {
                this.aQ = this.health;
                this.ab = this.au;
                this.health -= i;
                this.hurtTime = this.aS = 10;
            }

            this.aT = 0.0F;
            if (entity != null) {
                double d0 = entity.locX - this.locX;

                double d1;

                for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                    d0 = (Math.random() - Math.random()) * 0.01D;
                }

                this.aT = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                this.a(entity, i, d0, d1);
            } else {
                this.aT = (float) ((int) (Math.random() * 2.0D) * 180);
            }

            if (this.health <= 0) {
                this.world.a(this, this.f(), this.h(), (this.V.nextFloat() - this.V.nextFloat()) * 0.2F + 1.0F);
                this.die(entity);
            } else {
                this.world.a(this, this.e(), this.h(), (this.V.nextFloat() - this.V.nextFloat()) * 0.2F + 1.0F);
            }

            return true;
        }
    }

    protected float h() {
        return 1.0F;
    }

    protected String d() {
        return null;
    }

    protected String e() {
        return "random.hurt";
    }

    protected String f() {
        return "random.hurt";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = 0.4F;

        this.motX /= 2.0D;
        this.motY /= 2.0D;
        this.motZ /= 2.0D;
        this.motX -= d0 / (double) f * (double) f1;
        this.motY += 0.4000000059604645D;
        this.motZ -= d1 / (double) f * (double) f1;
        if (this.motY > 0.4000000059604645D) {
            this.motY = 0.4000000059604645D;
        }
    }

    public void die(Entity entity) {
        if (this.aK > 0 && entity != null) {
            entity.b(this, this.aK);
        }

        this.aY = true;
        int i = this.g();

        if (i > 0) {
            int j = this.V.nextInt(3);

            for (int k = 0; k < j; ++k) {
                this.a(i, 1);
            }
        }
    }

    protected int g() {
        return 0;
    }

    public void a(float f) {
        int i = (int) Math.ceil(f - 3.0F);

        if (i > 0) {
            this.hurt(null, i);
            int j = this.world.a(MathHelper.b(this.locX), MathHelper.b(this.locY - 0.20000000298023224D - (double) this.G), MathHelper.b(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.n[j].br;

                this.world.a(this, stepsound.c(), stepsound.a() * 0.5F, stepsound.b() * 0.75F);
            }
        }
    }

    public void c(float f, float f1) {
        double d0;

        if (this.r()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.b(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.t()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.b(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.54600006F;
                int i = this.world.a(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

                if (i > 0) {
                    f2 = Block.n[i].bu * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);

            this.a(f, f1, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.a(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

                if (j > 0) {
                    f2 = Block.n[j].bu * 0.91F;
                }
            }

            if (this.d_()) {
                this.fallDistance = 0.0F;
                if (this.motY < -0.15D) {
                    this.motY = -0.15D;
                }
            }

            this.c(this.motX, this.motY, this.motZ);
            if (this.positionChanged && this.d_()) {
                this.motY = 0.2D;
            }

            this.motY -= 0.08D;
            this.motY *= 0.9800000190734863D;
            this.motX *= f2;
            this.motZ *= f2;
        }

        this.bb = this.bc;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f4 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.bc += (f4 - this.bc) * 0.4F;
        this.bd += this.bc;
    }

    public boolean d_() {
        int i = MathHelper.b(this.locX);
        int j = MathHelper.b(this.boundingBox.b);
        int k = MathHelper.b(this.locZ);

        return this.world.a(i, j, k) == Block.LADDER.bi || this.world.a(i, j + 1, k) == Block.LADDER.bi;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) this.health);
        nbttagcompound.a("HurtTime", (short) this.hurtTime);
        nbttagcompound.a("DeathTime", (short) this.deathTime);
        nbttagcompound.a("AttackTime", (short) this.attackTime);
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.c("Health");
        if (!nbttagcompound.a("Health")) {
            this.health = 10;
        }

        this.hurtTime = nbttagcompound.c("HurtTime");
        this.deathTime = nbttagcompound.c("DeathTime");
        this.attackTime = nbttagcompound.c("AttackTime");
    }

    public boolean w() {
        return !this.dead && this.health > 0;
    }

    public void D() {
        if (this.b > 0) {
            double d0 = this.locX + (this.c - this.locX) / (double) this.b;
            double d1 = this.locY + (this.d - this.locY) / (double) this.b;
            double d2 = this.locZ + (this.e - this.locZ) / (double) this.b;

            double d3;

            for (d3 = this.f - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.b);
            this.pitch = (float) ((double) this.pitch + (this.ai - (double) this.pitch) / (double) this.b);
            --this.b;
            this.a(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        }

        if (this.health <= 0) {
            this.bj = false;
            this.bg = 0.0F;
            this.bh = 0.0F;
            this.bi = 0.0F;
        } else if (!this.aM) {
            this.c();
        }

        boolean flag = this.r();
        boolean flag1 = this.t();

        if (this.bj) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.J();
            }
        }

        this.bg *= 0.98F;
        this.bh *= 0.98F;
        this.bi *= 0.9F;
        this.c(this.bg, this.bh);
        List list = this.world.b(this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.u()) {
                    entity.c(this);
                }
            }
        }
    }

    protected void J() {
        this.motY = 0.41999998688697815D;
    }

    protected void c() {
        ++this.bf;
        EntityHuman entityhuman = this.world.a(this, -1.0D);

        if (entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 16384.0D) {
                this.die();
            }

            if (this.bf > 600 && this.V.nextInt(800) == 0) {
                if (d3 < 1024.0D) {
                    this.bf = 0;
                } else {
                    this.die();
                }
            }
        }

        this.bg = 0.0F;
        this.bh = 0.0F;
        float f = 8.0F;

        if (this.V.nextFloat() < 0.02F) {
            entityhuman = this.world.a(this, f);
            if (entityhuman != null) {
                this.aj = entityhuman;
                this.ak = 10 + this.V.nextInt(20);
            } else {
                this.bi = (this.V.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.aj != null) {
            this.b(this.aj, 10.0F);
            if (this.ak-- <= 0 || this.aj.dead || this.aj.b(this) > (double) (f * f)) {
                this.aj = null;
            }
        } else {
            if (this.V.nextFloat() < 0.05F) {
                this.bi = (this.V.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.bi;
            this.pitch = this.bk;
        }

        boolean flag = this.r();
        boolean flag1 = this.t();

        if (flag || flag1) {
            this.bj = this.V.nextFloat() < 0.8F;
        }
    }

    public void b(Entity entity, float f) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = entityliving.locY + (double) entityliving.s() - (this.locY + (double) this.s());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.s());
        }

        double d3 = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f2 = (float) (Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D);

        this.pitch = this.b(this.pitch, f2, f);
        this.yaw = this.b(this.yaw, f1, f);
    }

    private float b(float f, float f1, float f2) {
        float f3;

        for (f3 = f1 - f; f3 < -180.0F; f3 += 360.0F) {
        }

        while (f3 >= 180.0F) {
            f3 -= 360.0F;
        }

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public void K() {
    }

    public boolean a() {
        return this.world.a(this.boundingBox) && this.world.a(this, this.boundingBox).size() == 0 && !this.world.b(this.boundingBox);
    }

    public void o() {
        this.hurt(null, 4);
    }

    public Vec3D c(float f) {
        if (f == 1.0F) {
            return Vec3D.b(this.locX, this.locY, this.locZ);
        } else {
            double d0 = this.lastX + (this.locX - this.lastX) * (double) f;
            double d1 = this.lastY + (this.locY - this.lastY) * (double) f;
            double d2 = this.lastZ + (this.locZ - this.lastZ) * (double) f;

            return Vec3D.b(d0, d1, d2);
        }
    }

    public Vec3D B() {
        return this.d(1.0F);
    }

    public Vec3D d(float f) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (f == 1.0F) {
            f1 = MathHelper.b(-this.yaw * 0.017453292F - 3.1415927F);
            f2 = MathHelper.a(-this.yaw * 0.017453292F - 3.1415927F);
            f3 = -MathHelper.b(-this.pitch * 0.017453292F);
            f4 = MathHelper.a(-this.pitch * 0.017453292F);
            return Vec3D.b(f2 * f3, f4, f1 * f3);
        } else {
            f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.yaw - this.lastYaw) * f;
            f3 = MathHelper.b(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.a(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.b(-f1 * 0.017453292F);
            float f6 = MathHelper.a(-f1 * 0.017453292F);

            return Vec3D.b(f4 * f5, f6, f3 * f5);
        }
    }

    public MovingObjectPosition a(double d0, float f) {
        Vec3D vec3d = this.c(f);
        Vec3D vec3d1 = this.d(f);
        Vec3D vec3d2 = vec3d.c(vec3d1.a * d0, vec3d1.b * d0, vec3d1.c * d0);

        return this.world.a(vec3d, vec3d2);
    }

    public int i() {
        return 4;
    }
}
