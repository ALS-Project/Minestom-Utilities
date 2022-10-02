package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.bounding.BambooBounding;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.BambooVisual;
import fr.bretzel.minestomstates.BlockState;
import net.minestom.server.instance.block.Block;

public class BambooBoxState extends BoxState<BlockState> {
    private final BambooVisual visual;

    public BambooBoxState(Block block) {
        super(block);
        setDefaultBox(new BambooBounding(block));
        this.visual = new BambooVisual(block);
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }
}
