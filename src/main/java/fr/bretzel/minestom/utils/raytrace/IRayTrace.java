package fr.bretzel.minestom.utils.raytrace;


import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

public interface IRayTrace {
    public RayBlockResult rayTraceBlock(RayTraceContext context, Pos blockPosition, Vec offset);
}
