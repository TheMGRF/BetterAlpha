package net.minecraft.server.world.biome;

import net.minecraft.server.world.entity.impl.EntityGhast;
import net.minecraft.server.world.entity.impl.EntityPigZombie;

public class BiomeHell extends BiomeBase {

    public BiomeHell() {
        this.r = new Class[]{EntityGhast.class, EntityPigZombie.class};
        this.s = new Class[0];
    }
}
