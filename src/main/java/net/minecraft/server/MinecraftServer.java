package net.minecraft.server;

import net.minecraft.server.command.ICommandListener;
import net.minecraft.server.command.ServerCommand;
import net.minecraft.server.gui.ServerGUI;
import net.minecraft.server.item.Item;
import net.minecraft.server.item.ItemStack;
import net.minecraft.server.network.NetworkListenThread;
import net.minecraft.server.network.ThreadCommandReader;
import net.minecraft.server.network.thread.ThreadServerApplication;
import net.minecraft.server.network.thread.ThreadSleepForever;
import net.minecraft.server.packet.Packet3Chat;
import net.minecraft.server.packet.Packet4UpdateTime;
import net.minecraft.server.player.IUpdatePlayerListBox;
import net.minecraft.server.utils.AxisAlignedBB;
import net.minecraft.server.utils.Vec3D;
import net.minecraft.server.utils.logger.ConsoleLogManager;
import net.minecraft.server.world.SecondaryWorldServer;
import net.minecraft.server.world.WorldManager;
import net.minecraft.server.world.WorldServer;
import net.minecraft.server.world.chunk.ChunkCoordinates;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.EntityTracker;
import net.minecraft.server.world.entity.impl.EntityHuman;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.awt.*;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftServer implements ICommandListener, Runnable {

    public static final Logger LOGGER = Logger.getLogger("Minecraft");
    public static final HashMap b = new HashMap();

    public NetworkListenThread c;
    public PropertyManager d;
    public WorldServer[] worlds;
    public ServerConfigurationManager serverConfigurationManager;
    private boolean m = true;
    public boolean g = false;
    int h = 0;
    public String i;
    public int j;
    private final List<IUpdatePlayerListBox> n = new ArrayList<>();
    private final List<ServerCommand> commands = Collections.synchronizedList(new ArrayList<>());
    public EntityTracker[] entityTrackers = new EntityTracker[2];
    public boolean l;
    public boolean spawnAnimals;

    public MinecraftServer() {
        new ThreadSleepForever(this);
    }

    private boolean d() throws UnknownHostException {
        ThreadCommandReader threadcommandreader = new ThreadCommandReader(this);

        threadcommandreader.setDaemon(true);
        threadcommandreader.start();
        ConsoleLogManager.a();
        LOGGER.info("Starting Minecraft server version Alpha 1.2.1_01 (0.2.3)");
        LOGGER.info("Nether additions by Moresteck: https://github.com/Moresteck");
        LOGGER.info("Cleanup and extras by BetterAlpha: https://github.com/TheMGRF/BetterAlpha");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            LOGGER.warning("**** NOT ENOUGH RAM!");
            LOGGER.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        LOGGER.info("Loading properties");
        this.d = new PropertyManager(new File("server.properties"));
        String s = this.d.a("server-ip", "");

        this.l = this.d.a("online-mode", false);
        this.spawnAnimals = this.d.a("spawn-animals", true);
        InetAddress inetaddress = null;

        if (s.length() > 0) {
            inetaddress = InetAddress.getByName(s);
        }

        int i = this.d.a("server-port", 25565);

        LOGGER.info("Starting Minecraft server on " + (s.length() == 0 ? "*" : s) + ":" + i);

        try {
            this.c = new NetworkListenThread(this, inetaddress, i);
        } catch (Throwable ioexception) {
            LOGGER.warning("**** FAILED TO BIND TO PORT!");
            LOGGER.log(Level.WARNING, "The exception was: " + ioexception.toString());
            LOGGER.warning("Perhaps a server is already running on that port?");
            return false;
        }

        this.serverConfigurationManager = new ServerConfigurationManager(this);
        this.entityTrackers[0] = new EntityTracker(this, 0);
        this.entityTrackers[1] = new EntityTracker(this, -1);
        String s1 = this.d.a("level-name", "world");
        String s2 = this.d.a("level-seed", "");
        long k = (new Random()).nextLong();

        if (s2.length() > 0) {
            try {
                k = Long.parseLong(s2);
            } catch (NumberFormatException numberformatexception) {
                k = s2.hashCode();
            }
        }

        this.c(s1, k);
        LOGGER.info("Done! For help, type \"help\" or \"?\"");
        return true;
    }

    private void c(String s, long i) {
        this.worlds = new WorldServer[2];

        for (int j = 0; j < this.worlds.length; ++j) {
            if (j == 0) {
                this.worlds[j] = new WorldServer(this, new File("."), s, j == 0 ? 0 : -1, i);
            } else {
                this.worlds[j] = new SecondaryWorldServer(this, new File("."), s + "_nether", j == 0 ? 0 : -1, i, this.worlds[0]);
            }

            this.worlds[j].a(new WorldManager(this, this.worlds[j]));
            this.worlds[j].k = this.d.a("spawn-monsters", true) ? 1 : 0;
            this.serverConfigurationManager.a(this.worlds);
        }

        short short1 = 196;
        long k = System.currentTimeMillis();

        for (int l = 0; l < this.worlds.length; ++l) {
            LOGGER.info("Preparing start region for level " + l + " - " + s + " (Seed: " + i + ")");
            if (l == 0 || this.d.a("allow-nether", true)) {
                WorldServer worldserver = this.worlds[l];
                ChunkCoordinates chunkcoordinates = worldserver.getSpawn();

                for (int i1 = -short1; i1 <= short1 && this.m; i1 += 16) {
                    for (int j1 = -short1; j1 <= short1 && this.m; j1 += 16) {
                        long k1 = System.currentTimeMillis();

                        if (k1 < k) {
                            k = k1;
                        }

                        if (k1 > k + 1000L) {
                            int l1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                            int i2 = (i1 + short1) * (short1 * 2 + 1) + j1 + 1;

                            this.a("Preparing spawn area", i2 * 100 / l1);
                            k = k1;
                        }

                        worldserver.A.d(chunkcoordinates.a + i1 >> 4, chunkcoordinates.b + j1 >> 4);

                        while (worldserver.d() && this.m) {
                            ;
                        }
                    }
                }
            }
        }

        this.e();
    }

    private void a(String s, int i) {
        this.i = s;
        this.j = i;
        System.out.println(s + ": " + i + "%");
    }

    private void e() {
        this.i = null;
        this.j = 0;
    }

    private void f() {
        LOGGER.info("Saving chunks");

        for (int i = 0; i < this.worlds.length; ++i) {
            WorldServer worldserver = this.worlds[i];

            worldserver.a(true, null);
            worldserver.h();
        }
    }

    private void g() {
        LOGGER.info("Stopping server");
        if (this.serverConfigurationManager != null) {
            this.serverConfigurationManager.d();
        }

        for (int i = 0; i < this.worlds.length; ++i) {
            WorldServer worldserver = this.worlds[i];

            if (worldserver != null) {
                this.f();
            }
        }
    }

    public void a() {
        this.m = false;
    }

    public void run() {
        try {
            if (this.d()) {
                long i = System.currentTimeMillis();
                long j = 0L;

                while (this.m) {
                    long k = System.currentTimeMillis();
                    long l = k - i;

                    if (l > 2000L) {
                        LOGGER.warning("Can't keep up! Did the system time change, or is the server overloaded?");
                        l = 2000L;
                    }

                    if (l < 0L) {
                        LOGGER.warning("Time ran backwards! Did the system time change?");
                        l = 0L;
                    }

                    j += l;
                    i = k;

                    while (j > 50L) {
                        j -= 50L;
                        this.h();
                    }

                    Thread.sleep(1L);
                }
            } else {
                while (this.m) {
                    this.b();

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.log(Level.SEVERE, "Unexpected exception", exception);

            while (this.m) {
                this.b();

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException interruptedexception1) {
                    interruptedexception1.printStackTrace();
                }
            }
        } finally {
            this.g();
            this.g = true;
            System.exit(0);
        }
    }

    private void h() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = b.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            int i = ((Integer) b.get(s)).intValue();

            if (i > 0) {
                b.put(s, Integer.valueOf(i - 1));
            } else {
                arraylist.add(s);
            }
        }

        int j;

        for (j = 0; j < arraylist.size(); ++j) {
            b.remove(arraylist.get(j));
        }

        AxisAlignedBB.a();
        Vec3D.a();
        ++this.h;

        // Tick worlds
        for (j = 0; j < this.worlds.length; ++j) {
            if (j == 0 || this.d.a("allow-nether", true)) {
                WorldServer worldserver = this.worlds[j];

                if (this.h % 20 == 0) {
                    this.serverConfigurationManager.a(new Packet4UpdateTime(worldserver.lastUpdate), worldserver.q.e);
                }

                // Spawn entities in world?
                worldserver.f();


                while (worldserver.d()) {
                    ;
                }

                // Tick entities and tile entities
                worldserver.c();
            }
        }
        this.c.a();
        this.serverConfigurationManager.b();

        for (j = 0; j < this.entityTrackers.length; ++j) {
            this.entityTrackers[j].a();
        }

        for (j = 0; j < this.n.size(); ++j) {
            this.n.get(j).a();
        }

        try {
            this.b();
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Unexpected exception while parsing console command", exception);
        }
    }

    public void a(String s, ICommandListener icommandlistener) {
        this.commands.add(new ServerCommand(s, icommandlistener));
    }

    public void b() {
        while (this.commands.size() > 0) {
            ServerCommand servercommand = this.commands.remove(0);
            String s = servercommand.a;
            ICommandListener icommandlistener = servercommand.b;
            String s1 = icommandlistener.c();

            if (!s.toLowerCase().startsWith("help") && !s.toLowerCase().startsWith("?")) {
                if (s.toLowerCase().equals("stop")) {
                    this.a(s1, "Stopping the server...");
                    for (int j = 0; j < this.serverConfigurationManager.players.size(); ++j) {
                        EntityPlayer player = this.serverConfigurationManager.players.get(j);
                        player.networkHandler.sendPacket(new Packet3Chat("Stopping the server..."));
                    }
                    this.m = false;
                } else if (s.toLowerCase().startsWith("save-all")) {
                    this.a(s1, "Forcing save..");
                    this.f();
                    this.a(s1, "Save complete.");
                } else if (s.toLowerCase().startsWith("save-off")) {
                    this.a(s1, "Disabling level saving..");
                    this.worlds[0].C = true;
                } else if (s.toLowerCase().startsWith("save-on")) {
                    this.a(s1, "Enabling level saving..");
                    this.worlds[0].C = false;
                } else if (s.toLowerCase().startsWith("clearentity")) {
                    int i = 0;
                    for (int j = 0; j < this.worlds[0].entities.size(); ++j) {
                        Entity e = this.worlds[0].entities.get(j);
                        if (!(e instanceof EntityHuman) || !(e instanceof EntityPlayer)) {
                            this.worlds[0].d(e);
                            i++;
                        }
                    }
                    this.a(s1, "Cleared " + i + " entities.");
                } else {
                    String s2;

                    if (s.toLowerCase().startsWith("op ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        this.serverConfigurationManager.setAdmin(s2);
                        this.a(s1, "Opping " + s2);
                        this.serverConfigurationManager.a(s2, "\u00A7eYou are now op!");
                    } else if (s.toLowerCase().startsWith("deop ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        this.serverConfigurationManager.removeAdmin(s2);
                        this.serverConfigurationManager.a(s2, "\u00A7eYou are no longer op!");
                        this.a(s1, "De-opping " + s2);
                    } else if (s.toLowerCase().startsWith("ban-ip ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        this.serverConfigurationManager.banIP(s2);
                        this.a(s1, "Banning ip " + s2);
                    } else if (s.toLowerCase().startsWith("pardon-ip ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        this.serverConfigurationManager.unbanIP(s2);
                        this.a(s1, "Pardoning ip " + s2);
                    } else {
                        EntityPlayer entityplayer;

                        if (s.toLowerCase().startsWith("ban ")) {
                            s2 = s.substring(s.indexOf(" ")).trim();
                            this.serverConfigurationManager.ban(s2);
                            this.a(s1, "Banning " + s2);
                            entityplayer = this.serverConfigurationManager.getPlayer(s2);
                            if (entityplayer != null) {
                                entityplayer.networkHandler.c("Banned by admin");
                            }
                        } else if (s.toLowerCase().startsWith("pardon ")) {
                            s2 = s.substring(s.indexOf(" ")).trim();
                            this.serverConfigurationManager.unban(s2);
                            this.a(s1, "Pardoning " + s2);
                        } else if (s.toLowerCase().startsWith("kick ")) {
                            s2 = s.substring(s.indexOf(" ")).trim();
                            entityplayer = null;

                            for (int i = 0; i < this.serverConfigurationManager.players.size(); ++i) {
                                EntityPlayer entityplayer1 = this.serverConfigurationManager.players.get(i);

                                if (entityplayer1.username.equalsIgnoreCase(s2)) {
                                    entityplayer = entityplayer1;
                                }
                            }

                            if (entityplayer != null) {
                                entityplayer.networkHandler.c("Kicked by admin");
                                this.a(s1, "Kicking " + entityplayer.username);
                            } else {
                                icommandlistener.b("Can't find user " + s2 + ". No kick.");
                            }
                        } else {
                            String[] astring;
                            EntityPlayer entityplayer2;

                            if (s.toLowerCase().startsWith("tp ")) {
                                astring = s.split(" ");
                                if (astring.length == 3) {
                                    entityplayer = this.serverConfigurationManager.getPlayer(astring[1]);
                                    entityplayer2 = this.serverConfigurationManager.getPlayer(astring[2]);
                                    if (entityplayer == null) {
                                        icommandlistener.b("Can't find user " + astring[1] + ". No tp.");
                                    } else if (entityplayer2 == null) {
                                        icommandlistener.b("Can't find user " + astring[2] + ". No tp.");
                                    } else {
                                        entityplayer.networkHandler.a(entityplayer2.locX, entityplayer2.locY, entityplayer2.locZ, entityplayer2.yaw, entityplayer2.pitch);
                                        this.a(s1, "Teleporting " + astring[1] + " to " + astring[2] + ".");
                                    }
                                } else {
                                    icommandlistener.b("Syntax error, please provice a source and a target.");
                                }
                            } else if (s.toLowerCase().startsWith("give ")) {
                                astring = s.split(" ");
                                if (astring.length != 3 && astring.length != 4) {
                                    return;
                                }

                                String s3 = astring[1];

                                entityplayer2 = this.serverConfigurationManager.getPlayer(s3);
                                if (entityplayer2 != null) {
                                    try {
                                        int j = Integer.parseInt(astring[2]);

                                        if (Item.c[j] != null) {
                                            this.a(s1, "Giving " + entityplayer2.username + " some " + j);
                                            int k = 1;

                                            if (astring.length > 3) {
                                                k = this.b(astring[3], 1);
                                            }

                                            if (k < 1) {
                                                k = 1;
                                            }

                                            if (k > 64) {
                                                k = 64;
                                            }

                                            entityplayer2.dropItem(new ItemStack(j, k));
                                        } else {
                                            icommandlistener.b("There's no item with id " + j);
                                        }
                                    } catch (NumberFormatException numberformatexception) {
                                        icommandlistener.b("There's no item with id " + astring[2]);
                                    }
                                } else {
                                    icommandlistener.b("Can't find user " + s3);
                                }
                            } else if (s.toLowerCase().startsWith("say ")) {
                                s = s.substring(s.indexOf(" ")).trim();
                                LOGGER.info("[" + s1 + "] " + s);
                                this.serverConfigurationManager.a(new Packet3Chat("\u00A7d[Server] " + s));
                            } else if (s.toLowerCase().startsWith("tell ")) {
                                astring = s.split(" ");
                                if (astring.length >= 3) {
                                    s = s.substring(s.indexOf(" ")).trim();
                                    s = s.substring(s.indexOf(" ")).trim();
                                    LOGGER.info("[" + s1 + "->" + astring[1] + "] " + s);
                                    s = "\u00A77" + s1 + " whispers " + s;
                                    LOGGER.info(s);
                                    if (!this.serverConfigurationManager.a(astring[1], new Packet3Chat(s))) {
                                        icommandlistener.b("There's no player by that name online.");
                                    }
                                }
                            } else {
                                icommandlistener.b("Unknown console command. Type \"help\" for help.");
                            }
                        }
                    }
                }
            } else {
                icommandlistener.b("To run the server without a gui, start it like this:");
                icommandlistener.b("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
                icommandlistener.b("Console commands:");
                icommandlistener.b("   help  or  ?               shows this message");
                icommandlistener.b("   kick <player>             removes a player from the server");
                icommandlistener.b("   ban <player>              bans a player from the server");
                icommandlistener.b("   pardon <player>           pardons a banned player so that they can connect again");
                icommandlistener.b("   ban-ip <ip>               bans an IP address from the server");
                icommandlistener.b("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
                icommandlistener.b("   op <player>               turns a player into an op");
                icommandlistener.b("   deop <player>             removes op status from a player");
                icommandlistener.b("   tp <player1> <player2>    moves one player to the same location as another player");
                icommandlistener.b("   give <player> <id> [num]  gives a player a resource");
                icommandlistener.b("   tell <player> <message>   sends a private message to a player");
                icommandlistener.b("   stop                      gracefully stops the server");
                icommandlistener.b("   save-all                  forces a server-wide level save");
                icommandlistener.b("   save-off                  disables terrain saving (useful for backup scripts)");
                icommandlistener.b("   save-on                   re-enables terrain saving");
                icommandlistener.b("   list                      lists all currently connected players");
                icommandlistener.b("   say <message>             broadcasts a message to all players");
            }
        }
    }

    private void a(String s, String s1) {
        String s2 = s + ": " + s1;

        this.serverConfigurationManager.i("\u00A77(" + s2 + ")");
        LOGGER.info(s2);
    }

    private int b(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            return i;
        }
    }

    public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
        this.n.add(iupdateplayerlistbox);
    }

    public static void main(String[] astring) {
        try {
            MinecraftServer minecraftserver = new MinecraftServer();

            if (!GraphicsEnvironment.isHeadless() && (astring.length <= 0 || !astring[0].equals("nogui"))) {
                ServerGUI.a(minecraftserver);
            }

            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Failed to start the minecraft server", exception);
        }
    }

    public File a(String s) {
        return new File(s);
    }

    public void b(String s) {
        LOGGER.info(s);
    }

    public String c() {
        return "CONSOLE";
    }

    public WorldServer getWorldByDimension(int dimension) {
        return dimension == -1 ? this.worlds[1] : this.worlds[0];
    }

    public EntityTracker b(int i) {
        return i == -1 ? this.entityTrackers[1] : this.entityTrackers[0];
    }

    public static boolean a(MinecraftServer minecraftserver) {
        return minecraftserver.m;
    }
}
