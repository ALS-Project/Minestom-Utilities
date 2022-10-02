package fr.bretzel.minestom.utils.schematic;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.io.File;
import java.io.IOException;

public interface Schematic {

    /**
     * Get the block as position
     *
     * @param blockPosition position of block
     * @return the block or air
     */
    @NotNull Block getBlock(Point blockPosition);

    /**
     * @param x block x
     * @param y block y
     * @param z block z
     * @return the block or air if is null
     */
    @NotNull Block getBlock(int x, int y, int z);

    /**
     * @return get the origin point
     */
    @NotNull Pos getOrigin();

    /**
     * @return the offset pos
     */
    @NotNull Pos getOffset();

    /**
     * @param blockPosition position of block
     */
    void setBlock(@NotNull Point blockPosition, Block block);

    /**
     * Set the block to the coordinate
     *
     * @param x block x
     * @param y block y
     * @param z block z
     */
    void setBlock(int x, int y, int z, Block block);

    /**
     * @return the length of the schematic
     */
    short getLength();

    /**
     * @return the width of the schematic
     */
    short getWidth();

    /**
     * @return the height of the schematic
     */
    short getHeight();

    /**
     * @param file to test if is in good format
     * @return true if is in good format
     */
    @NotNull
    boolean isFormat(@NotNull File file);

    /**
     * @param point the location
     * @return true if a tile entities is saved
     */
    boolean hasTileEntities(Point point);

    /**
     * @param x x
     * @param y y
     * @param z z
     * @return true if a tile entities is saved
     */
    boolean hasTileEntities(int x, int y, int z);

    /**
     * @param point the location
     */
    String getTileEntitiesId(Point point);

    /**
     * @param x x
     * @param y y
     * @param z z
     */
    String getTileEntitiesId(int x, int y, int z);

    /**
     * @param point the location
     * @param id    the tile entities id
     * @return the nbt of the tile entities
     */
    NBTCompound getTileEntities(Point point, String id);

    /**
     * @param x  x
     * @param y  y
     * @param z  z
     * @param id the tile entities id
     * @return the nbt of the tile entities
     */
    NBTCompound getTileEntities(int x, int y, int z, String id);

    /**
     * @param file file to be saved
     * @return the current shem instance
     */
    Schematic save(@NotNull File file) throws IOException;
}
