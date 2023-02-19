package fr.bretzel.minestom.utils.raytrace.shapes;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;


public class BlockShape {
    public static final BlockShape EMPTY = new BlockShape(new BlockSection[0], Block.AIR);
    private final BlockSection[] blockSections;
    private final Point relativeStart, relativeEnd;
    private Block block;

    public BlockShape(BlockSection[] blockSections, Block block) {
        this.blockSections = blockSections;
        this.block = block;

        // Find bounds
        {
            double minX = 1, minY = 1, minZ = 1;
            double maxX = 0, maxY = 0, maxZ = 0;
            for (BlockSection blockSection : blockSections) {
                // Min
                if (blockSection.minX() < minX) minX = blockSection.minX();
                if (blockSection.minY() < minY) minY = blockSection.minY();
                if (blockSection.minZ() < minZ) minZ = blockSection.minZ();
                // Max
                if (blockSection.maxX() > maxX) maxX = blockSection.maxX();
                if (blockSection.maxY() > maxY) maxY = blockSection.maxY();
                if (blockSection.maxZ() > maxZ) maxZ = blockSection.maxZ();
            }
            this.relativeStart = new Vec(minX, minY, minZ);
            this.relativeEnd = new Vec(maxX, maxY, maxZ);
        }
    }

    public @NotNull Point relativeStart() {
        return relativeStart;
    }

    public @NotNull Point relativeEnd() {
        return relativeEnd;
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

    public BlockSection[] blockSections() {
        return blockSections;
    }
}
