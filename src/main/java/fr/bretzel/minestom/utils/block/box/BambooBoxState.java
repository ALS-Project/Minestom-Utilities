package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.bounding.BambooBounding;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.BambooVisual;
import fr.bretzel.minestom.states.BlockState;
import net.minestom.server.instance.block.Block;

public class BambooBoxState extends BoxState<BlockState> {
    private final BambooVisual visual;

    public BambooBoxState(Block block) {
        super(block);
        setDefaultBox(new BambooBounding(block));
        this.visual = new BambooVisual(block);
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }
}
