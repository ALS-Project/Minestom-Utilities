package fr.bretzel.minestom.utils.system.os;

import fr.bretzel.minestom.utils.system.HWInfo;

import java.util.stream.Stream;

public class Linux extends HWInfo {

    private final int cpu_cores;
    private final int cpu_threads;
    private final long max_system_memory;
    private final String cpu_name;

    public Linux() {
        String[] cpus = execute("cat /proc/cpuinfo");

        cpu_cores = Stream.of(cpus)
                .filter(s -> s.trim().toLowerCase().startsWith("cpu cores"))
                .map(s -> Integer.parseInt(s.trim().toLowerCase().split(":")[1].trim()))
                .findAny()
                .orElse(0);

        cpu_threads = (int) Stream.of(cpus).filter(s -> s.trim().toLowerCase().startsWith("processor")).count();

        cpu_name = Stream.of(cpus)
                .filter(s -> s.trim().toLowerCase().startsWith("model name"))
                .map(s -> s.trim().split(":")[1].trim())
                .findAny()
                .orElse("Linux Unknown CPU");

        max_system_memory = Stream.of(execute("grep MemTotal /proc/meminfo"))
                .filter(s -> s.trim().toLowerCase().startsWith("memtotal"))
                .map(s -> Long.parseLong(s.split(":")[1].replaceAll("[^\\d.]", "")))
                .findAny()
                .orElse(0L) * 1000;
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
        return Stream.of(execute("grep MemFree /proc/meminfo"))
                .filter(s -> s.trim().toLowerCase().startsWith("memfree"))
                .map(s -> Long.parseLong(s.split(":")[1].replaceAll("[^\\d.]", "")))
                .findAny()
                .orElse(0L) * 1000;
    }

    @Override
    public long getSystemMemoryUsed() {
        //TODO
        return 0;
    }
}
