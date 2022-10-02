package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.ButtonVisual;
import fr.bretzel.minestomstates.BlockState;
import net.minestom.server.instance.block.Block;

public class ButtonBoxState extends BoxState<BlockState> {

    private final Shape visual;

    public ButtonBoxState(Block blockAlternative) {
        super(blockAlternative);
        this.visual = new ButtonVisual(blockAlternative);
    }

    @Override
    public Shape getBoundingBox() {
        return VoidShape.VOID_SHAPE;
    }

    @Override
    public Shape getOutlineShape() {
        return visual;
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }
}
