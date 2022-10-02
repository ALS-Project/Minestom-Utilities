package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.ScaffoldingVisual;
import fr.bretzel.minestomstates.ScaffoldingState;
import net.minestom.server.instance.block.Block;

public class ScaffoldingBoxState extends BoxState<ScaffoldingState> {

    public ScaffoldingBoxState(Block blockAlternative) {
        super(blockAlternative);

        setDefaultBox(new ScaffoldingVisual(blockAlternative));
    }

    @Override
    public Shape getBoundingBox() {
        return VoidShape.VOID_SHAPE;
    }
}
