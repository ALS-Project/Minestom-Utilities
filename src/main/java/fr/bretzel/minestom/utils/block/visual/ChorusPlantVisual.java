package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.BooleanState;
import fr.bretzel.minestom.states.state.Directional;
import net.minestom.server.instance.block.Block;

public class ChorusPlantVisual extends BlockStateShapes<BlockState> {
    public ChorusPlantVisual(Block alternative) {
        super(alternative);

        add(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

        for (Directional directional : Directional.order_update) {
            boolean connected = states().get(BooleanState.Of(directional.getKey()));

            if (connected)
                add(getShape(directional));
        }
    }

    public Shape getShape(Directional directional) {
        return switch (directional) {
            case WEST -> new Shape(0.0, 0.25, 0.25, 0.25, 0.75, 0.75);
            case EAST -> new Shape(0.75, 0.25, 0.25, 1.0, 0.75, 0.75);
            case NORTH -> new Shape(0.25, 0.25, 0.0, 0.75, 0.75, 0.25);
            case SOUTH -> new Shape(0.25, 0.25, 0.75, 0.75, 0.75, 1.0);
            case DOWN -> new Shape(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
            case UP -> new Shape(0.25, 0.75, 0.25, 0.75, 1.0, 0.75);
            default -> new Shape(1, 1, 1, 1, 1, 1);
        };
    }
}
