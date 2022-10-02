package fr.bretzel.minestom.utils.block.shape.visual;

import fr.bretzel.minestom.utils.block.shape.shapes.BlockStateShapes;
import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.BooleanState;
import fr.bretzel.minestomstates.state.Directional;
import net.minestom.server.instance.block.Block;

public class FenceVisual extends BlockStateShapes<BlockState> {
    public FenceVisual(Block alternative) {
        super(alternative);

        add(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);

        for (Directional directional : Directional.axis) {
            boolean connected = states().get(BooleanState.Of(directional.getKey()));
            if (connected) {
                switch (directional) {
                    case SOUTH -> {
                        add(0.4375, 0.75, 0.4375, 0.5625, 0.9375, 1.0);
                        add(0.4375, 0.375, 0.4375, 0.5625, 0.5625, 1.0);
                    }
                    case NORTH -> {
                        add(0.4375, 0.75, 0.0, 0.5625, 0.9375, 0.5625);
                        add(0.4375, 0.375, 0.0, 0.5625, 0.5625, 0.5625);
                    }
                    case WEST -> {
                        add(0.0, 0.75, 0.4375, 0.5625, 0.9375, 0.5625);
                        add(0.0, 0.375, 0.4375, 0.5625, 0.5625, 0.5625);
                    }
                    case EAST -> {
                        add(0.4375, 0.75, 0.4375, 1.0, 0.9375, 0.5625);
                        add(0.4375, 0.375, 0.4375, 1.0, 0.5625, 0.5625);
                    }
                }
            }
        }
    }
}
