package fr.bretzel.minestom.utils.math;

import net.minestom.server.coordinate.Vec;

public class MathsUtils {
    public static double trunc(double value) {
        if (value < 0) {
            return Math.ceil(value);
        } else {
            return Math.floor(value);
        }
    }

    public static float range(float var0, float var1, float var2) {
        if (var0 < var1) {
            return var1;
        } else {
            return Math.min(var0, var2);
        }
    }

    public static int floor(double num) {
        int floor = (int) num;
        return (double) floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public static double toMiB(long value) {
        return (value / 1.049e+6);
    }

    public static int signum(double x) {
        if (x == 0.0D) {
            return 0;
        } else {
            return x > 0.0D ? 1 : -1;
        }
    }

    public static double lerp(double pct, double start, double end) {
        return start + pct * (end - start);
    }

    public static double frac(double number) {
        return number - (double) lfloor(number);
    }

    public static long lfloor(double value) {
        long i = (long) value;
        return value < (double) i ? i - 1L : i;
    }

    public static Vec normalizeZeros(Vec vector) {
        double x, y, z;
        x = vector.x();
        y = vector.y();
        z = vector.z();

        if (x == -0.0D) {
            x = 0.0D;
        }

        if (y == -0.0D) {
            y = 0.0D;
        }

        if (z == -0.0D) {
            z = 0.0D;
        }

        return new Vec(x, y, z);
    }

    public static long getOffsetCoordinate(int x, int y, int z) {
        long i = (x * 3129871L) ^ (long) z * 116129781L ^ (long) y;
        i = i * i * 42317861L + i * 11L;
        return i >> 16;
    }
}
