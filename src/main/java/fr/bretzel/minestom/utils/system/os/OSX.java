package fr.bretzel.minestom.utils.system.os;

import fr.bretzel.minestom.utils.system.HWInfo;

public class OSX extends HWInfo {
    @Override
    public int getCpuCores() {
        return Integer.parseInt(execute("sysctl -n machdep.cpu.cores_per_package")[0].trim());
    }

    @Override
    public int getCpuThread() {
        return getCpuCores() + Integer.parseInt(execute("sysctl -n machdep.cpu.logical_per_package")[0].trim());
    }

    @Override
    public String getCpuName() {
        return execute("sysctl -n machdep.cpu.brand_string")[0].trim();
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
