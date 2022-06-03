package net.minecraft.server.player;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.entity.impl.EntityPlayer;

import javax.swing.*;
import java.util.Vector;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {

    private MinecraftServer a;
    private int b = 0;

    public PlayerListBox(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
        minecraftserver.a((IUpdatePlayerListBox) this);
    }

    public void a() {
        if (this.b++ % 20 == 0) {
            Vector vector = new Vector();

            for (int i = 0; i < this.a.f.b.size(); ++i) {
                vector.add(((EntityPlayer) this.a.f.b.get(i)).username);
            }

            this.setListData(vector);
        }
    }
}
