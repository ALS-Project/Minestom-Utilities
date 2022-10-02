package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.instance.block.Block;

public class EndRodVisual extends BlockStateShapes<BlockState> {
    public EndRodVisual(Block alternative) {
        super(alternative);

        Facing facing = states().get(Facing.class);

        switch (facing) {
            case NORTH -> {
                add(0.375, 0.375, 0.9375, 0.625, 0.625, 1.0);
                add(0.4375, 0.4375, 0.0, 0.5625, 0.5625, 0.9375);
            }
            case SOUTH -> {
                add(0.375, 0.375, 0.0, 0.625, 0.625, 0.0625);
                add(0.4375, 0.4375, 0.0625, 0.5625, 0.5625, 1.0);
            }
            case EAST -> {
                add(0.0, 0.375, 0.375, 0.0625, 0.625, 0.625);
                add(0.0625, 0.4375, 0.4375, 1.0, 0.5625, 0.5625);
            }
            case WEST -> {
                add(0.9375, 0.375, 0.375, 1.0, 0.625, 0.625);
                add(0.0, 0.4375, 0.4375, 0.9375, 0.5625, 0.5625);
            }
            case UP -> {
                add(0.375, 0.0, 0.375, 0.625, 0.0625, 0.625);
                add(0.4375, 0.0625, 0.4375, 0.5625, 1.0, 0.5625);
            }
            case DOWN -> {
                add(0.375, 0.9375, 0.375, 0.625, 1.0, 0.625);
                add(0.4375, 0.0, 0.4375, 0.5625, 0.9375, 0.5625);
            }
        }
    }
}
