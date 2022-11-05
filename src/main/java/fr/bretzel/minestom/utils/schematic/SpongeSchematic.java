package fr.bretzel.minestom.utils.schematic;

import fr.bretzel.minestom.utils.block.BlockUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

//from https://github.com/sejtam10/MineSchem/blob/404ff5b0aafd8d13e7a29ee9ee37f355c66d7107/MineSchem-Core/src/main/java/dev/sejtam/mineschem/core/schematic/SpongeSchematic.java
public class SpongeSchematic implements Schematic {

    private final Pos offset = Pos.ZERO;
    private final Map<Point, Block> blockMap = new HashMap<>();
    private short width;
    private short length;
    private Pos origin = Pos.ZERO;
    private short height;
    private Map<String, Integer> palette = new HashMap<>();
    private final Map<Point, NBTCompound> blockEntitiesMap = new HashMap<>();

    public SpongeSchematic(InputStream inputStream) {
        try {
            var nbtReader = new NBTReader(inputStream);
            var schematicTag = (NBTCompound) nbtReader.read();

            if (!schematicTag.containsKey("Version")) {
                throw new IllegalArgumentException("Schematic cannot contains Version tag");
            }

            var version = schematicTag.getAsInt("Version");

            if (version != null && version == 1) {
                readV1(schematicTag);
            } else {
                readV2(schematicTag);
            }

            nbtReader.close();
        } catch (IOException | NBTException e) {
            e.printStackTrace();
        }
    }

    public SpongeSchematic(File file) throws IOException {
        this(new GZIPInputStream(new FileInputStream(file)));
    }

    @Override
    public @NotNull Block getBlock(Point blockPosition) {
        var block = blockMap.getOrDefault(blockPosition.add(offset), Block.AIR);
        //TileEntitiesLoading
        if (block.registry().isBlockEntity() && hasTileEntities(blockPosition) && !block.isAir()) {
            var tileId = getTileEntitiesId(blockPosition);
            final var handler = MinecraftServer.getBlockManager().getHandlerOrDummy(tileId);
            block = block.withHandler(handler);

            var compound = getTileEntities(blockPosition, tileId);
            return compound.getSize() > 0 ? block.withNbt(compound) : block;
        }
        return block;
    }

    @Override
    public @NotNull Block getBlock(int x, int y, int z) {
        return getBlock(new Pos(x, y, z));
    }

    @Override
    public @NotNull Pos getOrigin() {
        return this.origin;
    }

    @Override
    public @NotNull Pos getOffset() {
        return this.offset;
    }

    @Override
    public void setBlock(@NotNull Point blockPosition, Block block) {
        blockMap.remove(blockPosition);
        blockMap.put(blockPosition, block);
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        setBlock(new Pos(x, y, z), block);
    }

    @Override
    public boolean hasTileEntities(Point point) {
        return blockEntitiesMap.entrySet().stream().anyMatch(entry -> entry.getKey().sameBlock(point));
    }

    @Override
    public boolean hasTileEntities(int x, int y, int z) {
        return blockEntitiesMap.entrySet().stream().anyMatch(entry -> entry.getKey().sameBlock(x, y, z));
    }

    @Override
    public String getTileEntitiesId(Point point) {
        var tileId = "";
        for (var entry : blockEntitiesMap.entrySet()) {
            if (entry.getKey().sameBlock(point)) {
                tileId = entry.getValue().getString("Id");
            }
        }
        return tileId;
    }

    @Override
    public String getTileEntitiesId(int x, int y, int z) {
        return getTileEntitiesId(new Pos(x, y, z));
    }

    @Override
    public NBTCompound getTileEntities(Point point, String id) {
        if (getTileEntitiesId(point).equals(id)) {
            for (var entry : blockEntitiesMap.entrySet()) {
                if (entry.getKey().sameBlock(point)) {
                    return entry.getValue();
                }
            }
        }
        return NBTCompound.EMPTY;
    }

    @Override
    public NBTCompound getTileEntities(int x, int y, int z, String id) {
        return getTileEntities(new Pos(x, y, z), id);
    }


    @Override
    public boolean isFormat(@NotNull File file) {
        return true;
    }

