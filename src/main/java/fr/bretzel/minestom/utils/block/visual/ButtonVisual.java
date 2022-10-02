package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Face;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;

public class ButtonVisual extends BlockStateShape<BlockState> {
    public ButtonVisual(Block alternative) {
        super(alternative);

        Facing facing = states().get(Facing.class);
        Face face = states().get(Face.class);

        if (face == Face.WALL) {
            switch (facing) {
                case EAST -> set(0.0, 0.375, 0.3125, 0.125, 0.625, 0.6875);
                case WEST -> set(0.875, 0.375, 0.3125, 1.0, 0.625, 0.6875);
                case NORTH -> set(0.3125, 0.375, 0.875, 0.6875, 0.625, 1.0);
                case SOUTH -> set(0.3125, 0.375, 0.0, 0.6875, 0.625, 0.125);
            }
        } else if (face == Face.CEILING) {
            switch (facing) {
                case SOUTH, NORTH -> set(0.3125, 0.875, 0.375, 0.6875, 1.0, 0.625);
                case WEST, EAST -> set(0.375, 0.875, 0.3125, 0.625, 1.0, 0.6875);
            }
        } else if (face == Face.FLOOR) {
            switch (facing) {
                case SOUTH, NORTH -> set(0.3125, 0.0, 0.375, 0.6875, 0.125, 0.625);
                case WEST, EAST -> set(0.375, 0.0, 0.3125, 0.625, 0.125, 0.6875);
            }
        }
    }
}
