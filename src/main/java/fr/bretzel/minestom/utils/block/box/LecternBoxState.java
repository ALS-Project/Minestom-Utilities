package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.LecternBounding;
import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.LecternVisual;
import fr.bretzel.minestom.states.LecternState;
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
