package fr.bretzel.minestom.utils;

import net.minestom.server.MinecraftServer;

public class Handler {
    public static void handle(Throwable e) {
        MinecraftServer.getExceptionManager().handleException(e);
    }
}
