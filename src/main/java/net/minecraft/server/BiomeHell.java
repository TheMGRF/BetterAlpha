package net.minecraft.server;

import net.minecraft.server.world.entity.EntityGhast;
import net.minecraft.server.world.entity.EntityPigZombie;

public class BiomeHell extends BiomeBase {

    public BiomeHell() {
        this.r = new Class[] { EntityGhast.class, EntityPigZombie.class};
        this.s = new Class[0];
    }
}
