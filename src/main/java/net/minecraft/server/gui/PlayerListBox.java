package net.minecraft.server.gui;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.player.IUpdatePlayerListBox;

import javax.swing.*;
import java.util.Vector;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {

    private final MinecraftServer a;
    private int b = 0;

    public PlayerListBox(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
        minecraftserver.a(this);
    }

    public void a() {
        if (this.b++ % 20 == 0) {
            Vector vector = new Vector();

            for (int i = 0; i < this.a.serverConfigurationManager.players.size(); ++i) {
                vector.add(this.a.serverConfigurationManager.players.get(i).username);
            }

            this.setListData(vector);
        }
    }
}
