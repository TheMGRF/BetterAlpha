package net.minecraft.server;

import net.minecraft.server.entity.Entity;

public class WorldManager implements IWorldAccess {

    private MinecraftServer a;
    private WorldServer w;

    public WorldManager(MinecraftServer minecraftserver, WorldServer world) {
        this.a = minecraftserver;
        this.w = world;
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {}

    public void a(Entity entity) {
    	this.a.b(this.w.q.e).a(entity);
    }

    public void b(Entity entity) {
    	this.a.b(this.w.q.e).b(entity);
    }

    public void a(String s, double d0, double d1, double d2, float f, float f1) {}

    public void a(int i, int j, int k, int l, int i1, int j1) {}

    public void a() {}

    public void a(int i, int j, int k) {
        this.a.f.a(i, j, k, this.w.q.e);
    }

    public void a(String s, int i, int j, int k) {}

    public void a(int i, int j, int k, TileEntity tileentity) {
        this.a.f.a(i, j, k, tileentity);
    }
}
