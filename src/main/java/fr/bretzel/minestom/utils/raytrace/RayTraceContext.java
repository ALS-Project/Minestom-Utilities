package fr.bretzel.minestom.utils.raytrace;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record RayTraceContext(Instance instance, Vec start, Vec end, Vec direction, BlockMode blockMode,
                              FluidMode fluidMode) {

    public RayTraceContext(Player player, double distance) {
        this(player.getInstance(), player.getPosition().withY(y -> y + player.getEyeHeight()).asVec(), player.getPosition().withY(y -> y + player.getEyeHeight()).asVec().normalize().mul(distance), player.getPosition().direction(), BlockMode.OUTLINE, FluidMode.ANY);
    }

    public RayTraceContext(Player player, int distance, BlockMode blockMode, FluidMode fluidMode) {
        this(player.getInstance(), player.getPosition().withY(y -> y + player.getEyeHeight()).asVec(), player.getPosition().withY(y -> y + player.getEyeHeight()).asVec().normalize().mul(distance), player.getPosition().direction(), blockMode, fluidMode);

    }

    public double distance() {
        return start.distance(end);
    }
}
