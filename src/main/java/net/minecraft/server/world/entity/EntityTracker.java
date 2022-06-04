package net.minecraft.server.world.entity;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packet.Packet;
import net.minecraft.server.world.entity.impl.*;

import java.util.*;

public class EntityTracker {

    private final Set<EntityTrackerEntry> a = new HashSet<>();
    private EntityList b = new EntityList();
    private MinecraftServer c;
    private int d;
    private int e;

    public EntityTracker(MinecraftServer minecraftserver, int i) {
        this.c = minecraftserver;
        this.e = i;
        this.d = minecraftserver.serverConfigurationManager.a();
    }

    public void addEntity(Entity entity) {
        if (entity instanceof EntityPlayer entityplayer) {
            this.addEntity(entity, 512, 2);

            for (EntityTrackerEntry entitytrackerentry : this.a) {
                if (entitytrackerentry.a != entityplayer) {
                    entitytrackerentry.a(entityplayer);
                }
            }
        } else if (entity instanceof EntityFishingHook) {
            this.addEntity(entity, 64, 20);
        } else if (entity instanceof EntityItem) {
            this.addEntity(entity, 64, 20);
        } else if (entity instanceof EntityMinecart) {
            this.addEntity(entity, 160, 4);
        } else if (entity instanceof EntityBoat) {
            this.addEntity(entity, 160, 4);
        } else if (entity instanceof IAnimal) {
            this.addEntity(entity, 160, 2);
        }
    }

    public void addEntity(Entity entity, int i, int j) {
    	if (entity instanceof EntityPlayer player) {
            MinecraftServer.LOGGER.info("Tracking player: " + player.username);
    	}

        if (i > this.d) {
            i = this.d;
        }

        if (this.b.b(entity.g)) {
            throw new IllegalStateException("Entity is already tracked!");
        } else {
            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, j);

            this.a.add(entitytrackerentry);
            this.b.a(entity.g, entitytrackerentry);
            entitytrackerentry.b(this.c.getWorldByDimension(this.e).players);
        }
    }

    public void removeEntity(Entity entity) {
        if (entity instanceof EntityPlayer entityplayer) {
            for (EntityTrackerEntry entry : this.a) {
                entry.a(entityplayer);
            }
        }
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.b.d(entity.g);

        if (entitytrackerentry != null) {
            this.a.remove(entitytrackerentry);
            entitytrackerentry.a();
        }
    }

    public void a() {
        List<EntityPlayer> arraylist = new ArrayList<>();
        for (EntityTrackerEntry entitytrackerentry : this.a) {
            entitytrackerentry.a(this.c.getWorldByDimension(this.e).players);
            if (entitytrackerentry.j && entitytrackerentry.a instanceof EntityPlayer) {
                arraylist.add((EntityPlayer) entitytrackerentry.a);
            }
        }

        for (EntityPlayer entityPlayer : arraylist) {
            for (EntityTrackerEntry entitytrackerentry1 : this.a) {
                if (entitytrackerentry1.a != entityPlayer) {
                    entitytrackerentry1.a(entityPlayer);
                }
            }
        }
    }

    public void a(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.b.a(entity.g);

        if (entitytrackerentry != null) {
            entitytrackerentry.a(packet);
        }
    }
}
