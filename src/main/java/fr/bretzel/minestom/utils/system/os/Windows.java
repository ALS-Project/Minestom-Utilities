package fr.bretzel.minestom.utils.system.os;

import fr.bretzel.minestom.utils.system.HWInfo;

public class Windows extends HWInfo {
    private int cpu_cores = 0;
    private int cpu_threads = 0;
    private long max_system_memory;
    private String cpu_name = "";

    public Windows() {
        String[] cpus = execute("cmd /C WMIC CPU Get NumberOfCores, ThreadCount, Name /value");
        for (String val : cpus) {
            if (val != null && !val.isEmpty() && val.split("=").length > 1) {
                String[] keyAndValue = val.split("=");
                String key = keyAndValue[0].trim();
                String value = keyAndValue[1].trim();
                if (key.equalsIgnoreCase("Name")) {
                    cpu_name = value;
                } else if (key.equalsIgnoreCase("NumberOfCores")) {
                    cpu_cores = Integer.parseInt(value);
                } else if (key.equalsIgnoreCase("ThreadCount")) {
                    cpu_threads = Integer.parseInt(value);
                }
            }
        }

        String[] memories = execute("cmd /C WMIC MEMORYCHIP Get Capacity");

        for (String bytesMem : memories)
            if (bytesMem.trim().toLowerCase().matches("\\d+"))
                max_system_memory += Long.parseLong(bytesMem.trim().toLowerCase());
    }

    @Override
    public int getCpuCores() {
        return cpu_cores;
    }

    @Override
    public int getCpuThread() {
        return cpu_threads;
    }

    @Override
    public String getCpuName() {
        return cpu_name;
    }

    @Override
    public long getSystemMaxMemory() {
        return max_system_memory;
    }

    @Override
    public long getSystemFreeMemory() {
        return Long.parseLong(execute("cmd /C WMIC MEMORYCHIP Get FreePhysicalMemory")[1]);
    }

    @Override
    public long getSystemMemoryUsed() {
        return Long.parseLong(execute("cmd /C WMIC PROCESS WHERE processid=" + getPid() + " Get WorkingSetSize")[1].trim());
    }
}
