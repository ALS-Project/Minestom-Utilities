package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.bounding.LecternBounding;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.LecternVisual;
import fr.bretzel.minestomstates.LecternState;
import net.minestom.server.instance.block.Block;

public class LecternBoxState extends BoxState<LecternState> {
    private final Shape bouding = new LecternBounding();

    public LecternBoxState(Block blockAlternative) {
        super(blockAlternative);

        setDefaultBox(new LecternVisual(getStates()));
    }

    @Override
    public Shape getBoundingBox() {
        return bouding;
    }
}
