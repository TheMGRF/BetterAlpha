package net.minecraft.server.command;

public class ServerCommand {

    public final String a;
    public final ICommandListener b;

    public ServerCommand(String s, ICommandListener icommandlistener) {
        this.a = s;
        this.b = icommandlistener;
    }
}
