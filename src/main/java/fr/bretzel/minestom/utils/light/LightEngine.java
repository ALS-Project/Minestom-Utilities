package fr.bretzel.minestom.utils.light;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.Section;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Copyright Waterdev 2022, under the MIT License
 * Modified by MrBretzel 2022
 * https://github.com/CutestNekoAqua/MinestomBasicLight
 */

public class LightEngine {

    //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L25
    public static final int ARRAY_SIZE = 16 * 16 * 16 / (8 / 4); // blocks / bytes per block
    private final SectionUtils utils = new SectionUtils();
    private final byte fullbright = 15; // 15
    private final byte half = 10; // 10 half
    private final byte dark = 7; // 7 no light/air light
    byte[] skyArray;
    byte[] blockArray;

    public void recalculateChunk(Chunk chunk, Player... players) {
        List<Section> sections = new ArrayList<>(chunk.getSections());
        Collections.reverse(sections);
        for (Section section : sections) {
            recalculateSection(section);
        }

        updateClientLightPacket(chunk, players);
    }

    public void updateClientLightPacket(Chunk chunk, Player... players) {
        for (Player player : players) {
            chunk.sendChunk(player);
        }
    }

    public void recalculateSection(Section section) {
        boolean[][] exposed = new boolean[16][16];

        for (boolean[] booleans : exposed)
            Arrays.fill(booleans, true);

        blockArray = new byte[ARRAY_SIZE];
        skyArray = new byte[ARRAY_SIZE];

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 15; y > -1; y--) {
                    set(blockArray, utils.getCoordIndex(x, y, z), fullbright);
                }
            }
        }

        section.setSkyLight(blockArray);
        section.setBlockLight(blockArray);
    }

    public void recalculateInstance(Instance instance, Player... players) {
        instance.getChunks().forEach((chunk -> recalculateChunk(chunk, players)));
    }

    private Block nonNullBlock(int id) {
        Block block = Block.fromBlockId(id);
        return block == null ? Block.AIR : block;
    }

    // operation type: updating
    private void set(final byte[] array, final int x, final int y, final int z, final int lightLevel) {
        this.set(array, (x & 15) | ((z & 15) << 4) | ((y & 15) << 8), lightLevel);
    }

    //https://github.com/PaperMC/Starlight/blob/6503621c6fe1b798328a69f1bca784c6f3ffcee3/src/main/java/ca/spottedleaf/starlight/common/light/SWMRNibbleArray.java#L410
    // operation type: updating
    private void set(final byte[] array, final int index, final int lightLevel) {
        final int shift = (index & 1) << 2;
        final int i = index >>> 1;

        array[i] = (byte) ((array[i] & (0xF0 >>> shift)) | (lightLevel << shift));
    }
}
