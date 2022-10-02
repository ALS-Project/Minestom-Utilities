package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.LecternState;

public class LecternVisual extends BlockStateShapes<LecternState> {
    public LecternVisual(BlockState blockState) {
        super(blockState);

        switch (states().getFacing()) {
            case EAST -> {
                add(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
                add(0.25, 0.125, 0.25, 0.75, 0.875, 0.75);
                add(0.6875, 0.625, 0.0, 0.9375, 0.875, 1.0);
                add(0.4375, 0.75, 0.0, 0.6875, 1.0, 1.0);
                add(0.1875, 0.875, 0.0, 0.4375, 1.125, 1.0);
            }
            case WEST -> {
                add(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
                add(0.25, 0.125, 0.25, 0.75, 0.875, 0.75);
                add(0.0625, 0.625, 0.0, 0.3125, 0.875, 1.0);
                add(0.3125, 0.75, 0.0, 0.5625, 1.0, 1.0);
                add(0.5625, 0.875, 0.0, 0.8125, 1.125, 1.0);
            }
            case SOUTH -> {
                add(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
                add(0.25, 0.125, 0.25, 0.75, 0.875, 0.75);
                add(0.0, 0.625, 0.6875, 1.0, 0.875, 0.9375);
                add(0.0, 0.75, 0.4375, 1.0, 1.0, 0.6875);
                add(0.0, 0.875, 0.1875, 1.0, 1.125, 0.4375);
            }
            case NORTH -> {
                add(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
                add(0.25, 0.125, 0.25, 0.75, 0.875, 0.75);
                add(0.0, 0.625, 0.0625, 1.0, 0.875, 0.3125);
                add(0.0, 0.75, 0.3125, 1.0, 1.0, 0.5625);
                add(0.0, 0.875, 0.5625, 1.0, 1.125, 0.8125);
            }
        }
    }
}
