package fr.bretzel.minestom.utils.raytrace;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

import java.util.function.Function;

public enum OffsetType {
    NONE,
    XZ(blockPosition -> {
        var rand = getOffsetCoordinate(blockPosition.blockX(), 0, blockPosition.blockZ());
        return new Vec(((double) ((float) (rand & 15L) / 15.0F) - 0.5D) * 0.5D, 0.0D, ((double) ((float) (rand >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D);
    }),
    XYZ(blockPosition -> {
        var rand = getOffsetCoordinate(blockPosition.blockX(), blockPosition.blockY(), blockPosition.blockZ());
        return new Vec(((double) ((float) (rand & 15L) / 15.0F) - 0.5D) * 0.5D, ((double) ((float) (rand >> 4 & 15L) / 15.0F) - 1.0D) * 0.2D, ((double) ((float) (rand >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D);
    });

    private final Function<Point, Vec> offset;

    OffsetType() {
        offset = point -> new Vec(point.x(), point.y(), point.z());
    }

    OffsetType(Function<Point, Vec> offset) {
        this.offset = offset;
    }

    public Function<Point, Vec> operator() {
        return offset;
    }

    private static long getOffsetCoordinate(int x, int y, int z) {
        long i = (x * 3129871L) ^ (long) z * 116129781L ^ (long) y;
        i = i * i * 42317861L + i * 11L;
        return i >> 16;
    }
}
