package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.outline.OutlineTorch;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.VisualTorch;
import fr.bretzel.minestom.states.BlockState;
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
