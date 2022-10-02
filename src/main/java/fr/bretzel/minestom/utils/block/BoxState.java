package fr.bretzel.minestom.utils.block;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.BlockStateManager;
import net.minestom.server.instance.block.Block;

public abstract class BoxState<S extends BlockState> extends Box {
    protected final S blockStates;

    public BoxState(Block block) {
        this.blockStates = (S) BlockStateManager.get(block);
    }

    public Block getBlock() {
        return blockStates.block();
    }

    public S getStates() {
        return blockStates;
    }

    @Override
    public String toString() {
        return "BoxState{" +
                " blockStates=" + blockStates.toString() +
                ", bounding=" + getBoundingBox() +
                ", visual=" + getVisualShape() +
                ", outline=" + getOutlineShape() +
                '}';
    }
}
