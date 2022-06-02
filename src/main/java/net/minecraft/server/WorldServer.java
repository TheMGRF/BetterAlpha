package net.minecraft.server;

import net.minecraft.server.world.chunk.ChunkLoader;
import net.minecraft.server.world.chunk.ChunkProviderServer;
import net.minecraft.server.world.chunk.IChunkProvider;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.EntityAnimal;
import net.minecraft.server.world.entity.EntityHuman;

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
            entity.l();
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

    public List d(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = new ArrayList();

        for (int k1 = 0; k1 < this.c.size(); ++k1) {
            TileEntity tileentity = (TileEntity) this.c.get(k1);

            if (tileentity.b >= i && tileentity.c >= j && tileentity.d >= k && tileentity.b < l && tileentity.c < i1 && tileentity.d < j1) {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }
}
