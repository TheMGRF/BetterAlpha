package net.minecraft.server.packet;

import net.minecraft.server.item.ItemStack;
import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17AddToInventory extends Packet {

    public int a;
    public int b;
    public int c;

    public Packet17AddToInventory() {}

    public Packet17AddToInventory(ItemStack itemstack, int i) {
        this.a = itemstack.c;
        this.b = i;
        this.c = itemstack.d;
        if (i == 0) {
            boolean flag = true;
        }
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.a = datainputstream.readShort();
        this.b = datainputstream.readByte();
        this.c = datainputstream.readShort();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeShort(this.a);
        dataoutputstream.writeByte(this.b);
        dataoutputstream.writeShort(this.c);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 5;
    }
}
