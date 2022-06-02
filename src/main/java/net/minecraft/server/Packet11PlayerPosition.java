package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet11PlayerPosition extends Packet10Flying {

    public Packet11PlayerPosition() {
        this.h = true;
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.a = datainputstream.readDouble();
        this.b = datainputstream.readDouble();
        this.d = datainputstream.readDouble();
        this.c = datainputstream.readDouble();
        super.a(datainputstream);
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeDouble(this.a);
        dataoutputstream.writeDouble(this.b);
        dataoutputstream.writeDouble(this.d);
        dataoutputstream.writeDouble(this.c);
        super.a(dataoutputstream);
    }

    public int a() {
        return 33;
    }
}
