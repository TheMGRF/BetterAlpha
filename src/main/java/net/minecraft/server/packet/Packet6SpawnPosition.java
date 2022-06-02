package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet6SpawnPosition extends Packet {

    public int a;
    public int b;
    public int c;

    public Packet6SpawnPosition() {
    }

    public Packet6SpawnPosition(int i, int j, int k) {
        this.a = i;
        this.b = j;
        this.c = k;
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.a = datainputstream.readInt();
        this.b = datainputstream.readInt();
        this.c = datainputstream.readInt();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeInt(this.b);
        dataoutputstream.writeInt(this.c);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 12;
    }
}
