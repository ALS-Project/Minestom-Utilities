package fr.bretzel.minestom.utils.raytrace;

import com.github.luben.zstd.ZstdInputStream;
import com.google.gson.JsonParser;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.TriFunction;
import fr.bretzel.minestom.utils.math.MathsUtils;
import fr.bretzel.minestom.utils.raytrace.shapes.MultiBlockShape;
import fr.bretzel.minestom.utils.raytrace.shapes.OffsetType;
import it.unimi.dsi.fastutil.shorts.Short2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.block.BlockIterator;
import net.minestom.server.utils.validate.Check;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiFunction;

public class RayTrace {
    private static final Short2ObjectMap<MultiBlockShape> blockToShape = new Short2ObjectLinkedOpenHashMap<>();
    private static final boolean isInit = false;

    public static void init() {
        if (isInit)
            return;

        parseBlockFile();
    }

    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            var shape = switch (rayTraceContext.blockMode()) {
                case VISUAL -> blockToShape.get(block.stateId()).visualShape();
                case INTERACTION -> blockToShape.get(block.stateId()).interactionShape();
                case COLLISION -> blockToShape.get(block.stateId()).collisionShape();
                default -> blockToShape.get(block.stateId()).shape();
            };

            //TODO: Make the raytrace work with the shape

            return null;

        }, (context1, position) -> new RayBlockResult(position, position, context1, Facing.SELF, null));
    }

    protected static <T> T rayBlocks(RayTraceContext context, TriFunction<RayTraceContext, Point, Block, T> rayTrace, BiFunction<RayTraceContext, Point, T> miss) {
        if(!isInit)
            throw new IllegalStateException("RayTrace is not initialized");

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
        try (InputStream inputStream = new ZstdInputStream(RayTrace.class.getResourceAsStream("data/blocks.json.zst"))) {
            Check.notNull(inputStream, "Resource {0} does not exist!", "data/blocks.json.zst");
            var jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
            var mainObject = jsonElement.getAsJsonObject();

            for (String blockId : mainObject.keySet()) {
                //load the json object for the block
                var blockObject = mainObject.getAsJsonObject(blockId);

                Check.notNull(blockObject, "Resource {0} does not exist!", blockId);

                var statesObject = blockObject.getAsJsonArray("states");

                for (var elementState : statesObject) {
                    if (!elementState.isJsonObject())
                        continue;

                    var multiShape = MultiBlockShape.of(elementState.getAsJsonObject());
                    blockToShape.put(multiShape.stateId(), multiShape);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
