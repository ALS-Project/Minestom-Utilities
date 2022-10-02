package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;

public class LadderVisual extends BlockStateShapes<BlockState> {

    public LadderVisual(Block alternative) {
        super(alternative);

        Facing facing = states().get(Facing.class);

        switch (facing) {
            case EAST -> {
                add(0.0, 0.0, 0.75, 0.125, 1.0, 0.875);
                add(0.0, 0.0, 0.125, 0.125, 1.0, 0.25);
                add(0.0625, 0.8125, 0.0625, 0.1875, 0.9375, 0.9375);
                add(0.0625, 0.5625, 0.0625, 0.1875, 0.6875, 0.9375);
                add(0.0625, 0.3125, 0.0625, 0.1875, 0.4375, 0.9375);
                add(0.0625, 0.0625, 0.0625, 0.1875, 0.1875, 0.9375);
            }
            case WEST -> {
                add(0.875, 0.0, 0.75, 1.0, 1.0, 0.875);
                add(0.875, 0.0, 0.125, 1.0, 1.0, 0.25);
                add(0.8125, 0.8125, 0.0625, 0.9375, 0.9375, 0.9375);
                add(0.8125, 0.5625, 0.0625, 0.9375, 0.6875, 0.9375);
                add(0.8125, 0.3125, 0.0625, 0.9375, 0.4375, 0.9375);
                add(0.8125, 0.0625, 0.0625, 0.9375, 0.1875, 0.9375);
            }
            case SOUTH -> {
                add(0.125, 0.0, 0.0, 0.25, 1.0, 0.125);
                add(0.75, 0.0, 0.0, 0.875, 1.0, 0.125);
                add(0.0625, 0.8125, 0.0625, 0.9375, 0.9375, 0.1875);
                add(0.0625, 0.5625, 0.0625, 0.9375, 0.6875, 0.1875);
                add(0.0625, 0.3125, 0.0625, 0.9375, 0.4375, 0.1875);
                add(0.0625, 0.0625, 0.0625, 0.9375, 0.1875, 0.1875);
            }
            case NORTH -> {
                add(0.125, 0.0, 0.875, 0.25, 1.0, 1.0);
                add(0.75, 0.0, 0.875, 0.875, 1.0, 1.0);
                add(0.0625, 0.8125, 0.8125, 0.9375, 0.9375, 0.9375);
                add(0.0625, 0.5625, 0.8125, 0.9375, 0.6875, 0.9375);
                add(0.0625, 0.3125, 0.8125, 0.9375, 0.4375, 0.9375);
                add(0.0625, 0.0625, 0.8125, 0.9375, 0.1875, 0.9375);
            }
        }
    }
}
