package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.ScaffoldingVisual;
import fr.bretzel.minestom.states.ScaffoldingState;
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
