package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.ButtonVisual;
import fr.bretzel.minestom.states.BlockState;
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
