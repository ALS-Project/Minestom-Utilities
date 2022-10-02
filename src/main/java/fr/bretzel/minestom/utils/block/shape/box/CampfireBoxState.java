package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.bounding.CampfireBounding;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.CampFireVisual;
import fr.bretzel.minestomstates.CampfireState;
import net.minestom.server.instance.block.Block;

public class CampfireBoxState extends BoxState<CampfireState> {

    private final Shape visualBox = new CampFireVisual(getStates());

    public CampfireBoxState(Block blockAlternative) {
        super(blockAlternative);
        setDefaultBox(new CampfireBounding());
    }

    @Override
    public Shape getVisualShape() {
        return visualBox;
    }
}
