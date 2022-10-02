package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.BellVisual;
import fr.bretzel.minestom.states.BellState;
import fr.bretzel.minestom.states.state.Attachment;
import net.minestom.server.instance.block.Block;

public class BellBoxState extends BoxState<BellState> {

    protected Shape visualBox;

    public BellBoxState(Block block) {
        super(block);
        this.visualBox = new BellVisual(getStates());
        setDefaultBox(getStates().getAttachment() == Attachment.FLOOR ? new Shape(visualBox.getMin(), visualBox.getMax()) : getVisualShape());
    }

    @Override
    public Shape getVisualShape() {
        return visualBox;
    }
}
