package fr.bretzel.minestom.utils.system;

import fr.bretzel.minestom.utils.system.os.Linux;
import fr.bretzel.minestom.utils.system.os.OSX;
import fr.bretzel.minestom.utils.system.os.Unknown;
import fr.bretzel.minestom.utils.system.os.Windows;

import java.util.Locale;


public enum OperatingSystem {
    WINDOWS(new Windows()),
    OSX(new OSX()),
    SOLARIS,
    LINUX(new Linux()),
    UNKNOWN(new Unknown());

    private final HWInfo hwInfo;

    OperatingSystem() {
        this.hwInfo = new Unknown();
    }

    OperatingSystem(HWInfo hwInfo) {
        this.hwInfo = hwInfo;
    }

    public HWInfo getHwInfo() {
        return hwInfo;
    }

    public String getName() {
        return System.getProperty("os.name");
    }

    public static OperatingSystem getCurrent() {
        String os_name = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os_name.contains("win"))
            return OperatingSystem.WINDOWS;
        else if (os_name.contains("mac"))
            return OperatingSystem.OSX;
        else if (os_name.contains("solaris"))
            return OperatingSystem.SOLARIS;
        else if (os_name.contains("sunos"))
            return OperatingSystem.SOLARIS;
        else if (os_name.contains("linux"))
            return OperatingSystem.LINUX;
        else
            return os_name.contains("unix") ? OperatingSystem.LINUX : OperatingSystem.UNKNOWN;
    }
}
