package fr.bretzel.minestom.utils.raytrace;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.NotNull;

public class BlockSection {
    private final double width, height, depth;
    private final Point offset;
    private Point relativeEnd;

    public BlockSection(double width, double height, double depth, Vec offset) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.offset = offset;
    }

    public BlockSection(double width, double height, double depth) {
        this(width, height, depth, new Vec(-width / 2, 0, -depth / 2));
    }

    public @NotNull Point relativeStart() {
        return offset;
    }

    public @NotNull Point relativeEnd() {
        Point relativeEnd = this.relativeEnd;
        if (relativeEnd == null) this.relativeEnd = relativeEnd = offset.add(width, height, depth);
        return relativeEnd;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    public double depth() {
        return depth;
    }

    public double minX() {
        return relativeStart().x();
    }

    public double maxX() {
        return relativeEnd().x();
    }

    public double minY() {
        return relativeStart().y();
    }

    public double maxY() {
        return relativeEnd().y();
    }

    public double minZ() {
        return relativeStart().z();
    }

    public double maxZ() {
        return relativeEnd().z();
    }
}
