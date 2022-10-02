package fr.bretzel.minestom.utils.system.os;

import fr.bretzel.minestom.utils.system.HWInfo;

public class Unknown extends HWInfo {
    @Override
    public int getCpuCores() {
        return 0;
    }

    @Override
    public int getCpuThread() {
        return 0;
    }

    @Override
    public String getCpuName() {
        return "Unknown";
    }

    @Override
    public long getSystemMaxMemory() {
        return 0;
    }

    @Override
    public long getSystemFreeMemory() {
        return 0;
    }

    @Override
    public long getSystemMemoryUsed() {
        return 0;
    }
}