    @Override
    public Schematic save(@NotNull File file) throws IOException {
        var maxPalette = 0;
        var palette = new HashMap<String, Integer>();
        var outputStream = new ByteArrayOutputStream(width * height * length);

        for (var entry : blockMap.entrySet()) {
            var stateId = entry.getValue().stateId();
            var name = getNameFromStateId(stateId);

            var blockId = 0;

            if (palette.containsKey(name)) {
                blockId = palette.get(name);
            } else {
                blockId = maxPalette;
                palette.put(name, blockId);
                maxPalette++;
            }

            while ((blockId & -128) != 0) {
                outputStream.write(blockId & 127 | 128);
                blockId >>>= 7;
            }

            outputStream.write(blockId);
        }

        var finalMaxPalette = maxPalette;
        var nbtCompound = NBT.Compound(root -> {
            root.setInt("Version", 1);

            root.setShort("Width", width);
            root.setShort("Height", height);
            root.setShort("Length", length);

            root.setIntArray("Offset", new int[]{
                    (int) getOffset().x(),
                    (int) getOffset().y(),
                    (int) getOffset().z()
            });

            root.setInt("PaletteMax", finalMaxPalette);

            var paletteItems = NBT.Compound(mutableNBTCompound -> palette.forEach(mutableNBTCompound::setInt));

            root.set("Palette", paletteItems);
            root.setByteArray("BlockData", outputStream.toByteArray());
        });


        try (var writer = new NBTWriter(file, CompressedProcesser.GZIP)) {
            writer.writeNamed("Schematic", nbtCompound);
        } catch (IOException ex) {
            return null;
        }

        return new SpongeSchematic(file);
    }

    public short getLength() {
        return length;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    protected void readV1(NBTCompound compound) throws IOException {
        width = compound.getShort("Width");
        length = compound.getShort("Length");
        height = compound.getShort("Height");

        var nbtIntArray = (NBTIntArray) compound.get("Offset");
        int[] offsetParts;

        if (nbtIntArray != null) {
            offsetParts = nbtIntArray.getValue().copyArray();
            if (offsetParts.length != 3) {
                throw new IOException("Invalid offset specified in schematic.");
            }
        } else {
            offsetParts = new int[]{0, 0, 0};
        }

        origin = new Pos(offsetParts[0], offsetParts[1], offsetParts[2]);

        var paletteMax = compound.getAsInt("PaletteMax");

        var paletteTag = compound.getCompound("Palette");
        var keyList = new ArrayList<>(paletteTag.getKeys());

        if (keyList.size() != paletteMax) {
            throw new IllegalArgumentException("PaletteMax != Palette Size, please fix the schematic");
        }


        keyList.forEach(block -> {
            var integer = paletteTag.getInt(block);
            if (integer == null)
                return;
            this.palette.put(block, integer);
        });

        // Sort Palette map by values
        this.palette = this.palette.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);

        var blocksData = compound.getByteArray("BlockData").copyArray();

        if (blocksData == null || blocksData.length == 0)
            throw new IOException("Cannot fond BlockData entry in Schematic");

        var index = 0;
        var i = 0;
        var value = 0;
        var varintLength = 0;
        var paletteKeys = new ArrayList<>(palette.keySet());

        while (i < blocksData.length) {
            value = 0;
            varintLength = 0;

            while (true) {
                value |= (blocksData[i] & 127) << (varintLength++ * 7);
                if (varintLength > 5) {
                    throw new IOException("VarInt too big (probably corrupted data)");
                }

                if ((blocksData[i] & 128) != 128) {
                    i++;
                    break;
                }
                i++;
            }

            var x = (index % (width * length)) % width;
            var y = index / (width * length);
            var z = (index % (width * length)) / width;

            var block = paletteKeys.get(value);
            this.blockMap.put(new Pos(x, y, z), getStateId(block));
            index++;
        }
    }

    protected void readV2(NBTCompound compound) throws IOException {
        readV1(compound);

        if (compound.containsKey("BlockEntities")) {
            var blockEntities = compound.getList("BlockEntities");
            for (NBT nbt : blockEntities) {
                var data = (NBTCompound) nbt;
                var nbtCompound = data.toMutableCompound();
                var coords = nbtCompound.getIntArray("Pos").copyArray();
                nbtCompound.remove("Pos");
                blockEntitiesMap.put(new Pos(coords[0], coords[1], coords[2]), nbtCompound.toCompound());
            }
        }

        //Make Entity/Biomes
    }

    private Block getStateId(@NotNull String input) {
        var blockStatesIndex = input.indexOf("[");

        if (blockStatesIndex == -1) {
            return BlockUtils.getOrDefault(Block.fromNamespaceId(input), Block.AIR);
        } else {
            var blockName = input.substring(0, blockStatesIndex);
            var block = Block.fromNamespaceId(blockName);

            Check.notNull(block, "Block Cannot be null");

            var blockStatesMap = new HashMap<>(block.properties());

            var stateString = input.substring(blockStatesIndex).replace("\\\"", "\"").substring(1);

            stateString = stateString.substring(0, stateString.length() - 1);

            var states = stateString.split(",");

            for (var pair : states) {
                var keyAndValue = pair.split("=");
                blockStatesMap.remove(keyAndValue[0]);
                blockStatesMap.put(keyAndValue[0], keyAndValue[1]);
            }

            return block.withProperties(blockStatesMap);
        }
    }

    private @NotNull String getNameFromStateId(short stateId) {
        var block = Block.fromStateId(stateId);
        var blockName = new StringBuilder(block.namespace().getPath());

        blockName.append(blockName).append("[");

        block.properties().forEach((key, value) -> blockName.append(key).append("=").append(value).append(","));

        return blockName.reverse().append("]").toString();
    }
}
