package fr.bretzel.minestom.utils.instance.generator;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class FlatGenerator implements Generator {

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.modifier().fillHeight(0, 1, Block.BEDROCK);
        unit.modifier().fillHeight(2, 6, Block.DIRT);
        unit.modifier().fillHeight(7, 7, Block.GRASS_BLOCK);
    }
}
