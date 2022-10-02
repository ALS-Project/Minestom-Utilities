package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.BellVisual;
import fr.bretzel.minestomstates.BellState;
import fr.bretzel.minestomstates.state.Attachment;
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
