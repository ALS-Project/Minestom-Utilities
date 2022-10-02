package fr.bretzel.minestom.utils.block.shape.visual;

import fr.bretzel.minestom.utils.block.shape.shapes.BlockStateShapes;
import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.BooleanState;
import fr.bretzel.minestomstates.state.Directional;
import net.minestom.server.instance.block.Block;

public class PaneVisual extends BlockStateShapes<BlockState> {
    public PaneVisual(Block alternative) {
        super(alternative);

        add(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625);

        for (Directional directional : Directional.axis) {
            boolean connected = states().get(BooleanState.Of(directional.getKey()));

            if (connected) {
                switch (directional) {
                    case SOUTH -> add(0.4375, 0.0, 0.5625, 0.5625, 1.0, 1.0);
                    case NORTH -> add(0.4375, 0.0, 0.0, 0.5625, 1.0, 0.4375);
                    case WEST -> add(0.0, 0.0, 0.4375, 0.4375, 1.0, 0.5625);
                    case EAST -> add(0.5625, 0.0, 0.4375, 1.0, 1.0, 0.5625);
                }
            }
        }
    }
}
