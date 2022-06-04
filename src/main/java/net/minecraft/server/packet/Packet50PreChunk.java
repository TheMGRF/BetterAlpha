package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet50PreChunk extends Packet {

    public int x;
    public int z;
    public boolean loaded;

    public Packet50PreChunk() {
    }

    public Packet50PreChunk(int x, int z, boolean loaded) {
        this.j = true;
        this.x = x;
        this.z = z;
        this.loaded = loaded;
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.x = datainputstream.readInt();
        this.z = datainputstream.readInt();
        this.loaded = datainputstream.read() != 0;
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeInt(this.x);
        dataoutputstream.writeInt(this.z);
        dataoutputstream.write(this.loaded ? 1 : 0);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 9;
    }
}
