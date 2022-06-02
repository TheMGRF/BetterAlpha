package net.minecraft.server.gui;

import net.minecraft.server.MinecraftServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ServerWindowAdapter extends WindowAdapter {

    final MinecraftServer a;

    public ServerWindowAdapter(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
    }

    public void windowClosing(WindowEvent windowevent) {
        this.a.a();

        while (!this.a.g) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException interruptedexception) {
                interruptedexception.printStackTrace();
            }
        }

        System.exit(0);
    }
}
