package net.minecraft.server;

import net.minecraft.server.network.NetworkManager;

public class ThreadMonitorConnection extends Thread {

    final NetworkManager a;

    public ThreadMonitorConnection(NetworkManager networkmanager) {
        this.a = networkmanager;
    }

    public void run() {
        try {
            Thread.sleep(2000L);
            if (NetworkManager.a(this.a)) {
                NetworkManager.f(this.a).interrupt();
                this.a.a("Connection closed");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
