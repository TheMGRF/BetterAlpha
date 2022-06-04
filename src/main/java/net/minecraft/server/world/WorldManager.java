package net.minecraft.server.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.entity.Entity;

public class WorldManager implements IWorldAccess {

    private MinecraftServer a;
    private WorldServer w;

    public WorldManager(MinecraftServer minecraftserver, WorldServer world) {
        this.a = minecraftserver;
        this.w = world;
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
    }

    public void a(Entity entity) {
        this.a.b(this.w.q.e).addEntity(entity);
    }

    public void b(Entity entity) {
        this.a.b(this.w.q.e).removeEntity(entity);
    }

    public void a(String s, double d0, double d1, double d2, float f, float f1) {
    }

    public void a(int i, int j, int k, int l, int i1, int j1) {
    }

    public void a() {
    }

    public void a(int i, int j, int k) {
        this.a.serverConfigurationManager.a(i, j, k, this.w.q.e);
    }

    public void a(String s, int i, int j, int k) {
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        this.a.serverConfigurationManager.a(i, j, k, tileentity);
    }
}
