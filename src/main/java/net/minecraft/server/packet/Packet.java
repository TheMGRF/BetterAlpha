package net.minecraft.server.packet;

import net.minecraft.server.network.handler.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Packet {

    private static final Map<Integer, Class<? extends Packet>> ID_TO_PACKET = new HashMap<>();
    private static final Map<Class<? extends Packet>, Integer> PACKET_TO_ID = new HashMap<>();
    public boolean j = false;

    public Packet() {
    }

    static void register(int id, Class<? extends Packet> packet) {
        if (ID_TO_PACKET.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate packet id:" + id);
        } else if (PACKET_TO_ID.containsKey(packet)) {
            throw new IllegalArgumentException("Duplicate packet class:" + packet);
        } else {
            ID_TO_PACKET.put(id, packet);
            PACKET_TO_ID.put(packet, id);
        }
    }

    public static Packet a(int i) {
        try {
            Class oclass = (Class) ID_TO_PACKET.get(Integer.valueOf(i));

            return oclass == null ? null : (Packet) oclass.newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Skipping packet with id " + i);
            return null;
        }
    }

    public final int b() {
        return ((Integer) PACKET_TO_ID.get(this.getClass())).intValue();
    }

    public static Packet b(DataInputStream datainputstream) throws IOException {
        int i = datainputstream.read();

        if (i == -1) {
            return null;
        } else {
            Packet packet = a(i);

            if (packet == null) {
                throw new IOException("Bad packet id " + i);
            } else {
                packet.a(datainputstream);
                return packet;
            }
        }
    }

    public static void a(Packet packet, DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.write(packet.b());
        packet.a(dataoutputstream);
    }

    public abstract void a(DataInputStream datainputstream) throws IOException;

    public abstract void a(DataOutputStream dataoutputstream) throws IOException;

    public abstract void a(NetHandler nethandler);

    public abstract int a();

    static {
        register(0, Packet0KeepAlive.class);
        register(1, Packet1Login.class);
        register(2, Packet2Handshake.class);
        register(3, Packet3Chat.class);
        register(4, Packet4UpdateTime.class);
        register(5, Packet5PlayerInventory.class);
        register(6, Packet6SpawnPosition.class);
        register(7, Packet7UseEntity.class);
        register(8, Packet8UpdateHealth.class);
        register(9, Packet9Respawn.class);
        register(10, Packet10Flying.class);
        register(11, Packet11PlayerPosition.class);
        register(12, Packet12PlayerLook.class);
        register(13, Packet13PlayerLookMove.class);
        register(14, Packet14BlockDig.class);
        register(15, Packet15Place.class);
        register(16, Packet16BlockItemSwitch.class);
        register(17, Packet17AddToInventory.class);
        register(18, Packet18ArmAnimation.class);
        register(20, Packet20NamedEntitySpawn.class);
        register(21, Packet21PickupSpawn.class);
        register(22, Packet22Collect.class);
        register(23, Packet23VehicleSpawn.class);
        register(24, Packet24MobSpawn.class);
        register(29, Packet29DestroyEntity.class);
        register(30, Packet30Entity.class);
        register(31, Packet31RelEntityMove.class);
        register(32, Packet32EntityLook.class);
        register(33, Packet33RelEntityMoveLook.class);
        register(34, Packet34EntityTeleport.class);
        register(50, Packet50PreChunk.class);
        register(51, Packet51MapChunk.class);
        register(52, Packet52MultiBlockChange.class);
        register(53, Packet53BlockChange.class);
        register(59, Packet59ComplexEntity.class);
        register(255, Packet255KickDisconnect.class);
    }
}
