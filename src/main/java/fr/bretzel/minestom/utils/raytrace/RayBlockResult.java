package fr.bretzel.minestom.utils.raytrace;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.BlockFace;

public record RayBlockResult(Point hitLocation, RayTraceContext context, BlockFace blockFace, BlockShape hitBlockShape) {
}
