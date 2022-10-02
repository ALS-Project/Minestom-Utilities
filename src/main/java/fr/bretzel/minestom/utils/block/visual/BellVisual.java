package fr.bretzel.minestom.utils.block.visual;


import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.states.BellState;

public class BellVisual extends BlockStateShapes<BellState> {

    public BellVisual(BellState state) {
        super(state);

        //The midle bell
        add(new Shape(0.3125, 0.375, 0.3125, 0.6875, 0.8125, 0.6875));
        add(new Shape(0.25, 0.25, 0.25, 0.75, 0.375, 0.75));

        switch (states().getAttachment()) {
            case FLOOR -> {
                switch (states().getFacing()) {
                    case NORTH, SOUTH -> {
                        add(0.125, 0.8125, 0.4375, 0.875, 0.9375, 0.5625);
                        add(0.875, 0.0, 0.375, 1.0, 1.0, 0.625);
                        add(0.0, 0.0, 0.375, 0.125, 1.0, 0.625);
                    }
                    case WEST, EAST -> {
                        add(0.4375, 0.8125, 0.125, 0.5625, 0.9375, 0.875);
                        add(0.375, 0.0, 0.0, 0.625, 1.0, 0.125);
                        add(0.375, 0.0, 0.875, 0.625, 1.0, 1.0);
                    }
                }
            }
            case CEILING -> add(0.4375, 0.8125, 0.4375, 0.5625, 1.0, 0.5625);
            case DOUBLE_WALL -> {
                switch (states().getFacing()) {
                    case WEST, EAST -> add(0.0, 0.8125, 0.4375, 1.0, 0.9375, 0.5625);
                    case SOUTH, NORTH -> add(0.4375, 0.8125, 0.0, 0.5625, 0.9375, 1.0);
                }
            }
            case SINGLE_WALL -> {
                switch (states().getFacing().opposite()) {
                    case NORTH -> add(0.4375, 0.8125, 0.1875, 0.5625, 0.9375, 1.0);
                    case SOUTH -> add(0.4375, 0.8125, 0.0, 0.5625, 0.9375, 0.8125);
                    case WEST -> add(0.1875, 0.8125, 0.4375, 1.0, 0.9375, 0.5625);
                    case EAST -> add(0.0, 0.8125, 0.4375, 0.8125, 0.9375, 0.5625);
                }
            }
        }
    }
}
