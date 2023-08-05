package fr.bretzel.minestom.utils.raytrace;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BlockShape extends IRayTrace {
    private static final Pattern PATTERN = Pattern.compile("\\d.\\d{1,3}", Pattern.MULTILINE);
    public static final BlockShape EMPTY = new BlockShape(new BlockSection[0], Block.AIR, OffsetType.NONE, RenderType.MODEL);
    private final BlockSection[] blockSections;
    private final Point relativeStart, relativeEnd;
    private final Block block;
    private final OffsetType offsetType;
    private final RenderType renderType;

    public BlockShape(BlockSection[] blockSections, Block block, OffsetType offsetType, RenderType renderType) {
        this.blockSections = blockSections;
        this.block = block;
        this.offsetType = offsetType;
        this.renderType = renderType;

        // Find bounds
        {
            double minX = 1, minY = 1, minZ = 1;
            double maxX = 0, maxY = 0, maxZ = 0;
            for (BlockSection blockSection : blockSections) {
                blockSection.setParent(this);
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

    public static BlockShape of(String aabbString, Block block, OffsetType offsetType, RenderType renderType) {
        if (aabbString == null || aabbString.isEmpty() || aabbString.equals("[]"))
            return BlockShape.EMPTY;

        final Matcher matcher = PATTERN.matcher(aabbString);
        DoubleList vars = new DoubleArrayList();

        while (matcher.find()) {
            double newVal = Double.parseDouble(matcher.group());
            vars.add(newVal);
        }

        final int count = vars.size() / 6;

        BlockSection[] blockSections = new BlockSection[count];

        for (int i = 0; i < count; ++i) {
            final double minX = vars.getDouble(6 * i);
            final double minY = vars.getDouble(1 + 6 * i);
            final double minZ = vars.getDouble(2 + 6 * i);

            final double boundXSize = vars.getDouble(3 + 6 * i) - minX;
            final double boundYSize = vars.getDouble(4 + 6 * i) - minY;
            final double boundZSize = vars.getDouble(5 + 6 * i) - minZ;

            final BlockSection bs = new BlockSection(boundXSize, boundYSize, boundZSize);

            assert bs.minX() == minX;
            assert bs.minY() == minY;
            assert bs.minZ() == minZ;

            blockSections[i] = bs;
        }

        return new BlockShape(blockSections, block, offsetType, renderType);
    }

    public @NotNull Point relativeStart() {
        return relativeStart;
    }

    public @NotNull Point relativeEnd() {
        return relativeEnd;
    }

    public Block block() {
        return block;
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

    public boolean empty() {
        return this == EMPTY;
    }

    public OffsetType offsetType() {
        return offsetType;
    }

    public RenderType renderType() {
        return renderType;
    }

    public BlockSection[] blockSections() {
        return blockSections;
    }

    @Override
    public String toString() {
        return "BlockShape{" +
                "blockSections=" + Arrays.toString(blockSections) +
                ", relativeStart=" + relativeStart +
                ", relativeEnd=" + relativeEnd +
                ", block=" + block +
                ", offsetType=" + offsetType +
                ", renderType=" + renderType +
                '}';
    }

    @Override
    RayBlockResult rayTraceBlock(RayTraceContext context, Point blockPosition, Point offset) {
        //No collision
        if (empty())
            return null;

        RayBlockResult result = null;

        for (BlockSection blockSection : blockSections) {
            RayBlockResult blockResult = blockSection.rayTraceBlock(context, blockPosition, offset);
            if (blockResult != null)
                result = blockResult;
        }

        return result;
    }
}
