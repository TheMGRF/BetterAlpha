package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet9Respawn extends Packet {

    public Packet9Respawn() {
    }

    @Override
    public void a(DataInputStream datainputstream) throws IOException {
    }

    @Override
    public void a(DataOutputStream dataoutputstream) throws IOException {
    }

    @Override
    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    @Override
    public int a() {
        return 0;
    }

}
