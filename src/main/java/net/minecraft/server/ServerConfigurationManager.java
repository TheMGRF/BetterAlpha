package net.minecraft.server;

import net.minecraft.server.item.ItemInWorldManager;
import net.minecraft.server.nbt.PlayerNBTManager;
import net.minecraft.server.network.handler.NetLoginHandler;
import net.minecraft.server.packet.Packet;
import net.minecraft.server.packet.Packet3Chat;
import net.minecraft.server.packet.Packet4UpdateTime;
import net.minecraft.server.packet.Packet59ComplexEntity;
import net.minecraft.server.player.PlayerManager;
import net.minecraft.server.world.WorldServer;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class ServerConfigurationManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public List b = new ArrayList();
    private MinecraftServer c;
    private PlayerManager[] d = new PlayerManager[2];
    private int e;
    private Set f = new HashSet();
    private Set g = new HashSet();
    private Set h = new HashSet();
    private File i;
    private File j;
    private File k;
    private PlayerNBTManager l;

    public ServerConfigurationManager(MinecraftServer minecraftserver) {
        this.c = minecraftserver;
        this.i = minecraftserver.a("banned-players.txt");
        this.j = minecraftserver.a("banned-ips.txt");
        this.k = minecraftserver.a("ops.txt");

        this.d[0] = new PlayerManager(minecraftserver, 0);
        this.d[1] = new PlayerManager(minecraftserver, -1);
        this.e = minecraftserver.d.a("max-players", 20);
        this.e();
        this.g();
        this.i();
        this.f();
        this.h();
        this.j();
    }

    public void a(WorldServer[] aworldserver) {
        this.l = new PlayerNBTManager(aworldserver[0].t);
    }

    public void ass(EntityPlayer entityplayer) {
        this.d[0].b(entityplayer);
        this.d[1].b(entityplayer);
        this.a(entityplayer.dimension).a(entityplayer);
        WorldServer worldserver = this.c.a(entityplayer.dimension);

        worldserver.A.d((int) entityplayer.p >> 4, (int) entityplayer.r >> 4);
    }

    public int a() {
        return this.d[0].b();
    }

    public void a(EntityPlayer entityplayer) {
        this.b.add(entityplayer);
        WorldServer worldserver = this.c.a(entityplayer.world.q.e);

        worldserver.A.d((int) entityplayer.p >> 4, (int) entityplayer.r >> 4);

        while (worldserver.a(entityplayer, entityplayer.boundingBox).size() != 0) {
            entityplayer.a(entityplayer.p, entityplayer.q + 1.0D, entityplayer.r);
        }

        worldserver.a(entityplayer);
        this.a(entityplayer.world.q.e).a(entityplayer);
    }

    private PlayerManager a(int i) {
        return i == -1 ? this.d[1] : this.d[0];
    }

    public void b(EntityPlayer entityplayer) {
        this.a(entityplayer.dimension).c(entityplayer);
    }

    public void pfd(EntityPlayer entityplayer) {
        this.l.w(entityplayer);
    }

    public void c(EntityPlayer entityplayer) {
        this.l.a(entityplayer);
        this.c.a(entityplayer.dimension).d(entityplayer);
        this.b.remove(entityplayer);
        this.a(entityplayer.dimension).b(entityplayer);
    }

    public EntityPlayer a(NetLoginHandler netloginhandler, String s, String s1) {
        if (this.f.contains(s.trim().toLowerCase())) {
            netloginhandler.b("Banned!");
            return null;
        } else {
            String s2 = netloginhandler.b.b().toString();

            s2 = s2.substring(s2.indexOf("/") + 1);
            s2 = s2.substring(0, s2.indexOf(":"));
            if (this.g.contains(s2)) {
                netloginhandler.b("Your IP address is banned!");
                return null;
            } else if (this.b.size() >= this.e) {
                netloginhandler.b("Server full!");
                return null;
            } else {
                for (int i = 0; i < this.b.size(); ++i) {
                    EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

                    if (entityplayer.ar.equalsIgnoreCase(s)) {
                        netloginhandler.b("This name is already playing on the server!");
                    }
                }

                return new EntityPlayer(this.c, this.c.a(0), s, new ItemInWorldManager(this.c.a(0)));
            }
        }
    }

    public void b() {
        for (int i = 0; i < this.d.length; ++i) {
            this.d[i].a();
        }
    }

    // added int l
    public void a(int i, int j, int k, int l) {
        this.a(l).a(i, j, k);
    }

    public void a(Packet packet) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            entityplayer.a.b(packet);
        }
    }

    public void a(Packet packet, int i) {
        for (int j = 0; j < this.b.size(); ++j) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(j);

            if (entityplayer.dimension == i) {
                entityplayer.a.b(packet);
            }
        }
    }

    public String c() {
        String s = "";

        for (int i = 0; i < this.b.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayer) this.b.get(i)).ar;
        }

        return s;
    }

    public void a(String s) {
        this.f.add(s.toLowerCase());
        this.f();
    }

    public void b(String s) {
        this.f.remove(s.toLowerCase());
        this.f();
    }

    private void e() {
        try {
            this.f.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.i));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.f.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ban list: " + exception);
        }
    }

    private void f() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.i, false));
            Iterator iterator = this.f.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ban list: " + exception);
        }
    }

    public void c(String s) {
        this.g.add(s.toLowerCase());
        this.h();
    }

    public void d(String s) {
        this.g.remove(s.toLowerCase());
        this.h();
    }

    private void g() {
        try {
            this.g.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.j));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.g.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void h() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.j, false));
            Iterator iterator = this.g.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ip ban list: " + exception);
        }
    }

    public void e(String s) {
        this.h.add(s.toLowerCase());
        this.j();
    }

    public void f(String s) {
        this.h.remove(s.toLowerCase());
        this.j();
    }

    private void i() {
        try {
            this.h.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.k));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                this.h.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            a.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void j() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.k, false));
            Iterator iterator = this.h.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            a.warning("Failed to save ip ban list: " + exception);
        }
    }

    public boolean g(String s) {
        return this.h.contains(s.trim().toLowerCase());
    }

    public EntityPlayer h(String s) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (entityplayer.ar.equalsIgnoreCase(s)) {
                return entityplayer;
            }
        }

        return null;
    }

    public void a(String s, String s1) {
        EntityPlayer entityplayer = this.h(s);

        if (entityplayer != null) {
            entityplayer.a.b((Packet) (new Packet3Chat(s1)));
        }
    }

    public void i(String s) {
        Packet3Chat packet3chat = new Packet3Chat(s);

        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (this.g(entityplayer.ar)) {
                entityplayer.a.b((Packet) packet3chat);
            }
        }
    }

    public boolean a(String s, Packet packet) {
        EntityPlayer entityplayer = this.h(s);

        if (entityplayer != null) {
            entityplayer.a.b(packet);
            return true;
        } else {
            return false;
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        this.a(tileentity.a.q.e).a(new Packet59ComplexEntity(i, j, k, tileentity), i, j, k);
    }

    public void a(EntityPlayer entityplayer, WorldServer worldserver) {
        entityplayer.a.a(new Packet4UpdateTime(worldserver.lastUpdate));
    }

    public void d() {
        for (int i = 0; i < this.b.size(); ++i) {
            this.l.a((EntityPlayer) this.b.get(i));
        }
    }
}
