package fr.bretzel.minestom.utils.raytrace;

import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class RayBlockResult {
    private final Point hit;
    private final RayTraceContext context;
    private final Instance instance;
    private final Facing blockFace;
    private final Point blockPosition;
    private final Shape hitShape;

    public RayBlockResult(Point blockPosition, Point hitLocation, RayTraceContext context, Facing facing, Shape hitShape) {
        this.hit = hitLocation.add(context.direction().mul(0.0001));;
        this.blockPosition = blockPosition;
        this.context = context;
        this.instance = context.instance();
        this.blockFace = facing;

        this.hitShape = hitShape;
    }

    public Block getHitBlock() {
        try {
            return getInstance().getBlock(getBlockPosition());
        } catch (Exception e) {
            return Block.VOID_AIR;
        }
    }

    public Point getHit() {
        return hit;
    }

    public Pos getBlockPosition() {
        return Pos.fromPoint(blockPosition);
    }

    public RayTraceContext getContext() {
        return context;
    }

    public Instance getInstance() {
        return instance;
    }

    public Facing getBlockFace() {
        return blockFace;
    }

    public Shape getHitShape() {
        return hitShape;
    }

    public void lines(double spacing, double distance, Consumer<Vec> consumer) {
        Stream.iterate(context.start(), location -> location.distance(context.start()) <= distance, location -> location.add(context.direction().normalize().mul(spacing)))
                .toList()
                .forEach(consumer);
    }
}
