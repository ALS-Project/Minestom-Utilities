package fr.bretzel.minestom.utils.block.bounding;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.states.PistonHeadState;
import net.minestom.server.instance.block.Block;

public class PistonHeadBox extends BlockStateShapes<PistonHeadState> {

    public PistonHeadBox(Block block) {
        super(block);

        switch (states().getFacing()) {
            case NORTH -> {
                add(0.0, 0.0, 0.0, 1.0, 1.0, 0.25);
                add(0.375, 0.375, 0.25, 0.625, 0.625, 1.25);
            }
            case SOUTH -> {
                add(0.0, 0.0, 0.75, 1.0, 1.0, 1.0);
                add(0.375, 0.375, -0.25, 0.625, 0.625, 0.75);
            }
            case EAST -> {
                add(0.75, 0.0, 0.0, 1.0, 1.0, 1.0);
                add(-0.25, 0.375, 0.375, 0.75, 0.625, 0.625);
            }
            case WEST -> {
                add(0.0, 0.0, 0.0, 0.25, 1.0, 1.0);
                add(0.25, 0.375, 0.375, 1.25, 0.625, 0.625);
            }
            case UP -> {
                add(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
                add(0.375, -0.25, 0.375, 0.625, 0.75, 0.625);
            }
            case DOWN -> {
                add(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
                add(0.375, 0.25, 0.375, 0.625, 1.25, 0.625);
            }
        }
    }
}
