package fr.bretzel.minestom.utils.raytrace;


import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

public abstract class IRayTrace {
    abstract RayBlockResult rayTraceBlock(RayTraceContext context, Point blockPosition, Point offset);

    public double epsilon(double value) {
        return Math.abs(value) < Vec.EPSILON ? 0 : value;
    }
}
