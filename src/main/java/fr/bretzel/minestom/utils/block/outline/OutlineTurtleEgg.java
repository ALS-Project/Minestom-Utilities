package fr.bretzel.minestom.utils.block.outline;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.IntegerState;
import net.minestom.server.instance.block.Block;

public class OutlineTurtleEgg extends BlockStateShape<BlockState> {
    public OutlineTurtleEgg(Block alternative) {
        super(alternative);

        int pickle = states().get(IntegerState.Of("eggs"));

        if (pickle == 1)
            set(0.21875, 0.0, 0.21875, 0.78125, 0.4375, 0.78125);
        else set(0.0625, 0.0, 0.0625, 0.9375, 0.4375, 0.9375);
    }
}
