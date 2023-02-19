package fr.bretzel.minestom.utils.instance.generator;

import fr.bretzel.minestom.utils.instance.biomes.Biomes;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class VoidGenerator implements Generator {

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.modifier().fillBiome(Biomes.THE_VOID);

        final var minX = unit.absoluteStart().blockX();
        final var maxX = unit.absoluteEnd().blockX();

        final var minZ = unit.absoluteStart().blockZ();
        final var maxZ = unit.absoluteEnd().blockZ();

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                if (MathUtils.isBetween(x, -10, 10) && MathUtils.isBetween(z, -10, 10)) {
                    unit.modifier().setBlock(x, 64, z, Block.STONE);
                }
            }
        }
    }
}
