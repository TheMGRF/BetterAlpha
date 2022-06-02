package net.minecraft.server.world.chunk;

public interface IChunkProvider {

    boolean a(int i, int j);

    Chunk b(int i, int j);

    void a(IChunkProvider ichunkprovider, int i, int j);

    boolean a(boolean flag, IProgressUpdate iprogressupdate);

    boolean a();

    boolean b();
}
