package net.minecraft.server.world.chunk;

import net.minecraft.server.world.World;

public interface IChunkLoader {

    Chunk a(World world, int i, int j);

    void a(World world, Chunk chunk);

    void b(World world, Chunk chunk);

    void a();

    void b();
}
