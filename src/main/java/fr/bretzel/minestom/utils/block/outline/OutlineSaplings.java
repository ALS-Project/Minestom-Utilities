package fr.bretzel.minestom.utils.block.outline;


import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.raytrace.OffsetType;

public class OutlineSaplings extends Shape {
    public OutlineSaplings() {
        super(0.25, 0.0, 0.25, 0.75, 0.75, 0.75);
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
