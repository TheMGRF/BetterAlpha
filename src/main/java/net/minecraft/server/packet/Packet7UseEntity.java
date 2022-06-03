package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet7UseEntity extends Packet {

    public int playerId;
    public int entityId;
    public boolean leftClick;

    public Packet7UseEntity() {
    }

    public Packet7UseEntity(int playerId, int entityId, boolean leftClick) {
        this.playerId = playerId;
        this.entityId = entityId;
        this.leftClick = leftClick;
    }

    @Override
    public void a(DataInputStream datainputstream) throws IOException {
        this.playerId = datainputstream.readInt();
        this.entityId = datainputstream.readInt();
        this.leftClick = datainputstream.readBoolean();
    }

    @Override
    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeInt(this.playerId);
        dataoutputstream.writeInt(this.entityId);
        dataoutputstream.writeBoolean(this.leftClick);
    }

    @Override
    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    @Override
    public int a() {
        return 10; // TODO: IDK (maybe size in bytes)
    }
}
