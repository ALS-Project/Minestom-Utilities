package fr.bretzel.minestom.utils.block.outline;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.BooleanState;
import fr.bretzel.minestom.states.state.Directional;
import net.minestom.server.instance.block.Block;

public class OutlineChorusPlant extends BlockStateShapes<BlockState> {
    public OutlineChorusPlant(Block alternative) {
        super(alternative);

        add(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125);

        for (Directional directional : Directional.order_update) {
            boolean connected = states().get(BooleanState.Of(directional.getKey()));

            if (connected)
                add(getShape(directional));
        }
    }

    public Shape getShape(Directional directional) {
        return switch (directional) {
            case WEST -> new Shape(0.0, 0.1875, 0.1875, 0.1875, 0.8125, 0.8125);
            case EAST -> new Shape(0.8125, 0.1875, 0.1875, 1.0, 0.8125, 0.8125);
            case NORTH -> new Shape(0.1875, 0.1875, 0.0, 0.8125, 0.8125, 0.1875);
            case SOUTH -> new Shape(0.1875, 0.1875, 0.8125, 0.8125, 0.8125, 1.0);
            case DOWN -> new Shape(0.1875, 0.0, 0.1875, 0.8125, 0.1875, 0.8125);
            case UP -> new Shape(0.1875, 0.8125, 0.1875, 0.8125, 1.0, 0.8125);
            default -> new Shape(1, 1, 1, 1, 1, 1);
        };
    }
}
