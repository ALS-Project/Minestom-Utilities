package fr.bretzel.minestom.utils.system;

public class SystemUtils {
    private static final OperatingSystem operatingSystem;
    private static final String CPU_NAME;
    private static final int CPU_CORE;
    private static final int CPU_THREAD;
    private static final long SYSTEM_MAX_MEMORY;
    private static final String SYSTEM_ARCH;
    private static final String JAVA_VERSION;
    private static final String JAVA_VENDOR;
    private static final String OS_NAME;
    private static final String OS_VERSION;

    static {
        operatingSystem = OperatingSystem.getCurrent();
        CPU_NAME = operatingSystem.getHwInfo().getCpuName();
        CPU_CORE = operatingSystem.getHwInfo().getCpuCores();
        CPU_THREAD = operatingSystem.getHwInfo().getCpuThread();
        SYSTEM_MAX_MEMORY = operatingSystem.getHwInfo().getSystemMaxMemory();
        SYSTEM_ARCH = System.getProperty("os.arch");
        OS_NAME = System.getProperty("os.name");
        OS_VERSION = System.getProperty("os.version");
        JAVA_VERSION = System.getProperty("java.version");
        JAVA_VENDOR = System.getProperty("java.vendor");
    }

    public static long getSystemMaxMemory() {
        return SYSTEM_MAX_MEMORY;
    }

    public static long getSystemFreeMemory() {
        return operatingSystem.getHwInfo().getSystemFreeMemory();
    }

    public static long getSystemMemoryUsed() {
        return operatingSystem.getHwInfo().getSystemMemoryUsed();
    }

    public static long getTotalSystemUsedMemory() {
        return operatingSystem.getHwInfo().getSystemUsedMemory();
    }

    public static long getJVMMaxMemory() {
        return operatingSystem.getHwInfo().getJVMMaxMemory();
    }

    public static long getFreeRuntimeMemory() {
        return operatingSystem.getHwInfo().getFreeRuntimeMemory();
    }

    public static long getTotalRuntimeMemory() {
        return operatingSystem.getHwInfo().getTotalRuntimeMemory();
    }

    public static long getMaxRuntimeMemory() {
        return operatingSystem.getHwInfo().getMaxRuntimeMemory();
    }

    public static long getUsedRuntimeMemory() {
        return operatingSystem.getHwInfo().getUsedRuntimeMemory();
    }

    public static long getPid() {
        return operatingSystem.getHwInfo().getPid();
    }

    public static String getCommandLine() {
        return operatingSystem.getHwInfo().getCommandLine();
    }

    public static int getCpuThread() {
        return CPU_THREAD;
    }

    public static int getCpuCore() {
        return CPU_CORE;
    }

    public static String getCpuName() {
        return CPU_NAME;
    }

    public static String getJavaVendor() {
        return JAVA_VENDOR;
    }

    public static String getJavaVersion() {
        return JAVA_VERSION;
    }

    public static String getOsName() {
        return OS_NAME;
    }

    public static String getOsVersion() {
        return OS_VERSION;
    }

    public static String getSystemArch() {
        return SYSTEM_ARCH;
    }

    public static OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }
}
