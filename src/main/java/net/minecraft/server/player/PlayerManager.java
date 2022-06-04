package net.minecraft.server.player;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packet.Packet;
import net.minecraft.server.world.WorldServer;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private List<EntityPlayer> playerEntities = new ArrayList<>();
    private PlayerList playerList = new PlayerList();
    private List<PlayerInstance> players = new ArrayList<>();
    private MinecraftServer minecraftServer;
    private int dimension;

    public PlayerManager(MinecraftServer minecraftServer, int dimension) {
        this.minecraftServer = minecraftServer;
        this.dimension = dimension;
    }

    public WorldServer getWorld() {
        return this.minecraftServer.getWorldByDimension(this.dimension);
    }

    public void a() {
        for (int i = 0; i < this.players.size(); ++i) {
            this.players.get(i).a();
        }

        this.players.clear();
    }

    private PlayerInstance a(int i, int j, boolean flag) {
        long k = (long) i + 2147483647L | (long) j + 2147483647L << 32;
        PlayerInstance player = (PlayerInstance) this.playerList.a(k);

        if (player == null && flag) {
            player = new PlayerInstance(this, i, j);
            this.playerList.a(k, player);
        }

        return player;
    }

    public void a(Packet packet, int i, int j, int k) {
        int l = i >> 4;
        int i1 = k >> 4;
        PlayerInstance playerinstance = this.a(l, i1, false);

        if (playerinstance != null) {
            playerinstance.a(packet);
        }
    }

    public void a(int i, int j, int k) {
        int l = i >> 4;
        int i1 = k >> 4;
        PlayerInstance playerinstance = this.a(l, i1, false);

        if (playerinstance != null) {
            playerinstance.a(i & 15, j, k & 15);
        }
    }

    public void a(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;

        entityplayer.d = entityplayer.locX;
        entityplayer.e = entityplayer.locZ;

        for (int k = i - 10; k <= i + 10; ++k) {
            for (int l = j - 10; l <= j + 10; ++l) {
                this.a(k, l, true).a(entityplayer);
            }
        }

        this.playerEntities.add(entityplayer);
    }

    public void b(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;

        for (int k = i - 10; k <= i + 10; ++k) {
            for (int l = j - 10; l <= j + 10; ++l) {
                PlayerInstance playerinstance = this.a(k, l, false);

                if (playerinstance != null) {
                    playerinstance.b(entityplayer);
                }
            }
        }

        this.playerEntities.remove(entityplayer);
    }

    private boolean a(int i, int j, int k, int l) {
        int i1 = i - k;
        int j1 = j - l;

        return i1 >= -10 && i1 <= 10 ? j1 >= -10 && j1 <= 10 : false;
    }

    public void c(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;
        double d0 = entityplayer.d - entityplayer.locX;
        double d1 = entityplayer.e - entityplayer.locZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D) {
            int k = (int) entityplayer.d >> 4;
            int l = (int) entityplayer.e >> 4;
            int i1 = i - k;
            int j1 = j - l;

            if (i1 != 0 || j1 != 0) {
                for (int k1 = i - 10; k1 <= i + 10; ++k1) {
                    for (int l1 = j - 10; l1 <= j + 10; ++l1) {
                        if (!this.a(k1, l1, k, l)) {
                            this.a(k1, l1, true).a(entityplayer);
                        }

                        if (!this.a(k1 - i1, l1 - j1, i, j)) {
                            PlayerInstance playerinstance = this.a(k1 - i1, l1 - j1, false);

                            if (playerinstance != null) {
                                playerinstance.b(entityplayer);
                            }
                        }
                    }
                }

                entityplayer.d = entityplayer.locX;
                entityplayer.e = entityplayer.locZ;
            }
        }
    }

    public int b() { // ?
        return 144;
    }

    static PlayerList getPlayerList(PlayerManager playermanager) {
        return playermanager.playerList;
    }

    static List<PlayerInstance> getPlayerInstances(PlayerManager playermanager) {
        return playermanager.players;
    }
}
