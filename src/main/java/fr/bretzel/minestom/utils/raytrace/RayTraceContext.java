package fr.bretzel.minestom.utils.raytrace;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;

public record RayTraceContext(Instance instance, Point start, Point end, Vec direction, BoundingBox precision,
                              BlockMode blockMode, FluidMode fluidMode) {

    public RayTraceContext(Entity entity, double distance) {
        this(entity.getInstance(), entity.getPosition().withY(y -> y + entity.getEyeHeight()).asVec(), entity.getPosition().withY(y -> y + entity.getEyeHeight()).asVec().normalize().mul(distance), entity.getPosition().direction(), RayTrace.ZERO, BlockMode.OUTLINE, FluidMode.ANY);
    }

    public RayTraceContext(Entity entity, double distance, BlockMode blockMode) {
        this(entity.getInstance(), entity.getPosition().withY(y -> y + entity.getEyeHeight()).asVec(), entity.getPosition().withY(y -> y + entity.getEyeHeight()).asVec().normalize().mul(distance), entity.getPosition().direction(), RayTrace.ZERO, blockMode, FluidMode.NONE);
    }

    public RayTraceContext(Entity entity, double distance, BlockMode blockMode, FluidMode fluidMode) {
        this(entity.getInstance(), entity.getPosition().withY(y -> y + entity.getEyeHeight()).asVec(), entity.getPosition().withY(y -> y + entity.getEyeHeight()).asVec().normalize().mul(distance), entity.getPosition().direction(), RayTrace.ZERO, blockMode, fluidMode);
    }

    public double distance() {
        return start.distance(end);
    }
}
