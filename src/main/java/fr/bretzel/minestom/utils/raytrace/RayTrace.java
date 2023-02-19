package fr.bretzel.minestom.utils.raytrace;

import com.google.gson.JsonParser;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.TriFunction;
import fr.bretzel.minestom.utils.math.MathsUtils;
import fr.bretzel.minestom.utils.raytrace.shapes.BlockSection;
import fr.bretzel.minestom.utils.raytrace.shapes.BlockShape;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.registry.Registry;
import net.minestom.server.utils.block.BlockIterator;
import net.minestom.server.utils.validate.Check;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RayTrace {
    private static final Short2ObjectMap<BlockShape> blockToShape = new Short2ObjectLinkedOpenHashMap<>();
    private static final Pattern PATTERN = Pattern.compile("\\d.\\d{1,3}", Pattern.MULTILINE);
    private static final boolean isInit = false;

    public static void init() {
        if (isInit)
            return;


    }

    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            if (rayTraceContext.blockMode() == BlockMode.SHAPE) {
                var shape = block.registry().collisionShape();

                var isHit = shape.intersectBoxSwept(context.start(), context.direction(), position, new BoundingBox(0.01, 0.01, 0.01), new SweepResult(100, 0, 0, 0));

                if (isHit) {
                    return new RayBlockResult(position, position, context, Facing.SELF, shape);
                }
            }

            return null;

        }, (context1, position) -> new RayBlockResult(position, position, context1, Facing.SELF, null));
    }

    protected static <T> T rayBlocks(RayTraceContext context, TriFunction<RayTraceContext, Point, Block, T> rayTrace, BiFunction<RayTraceContext, Point, T> miss) {
        var start = context.start();
        var end = context.end();

        if (start.samePoint(end))
            return miss.apply(context, start);

        var blockIterator = new BlockIterator(start, context.direction(), 0, MathsUtils.floor(context.distance()));
        var instance = context.instance();

        var lastCheck = Vec.ZERO;

        while (blockIterator.hasNext()) {
            var position = blockIterator.next();

            //If chunk is not loaded
            if (!instance.isChunkLoaded(position))
                break;

            //If chunk is in void
            if (instance.isInVoid(position))
                break;

            lastCheck = Vec.fromPoint(position);

            //Get the Block
            var block = instance.getBlock(position);

            //Skip air block
            if (block.isAir())
                continue;

            //RayTrace, null == no collision
            var result = rayTrace.apply(context, position, block);

            //Found a hit
            if (result != null)
                return result;
        }

        return miss.apply(context, lastCheck);
    }

    public static Vec getOffset(Point blockPosition, OffsetType offsetType) {
        if (offsetType == OffsetType.NONE) {
            return Vec.ZERO;
        } else {
            var rand = MathsUtils.getOffsetCoordinate(blockPosition.blockX(), 0, blockPosition.blockZ());
            return new Vec(((double) ((float) (rand & 15L) / 15.0F) - 0.5D) * 0.5D, offsetType == OffsetType.XYZ ? ((double) ((float) (rand >> 4 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double) ((float) (rand >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D);
        }
    }

    private static void parseBlockFile() {
        try (InputStream inputStream = Registry.class.getResourceAsStream("blocks.json")) {
            Check.notNull(inputStream, "Resource {0} does not exist!", "blocks.json");
            var jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
            var mainObject = jsonElement.getAsJsonObject();

            for (String blockId : mainObject.keySet()) {
                var blockObject = mainObject.getAsJsonObject(blockId);
                Check.notNull(inputStream, "Resource {0} does not exist!", blockId);

                var block = Block.fromNamespaceId(blockId);
                Check.notNull(inputStream, "Cannot found block {0} !", blockId);

                var defaultShape = stringToShape(blockObject.get("shape").getAsString(), block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BlockShape stringToShape(String shapesString, Block block) {
        if (shapesString == null || shapesString.isEmpty())
            return BlockShape.EMPTY;

        final Matcher matcher = PATTERN.matcher(shapesString);
        DoubleList vals = new DoubleArrayList();

        while (matcher.find()) {
            double newVal = Double.parseDouble(matcher.group());
            vals.add(newVal);
        }

        final int count = vals.size() / 6;

        BlockSection[] blockSections = new BlockSection[count];

        for (int i = 0; i < count; ++i) {
            final double minX = vals.getDouble(6 * i);
            final double minY = vals.getDouble(1 + 6 * i);
            final double minZ = vals.getDouble(2 + 6 * i);

            final double boundXSize = vals.getDouble(3 + 6 * i) - minX;
            final double boundYSize = vals.getDouble(4 + 6 * i) - minY;
            final double boundZSize = vals.getDouble(5 + 6 * i) - minZ;

            final BlockSection bs = new BlockSection(boundXSize, boundYSize, boundZSize);
            assert bs.minX() == minX;
            assert bs.minY() == minY;
            assert bs.minZ() == minZ;
            blockSections[i] = bs;
        }

        return new BlockShape(blockSections, block);
    }
}
