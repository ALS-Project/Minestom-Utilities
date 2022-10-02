package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.CampfireState;

public class CampFireVisual extends BlockStateShapes<CampfireState> {

    public CampFireVisual(BlockState blockState) {
        super(blockState);

        switch (states().getFacing()) {
            case NORTH, SOUTH -> {
                add(0.0625, 0.0, 0.0, 0.3125, 0.25, 1.0);
                add(0.0, 0.1875, 0.6875, 1.0, 0.4375, 0.9375);
                add(0.6875, 0.0, 0.0, 0.9375, 0.25, 1.0);
                add(0.0, 0.1875, 0.0625, 1.0, 0.4375, 0.3125);
                add(0.3125, 0.0, 0.0, 0.6875, 0.0625, 1.0);
            }
            case WEST, EAST -> {
                add(0.0, 0.0, 0.0625, 1.0, 0.25, 0.3125);
                add(0.0625, 0.1875, 0.0, 0.3125, 0.4375, 1.0);
                add(0.0, 0.0, 0.6875, 1.0, 0.25, 0.9375);
                add(0.6875, 0.1875, 0.0, 0.9375, 0.4375, 1.0);
                add(0.0, 0.0, 0.3125, 1.0, 0.0625, 0.6875);
            }
        }
    }
}
