package net.minecraft.server.network.handler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ICommandListener;
import net.minecraft.server.item.ItemInWorldManager;
import net.minecraft.server.item.ItemStack;
import net.minecraft.server.network.NetworkManager;
import net.minecraft.server.packet.*;
import net.minecraft.server.utils.MathHelper;
import net.minecraft.server.utils.MovingObjectPosition;
import net.minecraft.server.utils.PortalTravelAgent;
import net.minecraft.server.world.WorldServer;
import net.minecraft.server.world.block.material.Material;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.impl.EntityItem;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.util.logging.Logger;

public class NetServerHandler extends NetHandler implements ICommandListener {

    public static Logger LOGGER = Logger.getLogger("Minecraft");
    public NetworkManager networkManager;
    public boolean c = false;
    private final MinecraftServer minecraftServer;
    private final EntityPlayer entityPlayer;
    private int f = 0;
    private double g;
    private double h;
    private double i;
    private boolean j = true;
    private ItemStack k = null;
    private boolean justPortaled = false;

    public NetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
        networkmanager.a(this);
        this.entityPlayer = entityplayer;
        entityplayer.networkHandler = this;
    }

    public void a() {
        this.networkManager.a();
        if (this.f++ % 20 == 0) {
            this.networkManager.sendPacket(new Packet0KeepAlive());
        }
    }

    public void c(String s) {
        this.networkManager.sendPacket(new Packet255KickDisconnect(s));
        this.networkManager.c();
        this.minecraftServer.serverConfigurationManager.c(this.entityPlayer);
        this.c = true;
    }

    @Override
    public void a(Packet7UseEntity packet7UseEntity) {
        LOGGER.info("Player " + packet7UseEntity.playerId + " interacted with entity " + packet7UseEntity.entityId + "!");
    }

    @Override
    public void a(Packet9Respawn packet9Respawn) {
        LOGGER.info("Received respawn packet!");
        this.minecraftServer.serverConfigurationManager.respawnPlayer(entityPlayer);
    }

    public void a(Packet10Flying packet10flying) {
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(this.entityPlayer.dimension);
        double d0;

        if (!this.j) {
            d0 = packet10flying.b - this.h;
            if (packet10flying.a == this.g && d0 * d0 < 0.01D && packet10flying.c == this.i) {
                this.j = true;
            }
        }

        if (this.j) {
            this.g = this.entityPlayer.locX;
            this.h = this.entityPlayer.locY;
            this.i = this.entityPlayer.locZ;
            d0 = this.entityPlayer.locX;
            double d1 = this.entityPlayer.locY;
            double d2 = this.entityPlayer.locZ;
            float f = this.entityPlayer.yaw;
            float f1 = this.entityPlayer.pitch;
            double d3;

            if (packet10flying.h) {
                d0 = packet10flying.a;
                d1 = packet10flying.b;
                d2 = packet10flying.c;
                d3 = packet10flying.d - packet10flying.b;
                if (d3 > 1.65D || d3 < 0.1D) {
                    //this.c("Illegal stance");
                    LOGGER.warning(this.entityPlayer.username + " had an illegal stance: " + d3);
                }

                this.entityPlayer.aj = packet10flying.d;
            }

            if (packet10flying.i) {
                f = packet10flying.e;
                f1 = packet10flying.f;
            }

            this.entityPlayer.k();
            this.entityPlayer.Q = 0.0F;
            this.entityPlayer.b(this.g, this.h, this.i, f, f1);
            d3 = d0 - this.entityPlayer.locX;
            double d4 = d1 - this.entityPlayer.locY;
            double d5 = d2 - this.entityPlayer.locZ;
            float f2 = 0.0625F;
            boolean flag = worldserver.a(this.entityPlayer, this.entityPlayer.boundingBox.b().e(f2, f2, f2)).size() == 0;

            this.entityPlayer.c(d3, d4, d5);
            d3 = d0 - this.entityPlayer.locX;
            d4 = d1 - this.entityPlayer.locY;
            if (d4 > -0.5D || d4 < 0.5D) {
                d4 = 0.0D;
            }

            d5 = d2 - this.entityPlayer.locZ;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;
            boolean flag1 = false;

            if (d6 > 0.0625D) {
                flag1 = true;
                LOGGER.warning(this.entityPlayer.username + " moved wrongly!");
                //this.c("You moved too quickly :( (Hacking?)");
            }

            this.entityPlayer.b(d0, d1, d2, f, f1);
            boolean flag2 = worldserver.a(this.entityPlayer, this.entityPlayer.boundingBox.b().e(f2, f2, f2)).size() == 0;

            if (flag && (flag1 || !flag2)) {
                this.a(this.g, this.h, this.i, f, f1);
                return;
            }

            this.entityPlayer.onGround = packet10flying.g;
            this.minecraftServer.serverConfigurationManager.b(this.entityPlayer);
        }
        Material m = worldserver.c((int) packet10flying.a, (int) packet10flying.b, (int) packet10flying.c);
        if (m != Material.x && this.justPortaled) {
            this.justPortaled = false;
            return;
        } else if (m == Material.x && !this.justPortaled) {
            this.justPortaled = true;
            tpToNether(this.entityPlayer);
        }
    }

    public void tpToNether(EntityPlayer player) {
        WorldServer world = this.minecraftServer.getWorldByDimension(player.dimension);
        byte b0;

        if (player.dimension == -1) {
            b0 = 0;
        } else {
            b0 = -1;
        }
        player.dimension = b0;
        WorldServer toTp = this.minecraftServer.getWorldByDimension(player.dimension);

        world.d(player);
        player.F = false;
        double d0 = player.locX;
        double d1 = player.locZ;
        double d2 = 8.0D;

        if (player.dimension == -1) {
            d0 /= d2;
            d1 /= d2;
            player.b(d0 + 3D, player.locY, d1, player.yaw, player.pitch);
            if (!player.F) {
                toTp.entityJoinedWorld(player, false);
            }
        } else {
            d0 *= d2;
            d1 *= d2;
            player.b(d0 + 3D, player.locY, d1, player.yaw, player.pitch);
            if (!player.F) {
                toTp.entityJoinedWorld(player, false);
            }
        }
        if (!player.F) {
            toTp.trackEntity(player);
            player.b(d0 + 3D, player.locY, d1, player.yaw, player.pitch);
            toTp.entityJoinedWorld(player, false);
            (new PortalTravelAgent()).a(toTp, player);
        }
        this.minecraftServer.serverConfigurationManager.ass(player);
        this.a(player.locX, player.locY, player.locZ, player.yaw, player.pitch);
        player.spawnIn(toTp);
        player.worldItems = new ItemInWorldManager(toTp);
        player.worldItems.entityHuman = player;
        for (int j = 0; j < world.b.size(); ++j) {
            Entity en = ((Entity) world.b.get(j));
            this.entityPlayer.networkHandler.sendPacket(new Packet29DestroyEntity(en.g));
        }
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        this.j = false;
        this.g = d0;
        this.h = d1;
        this.i = d2;
        this.entityPlayer.b(d0, d1, d2, f, f1);
        this.entityPlayer.networkHandler.sendPacket(new Packet13PlayerLookMove(d0, d1 + 1.6200000047683716D, d1, d2, f, f1, false));
    }

    public void a(Packet14BlockDig packet14blockdig) {
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(this.entityPlayer.dimension);
        this.entityPlayer.inventory.inventorySlots[this.entityPlayer.inventory.d] = this.k;
        //boolean flag = worldserver.B = worldserver.q.e != 0 || this.minecraftServer.f.g(this.e.ar);
        boolean flag1 = packet14blockdig.e == 0;

        if (packet14blockdig.e == 1) {
            flag1 = true;
        }

        if (flag1) {
            double d0 = this.entityPlayer.locY;

            this.entityPlayer.locY = this.entityPlayer.aj;
            MovingObjectPosition movingobjectposition = this.entityPlayer.a(4.0D, 1.0F);

            this.entityPlayer.locY = d0;
            if (movingobjectposition == null) {
                return;
            }

            if (movingobjectposition.b != packet14blockdig.a || movingobjectposition.c != packet14blockdig.b || movingobjectposition.d != packet14blockdig.c || movingobjectposition.e != packet14blockdig.d) {
                return;
            }
        }

        int i = packet14blockdig.a;
        int j = packet14blockdig.b;
        int k = packet14blockdig.c;
        int l = packet14blockdig.d;
        int i1 = (int) MathHelper.e((float) (i - worldserver.m));
        int j1 = (int) MathHelper.e((float) (k - worldserver.o));

        if (i1 > j1) {
            j1 = i1;
        }

        if (packet14blockdig.e == 0) {
            //System.out.println("break 0");
            this.entityPlayer.worldItems.a(i, j, k);
        } else if (packet14blockdig.e == 2) {
            //System.out.println("break 2");
            this.entityPlayer.worldItems.a();
        } else if (packet14blockdig.e == 1) {
            //System.out.println("break 1");
            this.entityPlayer.worldItems.a(i, j, k, l);
        } else if (packet14blockdig.e == 3) {
            double d1 = this.entityPlayer.locX - ((double) i + 0.5D);
            double d2 = this.entityPlayer.locY - ((double) j + 0.5D);
            double d3 = this.entityPlayer.locZ - ((double) k + 0.5D);
            double d4 = d1 * d1 + d2 * d2 + d3 * d3;

            if (d4 < 256.0D) {
                this.entityPlayer.networkHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
            }
        }

        worldserver.B = false;
    }

    public void a(Packet15Place packet15place) {
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(this.entityPlayer.dimension);
        boolean flag = worldserver.B = worldserver.q.e != 0 || this.minecraftServer.serverConfigurationManager.g(this.entityPlayer.username);
        int i = packet15place.b;
        int j = packet15place.c;
        int k = packet15place.d;
        int l = packet15place.e;
        int i1 = (int) MathHelper.e((float) (i - worldserver.m));
        int j1 = (int) MathHelper.e((float) (k - worldserver.o));

        if (i1 > j1) {
            j1 = i1;
        }

        if (j1 > 16 || flag) {
            ItemStack itemstack = packet15place.a >= 0 ? new ItemStack(packet15place.a) : null;

            this.entityPlayer.worldItems.a(this.entityPlayer, worldserver, itemstack, i, j, k, l);
        }

        this.entityPlayer.networkHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
        worldserver.B = false;
    }

    public void a(String s) {
        LOGGER.info(this.entityPlayer.username + " lost connection: " + s);
        this.minecraftServer.serverConfigurationManager.c(this.entityPlayer);
        this.c = true;
    }

    public void a(Packet packet) {
        LOGGER.warning(this.getClass() + " wasn't prepared to deal with a " + packet.getClass());
        this.c("Protocol error, unexpected packet");
    }

    public void sendPacket(Packet packet) {
        this.networkManager.sendPacket(packet);
    }

    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
