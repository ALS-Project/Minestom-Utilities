package fr.bretzel.minestom.utils.raytrace;

import com.github.luben.zstd.ZstdInputStream;
import com.google.gson.JsonParser;
import fr.bretzel.minestom.utils.TriFunction;
import fr.bretzel.minestom.utils.math.MathsUtils;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.block.BlockIterator;
import net.minestom.server.utils.validate.Check;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiFunction;

public class RayTrace {
    public static BoundingBox ZERO = new BoundingBox(0, 0, 0);
    private static final Short2ObjectMap<MultiBlockShape> blockToShape = new Short2ObjectOpenHashMap<>();
    private static final boolean isInit = false;

    public static void init() {
        if (isInit)
            return;

        parseBlocksFile();

        printInfo();
    }

    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            var multiShape = blockToShape.get(block.stateId());
            var shape = multiShape.getCorrectShape(rayTraceContext.blockMode());
            var offset = shape.offsetType().operator().apply(position);
            return shape.rayTraceBlock(rayTraceContext, position, offset);

        }, (context1, position) -> new RayBlockResult(position, context1, null, null));
    }

    protected static <T> T rayBlocks(RayTraceContext context, TriFunction<RayTraceContext, Pos, Block, T> rayTrace, BiFunction<RayTraceContext, Point, T> miss) {
        if (!isInit)
            throw new IllegalStateException("RayTrace is not initialized");

        var start = context.start();
        var end = context.end();

        if (start.samePoint(end))
            return miss.apply(context, start);

        var blockIterator = new BlockIterator(Vec.fromPoint(start), context.direction(), 0, MathsUtils.floor(context.distance()));
        var instance = context.instance();

        var lastCheck = Pos.ZERO;

        while (blockIterator.hasNext()) {
            var position = blockIterator.next();

            //If chunk is not loaded
            if (!instance.isChunkLoaded(position))
                break;

            //If chunk is in void
            if (instance.isInVoid(position))
                break;

            //Get the Block
            var block = instance.getBlock(position);

            //Skip air block
            if (block.isAir())
                continue;

            //RayTrace, null == no collision
            var result = rayTrace.apply(context, lastCheck = Pos.fromPoint(position), block);

            //Found a hit
            if (result != null)
                return result;
        }

        return miss.apply(context, lastCheck);
    }

    private static void parseBlocksFile() {
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
                    blockToShape.put(multiShape.block().stateId(), multiShape);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInfo() {
        blockToShape.forEach((aShort, multiBlockShape) -> {
            System.out.println("Block: " + multiBlockShape.block().name() + " - " + multiBlockShape.block().stateId());
            System.out.println("Shape: " + multiBlockShape.shape());
            System.out.println("VisualShape: " + multiBlockShape.visualShape());
            System.out.println("CollisionShape: " + multiBlockShape.collisionShape());
            System.out.println("InteractionShape: " + multiBlockShape.interactionShape());
        });
    }
}
