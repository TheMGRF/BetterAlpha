package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet8UpdateHealth extends Packet {

    public int health;

    public Packet8UpdateHealth() {
    }

    public Packet8UpdateHealth(int health) {
        this.health = health;
    }

    @Override
    public void a(DataInputStream datainputstream) throws IOException {
        this.health = datainputstream.readByte();
    }

    @Override
    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeByte(this.health);
    }

    @Override
    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    @Override
    public int a() {
        return 1;
    }
}
