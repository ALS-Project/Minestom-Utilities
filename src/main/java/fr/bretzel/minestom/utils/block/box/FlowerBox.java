package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.outline.OutlineFlower;
import fr.bretzel.minestom.utils.block.Box;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import net.minestom.server.instance.block.Block;

import java.util.Arrays;
import java.util.List;

public class FlowerBox extends Box {
    public static final List<Block> flowers = Arrays.asList(Block.DANDELION, Block.POPPY, Block.BLUE_ORCHID, Block.ALLIUM,
            Block.AZURE_BLUET, Block.RED_TULIP, Block.ORANGE_TULIP, Block.WHITE_TULIP, Block.PINK_TULIP, Block.OXEYE_DAISY,
            Block.CORNFLOWER, Block.LILY_OF_THE_VALLEY, Block.WITHER_ROSE);

    public FlowerBox() {
        setDefaultBox(new OutlineFlower());
    }

    @Override
    public Shape getBoundingBox() {
        return VoidShape.VOID_SHAPE;
    }
}
