package fr.bretzel.minestom.utils;

import net.minestom.server.MinecraftServer;

import java.time.temporal.ChronoUnit;

public class Delayable {
    public void delay(Runnable runnable, int delay) {
        MinecraftServer.getSchedulerManager().buildTask(runnable).delay(delay, ChronoUnit.MILLIS).schedule();
    }
}
