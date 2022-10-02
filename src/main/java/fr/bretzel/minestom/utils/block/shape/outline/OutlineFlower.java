package fr.bretzel.minestom.utils.block.shape.outline;

import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.raytrace.OffsetType;

public class OutlineFlower extends Shape {
    public OutlineFlower() {
        super(new Shape(0.3125, 0.0, 0.3125, 0.6875, 0.625, 0.6875));
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
