package fr.bretzel.minestom.utils.block.outline;

import fr.bretzel.minestom.utils.block.shapes.Shapes;

public class OutlineChorusFlower extends Shapes {
    public OutlineChorusFlower() {
        add(0.125, 0.875, 0.125, 0.875, 1.0, 0.875);
        add(0.0, 0.125, 0.125, 0.125, 0.875, 0.875);
        add(0.125, 0.125, 0.0, 0.875, 0.875, 0.125);
        add(0.125, 0.125, 0.875, 0.875, 0.875, 1.0);
        add(0.875, 0.125, 0.125, 1.0, 0.875, 0.875);
        add(0.125, 0.0, 0.125, 0.875, 0.875, 0.875);
    }
}
