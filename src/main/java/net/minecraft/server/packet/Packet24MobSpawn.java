package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.world.entity.EntityTypes;
import net.minecraft.server.world.entity.impl.EntityLiving;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet24MobSpawn extends Packet {

    public int a;
    public byte b;
    public int c;
    public int d;
    public int e;
    public byte f;
    public byte g;

    public Packet24MobSpawn() {
    }

    public Packet24MobSpawn(EntityLiving entityliving) {
        this.a = entityliving.g;
        this.b = (byte) EntityTypes.a(entityliving);
        this.c = MathHelper.b(entityliving.locX * 32.0D);
        this.d = MathHelper.b(entityliving.locY * 32.0D);
        this.e = MathHelper.b(entityliving.locZ * 32.0D);
        this.f = (byte) ((int) (entityliving.yaw * 256.0F / 360.0F));
        this.g = (byte) ((int) (entityliving.pitch * 256.0F / 360.0F));
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.a = datainputstream.readInt();
        this.b = datainputstream.readByte();
        this.c = datainputstream.readInt();
        this.d = datainputstream.readInt();
        this.e = datainputstream.readInt();
        this.f = datainputstream.readByte();
        this.g = datainputstream.readByte();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeByte(this.b);
        dataoutputstream.writeInt(this.c);
        dataoutputstream.writeInt(this.d);
        dataoutputstream.writeInt(this.e);
        dataoutputstream.writeByte(this.f);
        dataoutputstream.writeByte(this.g);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 19;
    }
}
