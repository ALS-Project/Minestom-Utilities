package fr.bretzel.minestom.utils.schematic;

import fr.bretzel.minestom.utils.block.BlockUtils;
import fr.bretzel.minestom.utils.file.FileUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import org.jglrxavpok.hephaistos.nbt.NBTType;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

//Ported from WorldEdit: https://github.com/EngineHub/WorldEdit/blob/a806c53287f754365b7a3d9a775226365466ab11/src/main/java/com/sk89q/worldedit/schematic/MCEditSchematicFormat.java
public class MCEditSchematic implements Schematic {
    private static final int MAX_SIZE = Short.MAX_VALUE - Short.MIN_VALUE;

    private Pos origin = Pos.ZERO;
    private Pos offset = Pos.ZERO;
    private short[] mineBlocks = new short[0];
    private short width = 0;
    private short length = 0;
    private short height = 0;

    public MCEditSchematic(GZIPInputStream inputStream) {
        try {
            NBTReader reader = new NBTReader(inputStream);
            NBTCompound schematicTag = (NBTCompound) reader.read();
            reader.close();

            if (!schematicTag.containsKey("Blocks")) {
                throw new NBTException("Schematic file is missing a \"Blocks\" tag");
            }

            width = schematicTag.getAsShort("Width");
            length = schematicTag.getAsShort("Length");
            height = schematicTag.getAsShort("Height");

            try {
                int originX = schematicTag.getAsInt("WEOriginX");
                int originY = schematicTag.getAsInt("WEOriginY");
                int originZ = schematicTag.getAsInt("WEOriginZ");

                origin = new Pos(originX, originY, originZ);
            } catch (Exception e) {
                // No origin data
            }

            try {
                int offsetX = schematicTag.getAsInt("WEOffsetX");
                int offsetY = schematicTag.getAsInt("WEOffsetY");
                int offsetZ = schematicTag.getAsInt("WEOffsetZ");

                offset = new Pos(offsetX, offsetY, offsetZ);
            } catch (Exception e) {
                // No offset data
            }


            String materials = schematicTag.getString("Materials");

            if (materials == null || !materials.equals("Alpha")) {
                throw new NBTException("Schematic file is not an Alpha schematic");
            }

            byte[] blockId = schematicTag.getByteArray("Blocks").copyArray();
            byte[] addId = new byte[0];

            short[] blocks = new short[blockId.length];

            if (schematicTag.containsKey("AddBlocks")) {
                addId = schematicTag.getByteArray("AddBlocks").copyArray();
            }

            for (int index = 0; index < blockId.length; index++) {
                if ((index >> 1) >= addId.length) { // No corresponding AddBlocks index
                    blocks[index] = (short) (blockId[index] & 0xFF);
                } else {
                    if ((index & 1) == 0) {
                        blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockId[index] & 0xFF));
                    } else {
                        blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
                    }
                }
            }

            mineBlocks = new short[blockId.length];

            //From MineSchem https://github.com/sejtam10/MineSchem/blob/main/MineSchem-Core/src/main/java/dev/sejtam/mineschem/core/schematic/MCEditSchematic.java
            for (int x = 0; x < this.width; ++x) {
                for (int y = 0; y < height; ++y) {
                    for (int z = 0; z < this.length; ++z) {
                        int index = y * this.width * this.length + z * this.width + x;
                        short stateId = (short) (blocks[index] << 8);
                        mineBlocks[index] = stateId;
                    }
                }
            }
        } catch (IOException | NBTException e) {
            e.printStackTrace();
        }
    }

    public MCEditSchematic(@NotNull File file) throws IOException {
        this(new GZIPInputStream(new FileInputStream(file)));
    }

    public static boolean isCorrectFormat(File file) {
        try (DataInputStream str = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
            if ((str.readByte() & 0xFF) != NBTType.TAG_Compound.getOrdinal()) {
                return false;
            }
            byte[] nameBytes = new byte[str.readShort() & 0xFFFF];
            str.readFully(nameBytes);
            String name = new String(nameBytes, StandardCharsets.UTF_8);
            return name.equals("Schematic");
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void setBlock(@NotNull Point blockPosition, Block block) {
        int index = blockPosition.blockY() * width * length + blockPosition.blockZ() * width + blockPosition.blockX();
        mineBlocks[index] = block.stateId();
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        setBlock(new Pos(x, y, z), block);
    }

    @Override
    public short getLength() {
        return length;
    }

    @Override
    public short getWidth() {
        return width;
    }

    @Override
    public short getHeight() {
        return height;
    }

    @Override
    public @NotNull Block getBlock(Point blockPosition) {
        return getBlock(blockPosition.blockX(), blockPosition.blockY(), blockPosition.blockZ());
    }

    @Override
    public @NotNull Block getBlock(int x, int y, int z) {
        int index = y * width * length + z * width + x;
        return BlockUtils.getOrDefault(Block.fromStateId(mineBlocks[index]), Block.AIR);
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
    public boolean isFormat(@NotNull File file) {
        return isCorrectFormat(file);
    }

    @Override
    public boolean hasTileEntities(Point point) {
        return false;
    }

    @Override
    public boolean hasTileEntities(int x, int y, int z) {
        return false;
    }

    @Override
    public String getTileEntitiesId(Point point) {
        throw new UnsupportedOperationException("MCEDit Schematic do not support that !");
    }

    @Override
    public String getTileEntitiesId(int x, int y, int z) {
        throw new UnsupportedOperationException("MCEDit Schematic do not support that !");
    }

    @Override
    public NBTCompound getTileEntities(Point point, String id) {
        throw new UnsupportedOperationException("MCEDit Schematic do not support that !");
    }

    @Override
    public NBTCompound getTileEntities(int x, int y, int z, String id) {
        throw new UnsupportedOperationException("MCEDit Schematic do not support that !");
    }

    @Override
    public @NotNull Schematic save(@NotNull File file) throws IOException {
        if (!file.exists())
            FileUtils.createNewFile(file);

        return new MCEditSchematic(file);
    }
}
