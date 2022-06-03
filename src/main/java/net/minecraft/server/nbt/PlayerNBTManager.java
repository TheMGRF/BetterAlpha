package net.minecraft.server.nbt;

import net.minecraft.server.world.entity.impl.EntityHuman;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import java.io.*;
import java.util.logging.Logger;

public class PlayerNBTManager {

    public static final Logger a = Logger.getLogger("Minecraft");
    private final File b;
    private final File c;
    private final File d;
    private final long e = System.currentTimeMillis();

    public PlayerNBTManager(File file1) {
        this.c = file1;
        this.b = new File(file1, "players");
        this.d = new File(file1, "data");
        this.d.mkdirs();
        this.c.mkdirs();
        b.mkdir();

        this.f();
    }

    private void f() {
        try {
            File file1 = new File(this.b, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

            try {
                dataoutputstream.writeLong(this.e);
            } finally {
                dataoutputstream.close();
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    public void a(EntityPlayer entityplayer) {
        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entityplayer.d(nbttagcompound);
            File file1 = new File(this.b, "_tmp_.dat");
            File file2 = new File(this.b, entityplayer.username + ".dat");

            CompressedStreamTools.a(nbttagcompound, (OutputStream) (new FileOutputStream(file1)));
            if (file2.exists()) {
                file2.delete();
            }

            file1.renameTo(file2);
        } catch (Exception exception) {
            a.warning("Failed to save player data for " + entityplayer.username);
        }
    }

    public void b(EntityPlayer entityplayer) {
        try {
            File file1 = new File(this.b, entityplayer.username + ".dat");

            if (file1.exists()) {
                NBTTagCompound nbttagcompound = CompressedStreamTools.a((InputStream) (new FileInputStream(file1)));

                if (nbttagcompound != null) {
                    entityplayer.e(nbttagcompound);
                }
            }
        } catch (Exception exception) {
            a.warning("Failed to load player data for " + entityplayer.username);
        }
    }

    public void w(EntityHuman entityhuman) {
        NBTTagCompound nbttagcompound = this.a(entityhuman.username);

        if (nbttagcompound != null) {
            entityhuman.e(nbttagcompound);
        }
    }

    public NBTTagCompound a(String s) {
        try {
            File file1 = new File(this.c, s + ".dat");

            if (file1.exists()) {
                return CompressedStreamTools.a((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            a.warning("Failed to load player data for " + s);
        }

        return null;
    }
}
