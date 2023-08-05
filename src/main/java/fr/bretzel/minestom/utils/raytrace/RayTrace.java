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

    /**
     * @param context The ray trace context {@link RayTraceContext}
     * @return The ray trace result {@link RayBlockResult}
     */
    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            //Get the block shape
            var multiShape = blockToShape.get(block.stateId());
            if (multiShape == null)
                return null;

            //get the correct shape from the block mode
            var shape = multiShape.getCorrectShape(rayTraceContext.blockMode());

            if (shape.empty())
                return null;

            //get the offset of the block and apply it to the position
            var offset = shape.offsetType().operator().apply(position);
            //ray trace the block
            return shape.rayTraceBlock(rayTraceContext, position, offset);

        }, (context1, position) -> new RayBlockResult(position, position, context1, BlockFace.BOTTOM, BlockShape.EMPTY));
    }

    /**
     * @param context  The ray trace context   {@link RayTraceContext}
     * @param rayTrace The ray trace function {@link TriFunction}
     * @param miss     The miss function       {@link BiFunction}
     * @param <T>      The return type
     */
    protected static <T> T rayBlocks(RayTraceContext context, TriFunction<RayTraceContext, Pos, Block, T> rayTrace, BiFunction<RayTraceContext, Point, T> miss) {
        if (!isInit)
            throw new IllegalStateException("RayTrace is not initialized");

        var start = context.start();
        var end = context.end();

        //can't ray trace if start and end are the same
        if (start.samePoint(end))
            return miss.apply(context, start);

        //create a block iterator to iterate over all the blocks
        var blockIterator = new BlockIterator(Vec.fromPoint(start), context.direction(), 0, MathsUtils.floor(context.distance()), true);

        //get the instance
        var instance = context.instance();

        //last checked position
        var lastCheck = Pos.fromPoint(start);

        //last checked chunk
        var lastChunkCkeck = instance.getChunkAt(start);

        //iterate over all the blocks
        while (blockIterator.hasNext()) {

            //get the position
            var position = blockIterator.next();

            //get the chunk position
            var chunkX = position.chunkX();
            var chunkZ = position.chunkZ();

            //if the chunk is not the same as the last checked chunk get the new chunk and check if it's null break the loop
            if (lastChunkCkeck == null || (lastChunkCkeck.getChunkX() != chunkX || lastChunkCkeck.getChunkZ() != chunkZ)) {
                lastChunkCkeck = instance.getChunkAt(position);

                if (lastChunkCkeck == null)
                    break;
            }

            //If chunk is in void
            if (instance.isInVoid(position))
                break;

            //Get the Block
            var block = lastChunkCkeck.getBlock(position);

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

    /**
     * load the blocks.json.zst file and register all block shapes in the {@link #blockToShape} map
     */
    private static void parseBlocksFile() {
        //load the blocks.json.zst file
        try (var inputStream = RayTrace.class.getResourceAsStream("/data/blocks.json.zst")) {
            Check.notNull(inputStream, "Resource {0} does not exist!", "/data/blocks.json.zst");

            //read the file as a byte array and close the input stream
            byte[] compressed = inputStream.readAllBytes();
            inputStream.close();

            //decompress the file
            var originalSize = Zstd.decompressedSize(compressed);
            byte[] decompressed = Zstd.decompress(compressed, (int) originalSize);

            //parse the json file and load all the block shapes
            Reader reader = new InputStreamReader(new ByteArrayInputStream(decompressed), StandardCharsets.UTF_8);
            var jsonElement = JsonParser.parseReader(new BufferedReader(reader));
            var mainObject = jsonElement.getAsJsonObject();

            //iterate over all json objects keys
            for (String blockId : mainObject.keySet()) {
                //load the json object for the block
                var blockObject = mainObject.getAsJsonObject(blockId);

                Check.notNull(blockObject, "Resource {0} does not exist!", blockId);

                //load the states array for the block
                var statesObject = blockObject.getAsJsonArray("states");

                //iterate over all the states
                for (var elementState : statesObject) {
                    if (!elementState.isJsonObject())
                        continue;

                    //get the block from the namespace id
                    Block block = Block.fromNamespaceId(blockId);

                    //if the block is null print a warning and continue
                    if (block == null) {
                        System.out.println("Block not found: " + blockId);
                        continue;
                    }

                    //create the block shape and add it to the map
                    var multiShape = MultiBlockShape.of(elementState.getAsJsonObject(), NamespaceID.from(blockId));
                    blockToShape.put(multiShape.stateId(), multiShape);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Assert that all the block shapes are linked to the correct block
     */
    private static void assertBlockShapes() {
        Map<String, ShortArrayList> map = new HashMap<>();

        //iterate over all the block shapes and check if the block is the same as the block from the state id
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

            //get the block from the state id and check if it's the same as the block from the block shape (if not throw an exception)
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

        map.clear();
    }

    /**
     * Print all the block shapes
     */
    private static void printInfo() {
        //iterate over all the block shapes and print the block name, state id, shape, visual shape, collision shape and interaction shape
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
