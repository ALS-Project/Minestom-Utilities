package fr.bretzel.minestom.utils.raytrace;

import com.google.gson.JsonParser;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.TriFunction;
import fr.bretzel.minestom.utils.math.MathsUtils;
import fr.bretzel.minestom.utils.raytrace.shapes.BlockSection;
import fr.bretzel.minestom.utils.raytrace.shapes.BlockShape;
import fr.bretzel.minestom.utils.raytrace.shapes.MultiBlockShape;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RayTrace {
    private static final Logger LOGGER = LoggerFactory.getLogger(RayTrace.class);
    private static final Short2ObjectMap<MultiBlockShape> blockToShape = new Short2ObjectLinkedOpenHashMap<>();
    private static final Pattern PATTERN = Pattern.compile("\\d.\\d{1,3}", Pattern.MULTILINE);
    private static final boolean isInit = false;

    public static void init() {
        if (isInit)
            return;

        parseBlockFile();
    }

    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            if (rayTraceContext.blockMode() == BlockMode.SHAPE) {

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
                //load the json object for the block
                var blockObject = mainObject.getAsJsonObject(blockId);
                Check.notNull(inputStream, "Resource {0} does not exist!", blockId);

                //get the block from the namespace
                var block = Block.fromNamespaceId(blockId);
                Check.notNull(inputStream, "Cannot found block {0} !", blockId);
                var statesObject = blockObject.getAsJsonObject("states");

                //No states
                if (statesObject.keySet().size() == 1) {
                    blockToShape.put(block.stateId(), new MultiBlockShape(defaultShape, visualShape, collisionShape, interactionShape, occlusionShape));
                } else {
                    //Has block states
                    for (String stateString : statesObject.keySet()) {
                        var blockSatesObject = statesObject.getAsJsonObject(stateString);

                        short stateId = blockSatesObject.get("stateId").getAsShort();

                        if (blockSatesObject.has("shape"))
                            defaultShape = stringToShape(blockSatesObject.get("shape").getAsString(), block);

                        if (blockSatesObject.has("collisionShape"))
                            collisionShape = stringToShape(blockSatesObject.get("collisionShape").getAsString(), block);

                        if (blockSatesObject.has("interactionShape"))
                            interactionShape = stringToShape(blockSatesObject.get("interactionShape").getAsString(), block);

                        if (blockSatesObject.has("occlusionShape"))
                            occlusionShape = stringToShape(blockSatesObject.get("occlusionShape").getAsString(), block);

                        if (blockSatesObject.has("visualShape"))
                            visualShape = stringToShape(blockSatesObject.get("visualShape").getAsString(), block);

                        blockToShape.put(stateId, new MultiBlockShape(defaultShape, visualShape, collisionShape, interactionShape, occlusionShape));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert a string to a {@link BlockShape}
     * using the following format:
     * <p> {@code [AABB[0.0, 0.0, 0.0] -> [1.0, 1.0, 1.0]} </p>
     * <p> {@code [AABB[0.0, 0.0, 0.0] -> [1.0, 1.0, 1.0], AABB[0.0, 0.0, 0.0] -> [1.0, 1.0, 1.0]]} </p>
     *
     * @param shapesString the string to convert
     * @param block        the block to apply the shape
     * @return the {@link BlockShape}
     */
    private static BlockShape stringToShape(String shapesString, Block block) {
        if (shapesString == null || shapesString.isEmpty() || shapesString.equals("[]")) {
            LOGGER.info("Block {} has not a correct shape: {}", block.namespace(), shapesString);
            return BlockShape.EMPTY;
        }

        final Matcher matcher = PATTERN.matcher(shapesString);
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

        return new BlockShape(blockSections, block);
    }
}
