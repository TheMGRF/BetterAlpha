package net.minecraft.server.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.chunk.ChunkLoader;
import net.minecraft.server.world.chunk.ChunkProviderServer;
import net.minecraft.server.world.chunk.IChunkProvider;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.EntityAnimal;
import net.minecraft.server.world.entity.impl.EntityHuman;
import net.minecraft.server.world.provider.WorldProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldServer extends World {

    public ChunkProviderServer A;
    public boolean B = false;
    public boolean C;
    public MinecraftServer server;

    public WorldServer(MinecraftServer x, File file1, String s, int i, long j) {
        super(file1, s, j, WorldProvider.a(i));
        this.server = x;
    }

    public void entityJoinedWorld(Entity entity, boolean flag) {
        if (!this.server.spawnAnimals && (entity instanceof EntityAnimal)) {
            entity.die();
        }

        if (entity.j == null || !(entity.j instanceof EntityHuman)) {
            super.entityJoinedWorld(entity, flag);
        }
    }

    public void f() {
        super.f();
    }

    protected IChunkProvider a(File file1) {
        this.A = new ChunkProviderServer(this, new ChunkLoader(file1, true), this.q.c());
        return this.A;
    }

    public List<TileEntity> d(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        List<TileEntity> tileEntitiesInRegion = new ArrayList<>();

        this.tileEntities.forEach(tileEntity -> {
            if (tileEntity.x >= h && tileEntity.y >= j && tileEntity.z >= k && tileEntity.x < minX && tileEntity.y < minY && tileEntity.z < minZ) {
                tileEntitiesInRegion.add(tileEntity);
            }
        });

        return tileEntitiesInRegion;
    }
}
