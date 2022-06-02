package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying {

    public Packet12PlayerLook() {
        this.i = true;
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.e = datainputstream.readFloat();
        this.f = datainputstream.readFloat();
        super.a(datainputstream);
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeFloat(this.e);
        dataoutputstream.writeFloat(this.f);
        super.a(dataoutputstream);
    }

    public int a() {
        return 9;
    }
}