/*    	if (packet16blockitemswitch.b >= 0 && packet16blockitemswitch.b <= 9) {
    		this.e.ak.d = packet16blockitemswitch.b;
    	} else {
    		
    	}*/

        int i = packet16blockitemswitch.b;

        this.entityPlayer.inventory.d = this.entityPlayer.inventory.inventorySlots.length - 1;
        if (i == 0) {
            this.k = null;
        } else {
            this.k = new ItemStack(i);
        }

        this.entityPlayer.inventory.inventorySlots[this.entityPlayer.inventory.d] = this.k;
        for (int j = 0; j < this.minecraftServer.entityTrackers.length; ++j) {
            this.minecraftServer.entityTrackers[j].a(this.entityPlayer, new Packet16BlockItemSwitch(this.entityPlayer.g, i));
        }
    }

    public void a(Packet21PickupSpawn packet21pickupspawn) {
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(this.entityPlayer.dimension);
        double d0 = (double) packet21pickupspawn.b / 32.0D;
        double d1 = (double) packet21pickupspawn.c / 32.0D;
        double d2 = (double) packet21pickupspawn.d / 32.0D;
        EntityItem entityitem = new EntityItem(worldserver, d0, d1, d2, new ItemStack(packet21pickupspawn.h, packet21pickupspawn.i));

        entityitem.motX = (double) packet21pickupspawn.e / 128.0D;
        entityitem.motY = (double) packet21pickupspawn.f / 128.0D;
        entityitem.motZ = (double) packet21pickupspawn.g / 128.0D;
        entityitem.c = 10;
        worldserver.trackEntity(entityitem);
    }

    public void a(Packet3Chat packet3chat) {
        String s = packet3chat.a;

        if (s.length() > 100) {
            this.c("Chat message too long");
        } else {
            s = s.trim();

            for (int i = 0; i < s.length(); ++i) {
                if (" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂\u00C7\u00FC\u00E9\u00E2\u00E4\u00E0\u00E5\u00E7\u00EA\u00EB\u00E8\u00EF\u00EE\u00EC\u00C4\u00C5\u00C9\u00E6\u00C6\u00F4\u00F6\u00F2\u00FB\u00F9\u00FF\u00D6\u00DC\u00F8\u00A3\u00D8\u00D7ƒ\u00E1\u00ED\u00F3\u00FA\u00F1\u00D1\u00AA\u00BA\u00BF\u00AE\u00AC\u00BD\u00BC\u00A1\u00AB\u00BB".indexOf(s.charAt(i)) < 0) {
                    this.c("Illegal characters in chat");
                    return;
                }
            }

            if (s.startsWith("/")) {
                this.d(s);
            } else {
                s = "<" + this.entityPlayer.username + "> " + s;
                LOGGER.info(s);
                this.minecraftServer.serverConfigurationManager.a(new Packet3Chat(s));
            }
        }
    }

    private void d(String s) {
        if (s.toLowerCase().startsWith("/me ")) {
            s = "* " + this.entityPlayer.username + " " + s.substring(s.indexOf(" ")).trim();
            LOGGER.info(s);
            this.minecraftServer.serverConfigurationManager.a(new Packet3Chat(s));
        } else if (s.toLowerCase().startsWith("/tell ")) {
            String[] astring = s.split(" ");

            if (astring.length >= 3) {
                s = s.substring(s.indexOf(" ")).trim();
                s = s.substring(s.indexOf(" ")).trim();
                s = "\u00A77" + this.entityPlayer.username + " whispers " + s;
                LOGGER.info(s + " to " + astring[1]);
                if (!this.minecraftServer.serverConfigurationManager.a(astring[1], new Packet3Chat(s))) {
                    this.sendPacket(new Packet3Chat("\u00A7cThere's no player by that name online."));
                }
            }
        } else if (s.toLowerCase().startsWith("/list")) {
            this.sendPacket(new Packet3Chat("\u00A7cConnected players\u00A7f: " + this.minecraftServer.serverConfigurationManager.c()));
        } else {
            String s1;

            if (this.minecraftServer.serverConfigurationManager.g(this.entityPlayer.username)) {
                s1 = s.substring(1);
                LOGGER.info(this.entityPlayer.username + " issued server command: " + s1);
                this.minecraftServer.a(s1, this);
            } else {
                s1 = s.substring(1);
                LOGGER.info(this.entityPlayer.username + " tried command: " + s1);
            }
        }
    }

    public void a(Packet18ArmAnimation packet18armanimation) {
        if (packet18armanimation.b == 1) {
            this.entityPlayer.E();
        }
    }

    public void a(Packet255KickDisconnect packet255kickdisconnect) {
        this.networkManager.a("Quitting");
    }

    public int b() {
        return this.networkManager.d();
    }

    public void b(String s) {
        this.sendPacket(new Packet3Chat("\u00A77" + s));
    }

    public String c() {
        return this.entityPlayer.username;
    }

    public void a(Packet5PlayerInventory packet5playerinventory) {
        if (packet5playerinventory.a == -1) {
            this.entityPlayer.inventory.inventorySlots = packet5playerinventory.b;
        }

        if (packet5playerinventory.a == -2) {
            this.entityPlayer.inventory.craftingSlots = packet5playerinventory.b;
        }

        if (packet5playerinventory.a == -3) {
            this.entityPlayer.inventory.armourSlots = packet5playerinventory.b;
        }
    }

    public void d() {
        this.networkManager.sendPacket(new Packet5PlayerInventory(-1, this.entityPlayer.inventory.inventorySlots));
        this.networkManager.sendPacket(new Packet5PlayerInventory(-2, this.entityPlayer.inventory.craftingSlots));
        this.networkManager.sendPacket(new Packet5PlayerInventory(-3, this.entityPlayer.inventory.armourSlots));
    }

    public void a(Packet59ComplexEntity packet59complexentity) {
        WorldServer worldserver = this.minecraftServer.getWorldByDimension(this.entityPlayer.dimension);
        if (packet59complexentity.e.d("x") == packet59complexentity.a) {
            if (packet59complexentity.e.d("y") == packet59complexentity.b) {
                if (packet59complexentity.e.d("z") == packet59complexentity.c) {
                    TileEntity tileentity = worldserver.k(packet59complexentity.a, packet59complexentity.b, packet59complexentity.c);

                    if (tileentity != null) {
                        try {
                            tileentity.a(packet59complexentity.e);
                        } catch (Exception exception) {
                        }

                        tileentity.c();
                    }
                }
            }
        }
    }
}
