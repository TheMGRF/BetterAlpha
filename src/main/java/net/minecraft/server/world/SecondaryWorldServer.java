package net.minecraft.server.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.WorldServer;

import java.io.File;

public class SecondaryWorldServer extends WorldServer {

    public SecondaryWorldServer(MinecraftServer minecraftserver, File file, String s, int i, long j, WorldServer worldserver) {
        super(minecraftserver, file, s, i, j);
        this.z = worldserver.z;
    }
}
