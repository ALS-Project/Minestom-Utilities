package fr.bretzel.minestom.utils.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

public abstract class HWInfo {
    private final Runtime runtime = Runtime.getRuntime();

    public abstract int getCpuCores();

    public abstract int getCpuThread();

    public abstract String getCpuName();

    public abstract long getSystemMaxMemory();

    public abstract long getSystemFreeMemory();

    public abstract long getSystemMemoryUsed();

    public long getSystemUsedMemory() {
        return getSystemMaxMemory() - getSystemFreeMemory();
    }

    public long getJVMMaxMemory() {
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
    }

    public long getFreeRuntimeMemory() {
        return runtime.freeMemory();
    }

    public long getTotalRuntimeMemory() {
        return runtime.totalMemory();
    }

    public long getMaxRuntimeMemory() {
        return runtime.maxMemory();
    }

    public long getUsedRuntimeMemory() {
        return getTotalRuntimeMemory() - getFreeRuntimeMemory();
    }

    public long getPid() {
        return ProcessHandle.current().pid();
    }

    public String getCommandLine() {
        return ProcessHandle.current().info().commandLine().orElse("Unknown command");
    }

    public String[] execute(String command) {
        Process process = null;
        try {
            process = runtime.exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ArrayList<String> result = new ArrayList<>();
            String s;
            while ((s = stdInput.readLine()) != null)
                if (s.length() > 0 && !s.isBlank())
                    result.add(s);

            return result.toArray(String[]::new);
        } catch (IOException e) {
            if (process != null)
                process.destroy();
            return new String[]{};
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}
