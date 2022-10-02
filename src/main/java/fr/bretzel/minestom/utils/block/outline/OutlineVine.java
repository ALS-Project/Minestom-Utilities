package fr.bretzel.minestom.utils.block.outline;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.BooleanState;
import fr.bretzel.minestom.states.state.Directional;
import net.minestom.server.instance.block.Block;

public class OutlineVine extends BlockStateShapes<BlockState> {

    public OutlineVine(Block alternative) {
        super(alternative);

        Directional[] vineDirectional = new Directional[]{Directional.EAST, Directional.WEST, Directional.SOUTH, Directional.NORTH, Directional.UP};

        for (Directional directional : vineDirectional) {
            boolean connected = states().get(BooleanState.Of(directional.getKey()));
            if (connected)
                add(getBox(directional));
        }
    }

    private Shape getBox(Directional directional) {
        return switch (directional) {
            case EAST -> new Shape(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0);
            case NORTH -> new Shape(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625);
            case SOUTH -> new Shape(0.0, 0.0, 0.9375, 1.0, 1.0, 1.0);
            case UP -> new Shape(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
            case WEST -> new Shape(0.0, 0.0, 0.0, 0.0625, 1.0, 1.0);
            default -> new Shape();
        };
    }
}
