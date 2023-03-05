package fr.bretzel.minestom.utils.raytrace;

import com.github.luben.zstd.Zstd;
import com.google.gson.JsonParser;
import fr.bretzel.minestom.utils.TriFunction;
import fr.bretzel.minestom.utils.math.MathsUtils;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.block.BlockIterator;
import net.minestom.server.utils.validate.Check;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class RayTrace {
    public static BoundingBox ZERO = new BoundingBox(0, 0, 0);

    public static final MultiBlockShape EMPTY = new MultiBlockShape(BlockShape.EMPTY, BlockShape.EMPTY, BlockShape.EMPTY, BlockShape.EMPTY, Block.AIR.stateId(), Block.AIR, Block.AIR.namespace());

    private static final Short2ObjectMap<MultiBlockShape> blockToShape = new Short2ObjectOpenHashMap<>();
    private static boolean isInit = false;

    public static void init() {
        if (isInit)
            return;

        parseBlocksFile();
        assertBlockShapes();

        isInit = true;
        //printInfo();
    }

    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            var multiShape = blockToShape.get(block.stateId());
            var shape = multiShape.getCorrectShape(rayTraceContext.blockMode());
            var offset = shape.offsetType().operator().apply(position);
            return shape.rayTraceBlock(rayTraceContext, position, offset);

        }, (context1, position) -> new RayBlockResult(position, context1, BlockFace.BOTTOM, BlockShape.EMPTY));
    }

    protected static <T> T rayBlocks(RayTraceContext context, TriFunction<RayTraceContext, Pos, Block, T> rayTrace, BiFunction<RayTraceContext, Point, T> miss) {
        if (!isInit)
            throw new IllegalStateException("RayTrace is not initialized");

        var start = context.start();
        var end = context.end();

        if (start.samePoint(end))
            return miss.apply(context, start);

        var blockIterator = new BlockIterator(Vec.fromPoint(start), context.direction(), 0, MathsUtils.floor(context.distance()), true);

        var instance = context.instance();

        var lastCheck = Pos.ZERO;

        var lastChunkCkeck = instance.getChunkAt(start);

        while (blockIterator.hasNext()) {

            var position = blockIterator.next();
            var chunkX = position.chunkX();
            var chunkZ = position.chunkZ();

            if (lastChunkCkeck == null || (lastChunkCkeck.getChunkX() != chunkX || lastChunkCkeck.getChunkZ() != chunkZ)) {
                lastChunkCkeck = instance.getChunkAt(position);

                if (lastChunkCkeck == null)
                    break;
            }

            //If chunk is in void
            if (instance.isInVoid(position))
                break;

            //Get the Block
            var block = lastChunkCkeck.getBlock(position, Block.Getter.Condition.TYPE);

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
        try (var inputStream = RayTrace.class.getResourceAsStream("/data/blocks.json.zst")) {
            Check.notNull(inputStream, "Resource {0} does not exist!", "/data/blocks.json.zst");

            byte[] compressed = inputStream.readAllBytes();

            inputStream.close();

            var originalSize = Zstd.decompressedSize(compressed);
            byte[] decompressed = Zstd.decompress(compressed, (int) originalSize);

            Reader reader = new InputStreamReader(new ByteArrayInputStream(decompressed), StandardCharsets.UTF_8);
            var jsonElement = JsonParser.parseReader(new BufferedReader(reader));
            var mainObject = jsonElement.getAsJsonObject();

            for (String blockId : mainObject.keySet()) {
                //load the json object for the block
                var blockObject = mainObject.getAsJsonObject(blockId);

                Check.notNull(blockObject, "Resource {0} does not exist!", blockId);

                var statesObject = blockObject.getAsJsonArray("states");

                for (var elementState : statesObject) {
                    if (!elementState.isJsonObject())
                        continue;

                    Block block = Block.fromNamespaceId(blockId);

                    if (block == null) {
                        System.out.println("Block not found: " + blockId);
                        continue;
                    }

                    var multiShape = MultiBlockShape.of(elementState.getAsJsonObject(), NamespaceID.from(blockId));

                    blockToShape.put(multiShape.stateId(), multiShape);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void assertBlockShapes() {
        Map<String, ShortArrayList> map = new HashMap<>();

        blockToShape.forEach((aShort, multiBlockShape) -> {
            var block = multiBlockShape.block();
            var stateId = multiBlockShape.stateId();

            if (block == null) {
                if (map.containsKey(multiBlockShape.namespaceID().toString())) {
                    map.get(multiBlockShape.namespaceID().toString()).add(stateId);
                } else {
                    var list = new ShortArrayList();
                    list.add(stateId);
                    map.put(multiBlockShape.namespaceID().toString(), list);
                }
                return;
            }

            var blockId = Block.fromStateId(stateId);

            if (blockId != block) {
                throw new IllegalStateException("BlockId is not the same for stateId: " + stateId);
            }
        });

        map.forEach((s, shorts) -> {
            System.out.println("###############################################");
            System.out.println("Block " + s + " doesnt not exist.");
            System.out.println("Impacted States: " + shorts);
            System.out.println("###############################################");
        });
    }

    private static void printInfo() {
        blockToShape.forEach((aShort, multiBlockShape) -> {
            var blockName = multiBlockShape.block() == null ? "null" : multiBlockShape.block().name();
            System.out.println("Block: " + blockName + " - " + multiBlockShape.stateId());
            System.out.println("Shape: " + multiBlockShape.shape());
            System.out.println("VisualShape: " + multiBlockShape.visualShape());
            System.out.println("CollisionShape: " + multiBlockShape.collisionShape());
            System.out.println("InteractionShape: " + multiBlockShape.interactionShape());
        });
    }
}
