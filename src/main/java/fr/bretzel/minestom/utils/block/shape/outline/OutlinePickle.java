package fr.bretzel.minestom.utils.block.shape.outline;

import fr.bretzel.minestom.utils.block.shape.shapes.BlockStateShape;
import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.IntegerState;
import net.minestom.server.instance.block.Block;

public class OutlinePickle extends BlockStateShape<BlockState> {
    public OutlinePickle(Block alternative) {
        super(alternative);

        int pickle = states().get(IntegerState.Of("pickles"));

        switch (pickle) {
            case 2 -> set(0.1875, 0.0, 0.1875, 0.8125, 0.375, 0.8125);
            case 3 -> set(0.125, 0.0, 0.125, 0.875, 0.375, 0.875);
            case 4 -> set(0.125, 0.0, 0.125, 0.875, 0.4375, 0.875);
            default -> set(0.375, 0.0, 0.375, 0.625, 0.375, 0.625);
        }
    }
}
