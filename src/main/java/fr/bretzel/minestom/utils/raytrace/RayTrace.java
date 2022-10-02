package fr.bretzel.minestom.utils.raytrace;

import fr.bretzel.minestom.utils.TriFunction;
import fr.bretzel.minestom.utils.block.BoxManager;
import fr.bretzel.minestom.utils.block.bounding.VoidShape;
import fr.bretzel.minestom.utils.math.MathsUtils;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.block.BlockIterator;

import java.util.function.BiFunction;

public class RayTrace {

    public static RayBlockResult rayTraceBlock(RayTraceContext context) {
        return rayBlocks(context, (rayTraceContext, position, block) -> {
            var box = BoxManager.get(block);
            var shape = context.blockMode() == BlockMode.VISUAL ? box.getVisualShape() : context.blockMode() == BlockMode.BOUNDING ? box.getBoundingBox() : box.getOutlineShape();
            return shape.rayTraceBlocks(context, position, getOffset(position, shape.getOffsetType()));
        }, (context1, position) -> new RayBlockResult(position, position, context1, Facing.SELF, VoidShape.VOID_SHAPE));
    }

    protected static <T> T rayBlocks(RayTraceContext context, TriFunction<RayTraceContext, Point, Block, T> rayTrace, BiFunction<RayTraceContext, Point, T> miss) {
        var start = context.start();
        var end = context.end();

        if (start.samePoint(end))
            return miss.apply(context, start);

        var blockIterator = new BlockIterator(start, context.direction(), 0, MathsUtils.floor(context.distance()));
        var instance = context.instance();

        var lastCheck = Vec.ZERO;

        while (blockIterator.hasNext()) {
            var position = blockIterator.next();

            //If chunk is not loaded
            if (!instance.isChunkLoaded(position))
                break;

            //If chunk is in void
            if (instance.isInVoid(position))
                break;

            lastCheck = Vec.fromPoint(position);

            //Get the Block
            var block = instance.getBlock(position);

            //Skip air block
            if (block.isAir())
                continue;

            //RayTrace, null == no collision
            var result = rayTrace.apply(context, position, block);

            //Found a hit
            if (result != null)
                return result;
        }

        return miss.apply(context, lastCheck);
    }

    public static Vec getOffset(Point blockPosition, OffsetType offsetType) {
        if (offsetType == OffsetType.NONE) {
            return Vec.ZERO;
        } else {
            var rand = MathsUtils.getOffsetCoordinate(blockPosition.blockX(), 0, blockPosition.blockZ());
            return new Vec(((double) ((float) (rand & 15L) / 15.0F) - 0.5D) * 0.5D, offsetType == OffsetType.XYZ ? ((double) ((float) (rand >> 4 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double) ((float) (rand >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D);
        }
    }
}
