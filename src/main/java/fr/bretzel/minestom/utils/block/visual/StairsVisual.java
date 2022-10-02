package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.states.StairsState;
import fr.bretzel.minestom.states.state.Half;
import net.minestom.server.instance.block.Block;

public class StairsVisual extends BlockStateShapes<StairsState> {
    public StairsVisual(Block alternative) {
        super(alternative);

        if (states().getHalf() == Half.TOP) {
            add(0, 0.5, 0, 1, 1, 1);
        } else {
            add(0, 0, 0, 1, 0.5, 1);
        }

        switch (states().getShape()) {
            case STRAIGHT -> {
                switch (states().getFacing()) {
                    case NORTH -> add(0, 0, 0, 1, 1, 0.5);
                    case SOUTH -> add(0, 0, 0.5, 1, 1, 1);
                    case WEST -> add(0, 0, 0, 0.5, 1, 1);
                    case EAST -> add(0.5, 0, 0, 1, 1, 1);
                }
            }
            case OUTER_LEFT -> {
                switch (states().getFacing()) {
                    case SOUTH -> add(0.5, 0, 0.5, 1, 1, 1);
                    case NORTH -> add(0, 0, 0, 0.5, 1, 0.5);
                    case EAST -> add(0.5, 0, 0, 1, 1, 0.5);
                    case WEST -> add(0, 0, 0.5, 0.5, 1, 1);
                }
            }
            case OUTER_RIGHT -> {
                switch (states().getFacing()) {
                    case SOUTH -> add(0, 0, 0.5, 0.5, 1, 1);
                    case NORTH -> add(0.5, 0, 0, 1, 1, 0.5);
                    case EAST -> add(0.5, 0, 1, 1, 1, 0.5);
                    case WEST -> add(0, 0, 0, 0.5, 1, 0.5);
                }
            }
            case INNER_LEFT -> {
                double baseY = states().getHalf() == Half.BOTTOM ? 0 : -0.5;
                switch (states().getFacing().opposite()) {
                    case SOUTH -> {
                        add(0.0, baseY + 0.5, 0.0, 0.5, baseY + 1.0, 1.0);
                        add(0.5, baseY + 0.5, 0.0, 1.0, baseY + 1.0, 0.5);
                    }
                    case NORTH -> {
                        add(0.5, baseY + 0.5, 0.0, 1.0, baseY + 1.0, 1.0);
                        add(0.0, baseY + 0.5, 0.5, 0.5, baseY + 1.0, 1.0);
                    }
                    case EAST -> {
                        add(0.0, baseY + 0.5, 0.0, 0.5, baseY + 1.0, 1.0);
                        add(0.5, baseY + 0.5, 0.5, 1.0, baseY + 1.0, 1.0);
                    }
                    case WEST -> {
                        add(0.5, baseY + 0.5, 0.0, 1.0, baseY + 1.0, 1.0);
                        add(0.0, baseY + 0.5, 0.0, 0.5, baseY + 1.0, 0.5);
                    }
                }
            }
            case INNER_RIGHT -> {
                double baseY = states().getHalf() == Half.BOTTOM ? 0 : -0.5;
                switch (states().getFacing().opposite()) {
                    case SOUTH -> {
                        add(0.5, baseY + 0.5, 0.0, 1.0, baseY + 1.0, 1.0);
                        add(0.0, baseY + 0.5, 0.0, 0.5, baseY + 1.0, 0.5);
                    }
                    case WEST -> {
                        add(0.5, baseY + 0.5, 0.0, 1.0, baseY + 1.0, 1.0);
                        add(0.0, baseY + 0.5, 0.5, 0.5, baseY + 1.0, 1.0);
                    }
                    case NORTH -> {
                        add(0.0, baseY + 0.5, 0.0, 0.5, baseY + 1.0, 1.0);
                        add(0.5, baseY + 0.5, 0.5, 1.0, baseY + 1.0, 1.0);
                    }
                    case EAST -> {
                        add(0.0, baseY + 0.5, 0.0, 0.5, baseY + 1.0, 1.0);
                        add(0.5, baseY + 0.5, 0.0, 1.0, baseY + 1.0, 0.5);
                    }
                }
            }
        }
    }
}
