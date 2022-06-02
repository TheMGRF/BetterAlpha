package net.minecraft.server.world.entity.impl;

import net.minecraft.server.world.World;

public class EntityGiantZombie extends EntityMonster {

    public EntityGiantZombie(World world) {
        super(world);
        this.aF = "/mob/zombie.png";
        this.bl = 0.5F;
        this.e = 50;
        this.health *= 10;
        this.G *= 6.0F;
        this.a(this.H * 6.0F, this.I * 6.0F);
    }

    protected float a(int i, int j, int k) {
        return this.world.j(i, j, k) - 0.5F;
    }
}
