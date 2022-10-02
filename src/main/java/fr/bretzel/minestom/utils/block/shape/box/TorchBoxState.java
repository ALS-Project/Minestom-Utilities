package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.outline.OutlineTorch;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.VisualTorch;
import fr.bretzel.minestomstates.BlockState;
import net.minestom.server.instance.block.Block;

public class TorchBoxState extends BoxState<BlockState> {

    public final Shape visual;
    public final Shape outline;

    public TorchBoxState(Block blockAlternative) {
        super(blockAlternative);
        this.visual = new VisualTorch(blockAlternative);
        this.outline = new OutlineTorch(blockAlternative);
    }

    @Override
    public Shape getBoundingBox() {
        return getOutlineShape();
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }

    @Override
    public Shape getOutlineShape() {
        return outline;
    }
}
