package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.IntegerState;
import net.minestom.server.instance.block.Block;

public class CakeVisual extends BlockStateShape<BlockState> {
    public CakeVisual(Block alternative) {
        super(alternative);
        int bites = states().get(IntegerState.Of("bites"));

        switch (bites) {
            case 1 -> set(0.1875, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
            case 2 -> set(0.3125, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
            case 3 -> set(0.4375, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
            case 4 -> set(0.5625, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
            case 5 -> set(0.6875, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
            case 6 -> set(0.8125, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
            default -> set(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
        }
    }
}
