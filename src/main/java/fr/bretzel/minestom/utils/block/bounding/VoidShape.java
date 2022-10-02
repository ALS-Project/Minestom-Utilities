package fr.bretzel.minestom.utils.block.bounding;

import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.raytrace.RayBlockResult;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

public class VoidShape extends Shape {

    public static final Shape VOID_SHAPE = new VoidShape();

    private VoidShape() {
        super(0, 0, 0, 1, 1, 1);
    }

    @Override
    public boolean isInside(Vec position) {
        return false;
    }

    @Override
    public RayBlockResult rayTraceBlocks(RayTraceContext context, Point blockPosition, Point offset) {
        return null;
    }
}
