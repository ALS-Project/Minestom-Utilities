package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.CampfireBounding;
import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.CampFireVisual;
import fr.bretzel.minestom.states.CampfireState;
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
