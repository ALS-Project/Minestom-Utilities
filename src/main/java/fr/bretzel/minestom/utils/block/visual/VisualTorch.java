package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;

public class VisualTorch extends BlockStateShape<BlockState> {
    public VisualTorch(Block alternative) {
        super(alternative);
        boolean isWall = alternative.name().contains("wall");
        if (isWall) {
            Facing facing = states().get(Facing.class);
            switch (facing) {
                case WEST -> set(0.6875, 0.1875, 0.34375, 1.0, 0.8125, 0.65625);
                case EAST -> set(0.0, 0.1875, 0.34375, 0.3125, 0.8125, 0.65625);
                case SOUTH -> set(0.34375, 0.1875, 0.0, 0.65625, 0.8125, 0.3125);
                case NORTH -> set(0.34375, 0.1875, 0.6875, 0.65625, 0.8125, 1.0);
            }
        } else {
            set(0.4375, 0.0, 0.4375, 0.5625, 0.625, 0.5625);
        }
    }
}
