package net.minecraft.server;

import net.minecraft.server.item.ItemInWorldManager;
import net.minecraft.server.nbt.PlayerNBTManager;
import net.minecraft.server.network.handler.NetLoginHandler;
import net.minecraft.server.packet.*;
import net.minecraft.server.player.PlayerManager;
import net.minecraft.server.world.WorldServer;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class ServerConfigurationManager {

    public static final Logger LOGGER = Logger.getLogger("Minecraft");

    public final List<EntityPlayer> players = new ArrayList<>();
    private final MinecraftServer minecraftServer;
    private final PlayerManager[] playersInWorld = new PlayerManager[2];
    private final int maxPlayers;
    private final Set<String> banned = new HashSet<>();
    private final Set<String> ipBanned = new HashSet<>();
    private final Set<String> admins = new HashSet<>();
    private final File bannedPlayers;
    private final File bannedIPs;
    private final File operators;
    private PlayerNBTManager l;

    public ServerConfigurationManager(MinecraftServer minecraftserver) {
        this.minecraftServer = minecraftserver;
        this.bannedPlayers = minecraftserver.a("banned-players.txt");
        this.bannedIPs = minecraftserver.a("banned-ips.txt");
        this.operators = minecraftserver.a("ops.txt");

        this.playersInWorld[0] = new PlayerManager(minecraftserver, 0);
        this.playersInWorld[1] = new PlayerManager(minecraftserver, -1);
        this.maxPlayers = minecraftserver.d.a("max-players", 20);
        this.loadBanList();
        this.loadIPBanList();
        this.loadAdmins();
        this.saveBanList();
        this.saveIPBanList();
        this.saveAdmins();
    }

    public void a(WorldServer[] aworldserver) {
        this.l = new PlayerNBTManager(aworldserver[0].t);
    }

    public void ass(EntityPlayer entityplayer) {
        this.playersInWorld[0].b(entityplayer);
        this.playersInWorld[1].b(entityplayer);
        this.a(entityplayer.dimension).a(entityplayer);
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(entityplayer.dimension);

        worldserver.A.d((int) entityplayer.locX >> 4, (int) entityplayer.locZ >> 4);
    }

    public int a() {
        return this.playersInWorld[0].b();
    }

    public void a(EntityPlayer entityplayer) {
        this.players.add(entityplayer);
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(entityplayer.world.q.e);

        worldserver.A.d((int) entityplayer.locX >> 4, (int) entityplayer.locZ >> 4);

        while (worldserver.a(entityplayer, entityplayer.boundingBox).size() != 0) {
            entityplayer.a(entityplayer.locX, entityplayer.locY + 1.0D, entityplayer.locZ);
        }

        worldserver.trackEntity(entityplayer);
        this.a(entityplayer.world.q.e).a(entityplayer);
    }

    private PlayerManager a(int i) {
        return i == -1 ? this.playersInWorld[1] : this.playersInWorld[0];
    }

    public void b(EntityPlayer entityplayer) {
        this.a(entityplayer.dimension).c(entityplayer);
    }

    public void pfd(EntityPlayer entityplayer) {
        this.l.w(entityplayer);
    }

    public void c(EntityPlayer entityplayer) {
        this.l.a(entityplayer);
        this.minecraftServer.getWorldByDimension(entityplayer.dimension).d(entityplayer);
        this.players.remove(entityplayer);
        this.a(entityplayer.dimension).b(entityplayer);
    }

    public EntityPlayer a(NetLoginHandler netloginhandler, String s, String s1) {
        if (this.banned.contains(s.trim().toLowerCase())) {
            netloginhandler.b("You are banned from this server!");
            return null;
        } else {
            String s2 = netloginhandler.networkManager.b().toString();

            s2 = s2.substring(s2.indexOf("/") + 1);
            s2 = s2.substring(0, s2.indexOf(":"));
            if (this.ipBanned.contains(s2)) {
                netloginhandler.b("Your IP address is banned from this server!");
                return null;
            } else if (this.players.size() >= this.maxPlayers) {
                netloginhandler.b("The server is full!");
                return null;
            } else {
                for (EntityPlayer player : this.players) {

                    if (player.username.equalsIgnoreCase(s)) {
                        netloginhandler.b("You logged in from another location!");
                    }
                }

                return new EntityPlayer(this.minecraftServer, this.minecraftServer.getWorldByDimension(0), s, new ItemInWorldManager(this.minecraftServer.getWorldByDimension(0)));
            }
        }
    }

    public EntityPlayer respawnPlayer(EntityPlayer player) {
        // TODO: Support for multi-world (nether?)
        this.minecraftServer.entityTrackers[0].removeEntity(player);
        //this.minecraftServer.entityTrackers[0].addEntity(player);
        this.playersInWorld[0].b(player);
        this.players.remove(player);

        WorldServer worldServer = this.minecraftServer.worlds[0];
        //worldServer.trackEntity()
        worldServer.d(player);

        // Make a new entity player
        EntityPlayer newPlayer = new EntityPlayer(this.minecraftServer, worldServer, player.username, new ItemInWorldManager(worldServer));
        newPlayer.g = player.g;
        newPlayer.networkHandler = player.networkHandler;

        worldServer.A.d((int) newPlayer.lastX >> 4, (int) newPlayer.lastZ >> 4);

        while (worldServer.a(newPlayer, newPlayer.boundingBox).size() != 0) {
            newPlayer.a(newPlayer.lastX, newPlayer.lastY + 1.0D, newPlayer.lastZ);
        }

        newPlayer.networkHandler.sendPacket(new Packet9Respawn());
        newPlayer.networkHandler.sendPacket(new Packet9Respawn());
        newPlayer.spawnIn(worldServer);
        newPlayer.dead = false;

        newPlayer.networkHandler.sendPacket(new Packet6SpawnPosition(worldServer.m, worldServer.n, worldServer.o));
        a(newPlayer); // TODO: Check if player is connected before re-add
        newPlayer.networkHandler.teleport(newPlayer.lastX, newPlayer.lastY, newPlayer.lastZ, newPlayer.lastYaw, newPlayer.lastPitch);
        newPlayer.networkHandler.updateInventory();
        minecraftServer.c.a(newPlayer.networkHandler);
        newPlayer.networkHandler.sendPacket(new Packet4UpdateTime(worldServer.lastUpdate));
        return newPlayer;
    }

    public void b() {
        for (PlayerManager playerManager : this.playersInWorld) {
            playerManager.a();
        }
    }

    // added int l
    public void a(int i, int j, int k, int l) {
        this.a(l).a(i, j, k);
    }

    public void a(Packet packet) {
        for (EntityPlayer player : this.players) {
            player.networkHandler.sendPacket(packet);
        }
    }

    public void a(Packet packet, int i) {
        for (EntityPlayer player : this.players) {
            if (player.dimension == i) {
                player.networkHandler.sendPacket(packet);
            }
        }
    }

    public String getPlayersHumanReadable() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < this.players.size(); ++i) {
            if (i > 0) {
                s.append(", ");
            }

            s.append(this.players.get(i).username);
        }

        return s.toString();
    }

    public void ban(String s) {
        this.banned.add(s.toLowerCase());
        this.saveBanList();
    }

    public void unban(String s) {
        this.banned.remove(s.toLowerCase());
        this.saveBanList();
    }

    private void loadBanList() {
        try {
            this.banned.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.bannedPlayers));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                this.banned.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to load ban list: " + exception);
        }
    }

    private void saveBanList() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.bannedPlayers, false));
            for (String s : this.banned) {
                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to save ban list: " + exception);
        }
    }

    public void banIP(String s) {
        this.ipBanned.add(s.toLowerCase());
        this.saveIPBanList();
    }

    public void unbanIP(String s) {
        this.ipBanned.remove(s.toLowerCase());
        this.saveIPBanList();
    }

    private void loadIPBanList() {
        try {
            this.ipBanned.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.bannedIPs));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                this.ipBanned.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void saveIPBanList() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.bannedIPs, false));
            for (String s : this.ipBanned) {
                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to save ip ban list: " + exception);
        }
    }

    public void setAdmin(String s) {
        this.admins.add(s.toLowerCase());
        this.saveAdmins();
    }

    public void removeAdmin(String s) {
        this.admins.remove(s.toLowerCase());
        this.saveAdmins();
    }

    private void loadAdmins() {
        try {
            this.admins.clear();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.operators));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                this.admins.add(s.trim().toLowerCase());
            }

            bufferedreader.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to load ip ban list: " + exception);
        }
    }

    private void saveAdmins() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.operators, false));

            for (String s : this.admins) {
                printwriter.println(s);
            }

            printwriter.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to save admin list: " + exception);
        }
    }

    public boolean isAdmin(String s) {
        return this.admins.contains(s.trim().toLowerCase());
    }

    public EntityPlayer getPlayer(String s) {
        for (EntityPlayer player : this.players) {
            if (player.username.equalsIgnoreCase(s)) {
                return player;
            }
        }

        return null;
    }

    public void sendMessage(String player, String message) {
        EntityPlayer entityplayer = this.getPlayer(player);
        if (entityplayer == null) return;

        entityplayer.networkHandler.sendPacket(new Packet3Chat(message));
    }

    public void broadcastToOperators(String message) {
        Packet3Chat packet3chat = new Packet3Chat(message);
        for (EntityPlayer player : this.players) {
            if (this.isAdmin(player.username)) {
                player.networkHandler.sendPacket(packet3chat);
            }
        }
    }

    public boolean trySendPacket(String s, Packet packet) {
        EntityPlayer entityplayer = this.getPlayer(s);

        if (entityplayer != null) {
            entityplayer.networkHandler.sendPacket(packet);
            return true;
        } else {
            return false;
        }
    }

    public void a(int i, int j, int k, TileEntity tileentity) {
        this.a(tileentity.world.q.e).a(new Packet59ComplexEntity(i, j, k, tileentity), i, j, k);
    }

    public void a(EntityPlayer entityplayer, WorldServer worldserver) {
        entityplayer.networkHandler.a(new Packet4UpdateTime(worldserver.lastUpdate));
    }

    public void d() {
        for (EntityPlayer player : this.players) {
            this.l.a(player);
        }
    }
}
