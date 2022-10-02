package fr.bretzel.minestom.utils.block.shape.shapes;

import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.BlockStateManager;
import net.minestom.server.instance.block.Block;

public class BlockStateShapes<S extends BlockState> extends Shapes {

    private final S blockStates;

    public BlockStateShapes(Block alternative) {
        this.blockStates = (S) BlockStateManager.get(alternative);
    }

    public BlockStateShapes(BlockState blockState) {
        this.blockStates = (S) blockState;
    }

    public S states() {
        return blockStates;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "blockStates=" + blockStates +
                '}';
    }
}
