package net.minecraft.server.player;

import net.minecraft.server.packet.*;
import net.minecraft.server.world.WorldServer;
import net.minecraft.server.world.block.Block;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.chunk.ChunkCoordIntPair;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

class PlayerInstance {

    private final List<EntityPlayer> playersInChunk = new ArrayList<>();
    private final int chunkX;
    private final int chunkZ;
    private final ChunkCoordIntPair chunkCoordPair;
    private final short[] f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;

    final PlayerManager playerManager;

    public PlayerInstance(PlayerManager playermanager, int chunkX, int chunkZ) {
        this.playerManager = playermanager;
        this.f = new short[10];
        this.g = 0;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkCoordPair = new ChunkCoordIntPair(chunkX, chunkZ);
        playerManager.getWorld().A.d(chunkX, chunkZ);
    }

    public void a(EntityPlayer entityplayer) {
        if (this.playersInChunk.contains(entityplayer)) {
            new IllegalStateException("Failed to add player. " + entityplayer + " already is in chunk " + this.chunkX + ", " + this.chunkZ);
        } else {
            entityplayer.ai.add(this.chunkCoordPair);
            entityplayer.networkHandler.sendPacket(new Packet50PreChunk(this.chunkCoordPair.a, this.chunkCoordPair.b, true));
            this.playersInChunk.add(entityplayer);
            entityplayer.f.add(this.chunkCoordPair);
        }
    }

    public void b(EntityPlayer entityplayer) {
        if (!this.playersInChunk.contains(entityplayer)) {
            (new IllegalStateException("Failed to remove player. " + entityplayer + " isn't in chunk " + this.chunkX + ", " + this.chunkZ)).printStackTrace();
        } else {
            this.playersInChunk.remove(entityplayer);
            if (this.playersInChunk.size() == 0) {
                long i = (long) this.chunkX + 2147483647L | (long) this.chunkZ + 2147483647L << 32;

                PlayerManager.getPlayerList(this.playerManager).b(i);
                if (this.g > 0) {
                    PlayerManager.getPlayerInstances(this.playerManager).remove(this);
                }

                this.playerManager.getWorld().A.c(this.chunkX, this.chunkZ);
            }

            entityplayer.f.remove(this.chunkCoordPair);
            if (entityplayer.ai.contains(this.chunkCoordPair)) {
                entityplayer.networkHandler.sendPacket(new Packet50PreChunk(this.chunkX, this.chunkZ, false));
            }
        }
    }

    public void a(int i, int j, int k) {
        if (this.g == 0) {
            PlayerManager.getPlayerInstances(this.playerManager).add(this);
            this.h = this.i = i;
            this.j = this.k = j;
            this.l = this.m = k;
        }

        if (this.h > i) {
            this.h = i;
        }

        if (this.i < i) {
            this.i = i;
        }

        if (this.j > j) {
            this.j = j;
        }

        if (this.k < j) {
            this.k = j;
        }

        if (this.l > k) {
            this.l = k;
        }

        if (this.m < k) {
            this.m = k;
        }

        if (this.g < 10) {
            short short1 = (short) (i << 12 | k << 8 | j);

            for (int l = 0; l < this.g; ++l) {
                if (this.f[l] == short1) {
                    return;
                }
            }

            this.f[this.g++] = short1;
        }
    }

    public void a(Packet packet) {
        for (int i = 0; i < this.playersInChunk.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.playersInChunk.get(i);

            if (entityplayer.ai.contains(this.chunkCoordPair)) {
                entityplayer.networkHandler.sendPacket(packet);
            }
        }
    }

    public void a() {
        WorldServer world = this.playerManager.getWorld();
        if (this.g != 0) {
            int i;
            int j;
            int k;

            if (this.g == 1) {
                i = this.chunkX * 16 + this.h;
                j = this.j;
                k = this.chunkZ * 16 + this.l;
                this.a(new Packet53BlockChange(i, j, k, world));
                if (Block.q[world.a(i, j, k)]) {
                    this.a(new Packet59ComplexEntity(i, j, k, world.k(i, j, k)));
                }
            } else {
                int l;

                if (this.g == 10) {
                    this.j = this.j / 2 * 2;
                    this.k = (this.k / 2 + 1) * 2;
                    i = this.h + this.chunkX * 16;
                    j = this.j;
                    k = this.l + this.chunkZ * 16;
                    l = this.i - this.h + 1;
                    int i1 = this.k - this.j + 2;
                    int j1 = this.m - this.l + 1;

                    this.a(new Packet51MapChunk(i, j, k, l, i1, j1, world));
                    List list = world.d(i, j, k, i + l, j + i1, k + j1);

                    for (int k1 = 0; k1 < list.size(); ++k1) {
                        TileEntity tileentity = (TileEntity) list.get(k1);

                        this.a(new Packet59ComplexEntity(tileentity.x, tileentity.y, tileentity.z, tileentity));
                    }
                } else {
                    this.a(new Packet52MultiBlockChange(this.chunkX, this.chunkZ, this.f, this.g, world));

                    for (i = 0; i < this.g; ++i) {
                        j = this.chunkX * 16 + (this.g >> 12 & 15);
                        k = this.g & 255;
                        l = this.chunkZ * 16 + (this.g >> 8 & 15);
                        if (Block.q[world.a(j, k, l)]) {
                            this.a(new Packet59ComplexEntity(j, k, l, world.k(j, k, l)));
                        }
                    }
                }
            }

            this.g = 0;
        }
    }
}
