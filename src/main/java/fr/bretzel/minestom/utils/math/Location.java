package fr.bretzel.minestom.utils.math;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.MathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.DoubleUnaryOperator;

public class Location {
    private final Instance instance;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    public Location(@NotNull Instance instance, @NotNull Point point) {
        this(instance, point.x(), point.y(), point.z(), point instanceof Pos pos ? pos.yaw() : 0, point instanceof Pos pos ? pos.pitch() : 0);
    }

    public Location(Instance instance, double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.instance = instance;
    }

    public static @NotNull Location fromPoint(@NotNull Instance instance, @NotNull Point point) {
        return new Location(instance, point);
    }

    /**
     * Fixes a yaw value that is not between -180.0F and 180.0F
     * So for example -1355.0F becomes 85.0F and 225.0F becomes -135.0F
     *
     * @param yaw The possible "wrong" yaw
     * @return a fixed yaw
     */
    private static float fixYaw(float yaw) {
        yaw = yaw % 360;
        if (yaw < -180.0F) {
            yaw += 360.0F;
        } else if (yaw > 180.0F) {
            yaw -= 360.0F;
        }
        return yaw;
    }

    /**
     * Changes the 3 coordinates of this position.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return a new position
     */
    @Contract(pure = true)
    public @NotNull Location withCoord(double x, double y, double z) {
        return new Location(instance, x, y, z, yaw, pitch);
    }

    @Contract(pure = true)
    public @NotNull Location withCoord(@NotNull Point point) {
        return withCoord(point.x(), point.y(), point.z());
    }

    @Contract(pure = true)
    public @NotNull Location withView(float yaw, float pitch) {
        return new Location(instance, x, y, z, yaw, pitch);
    }

    /**
     * Sets the yaw and pitch to point
     * in the direction of the point.
     */
    @Contract(pure = true)
    public @NotNull Location withDirection(@NotNull Point point) {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        final double x = point.x();
        final double z = point.z();
        if (x == 0 && z == 0) {
            return withPitch(point.y() > 0 ? -90f : 90f);
        }
        final double theta = Math.atan2(-x, z);
        final double xz = Math.sqrt(MathUtils.square(x) + MathUtils.square(z));
        final double _2PI = 2 * Math.PI;
        return withView((float) Math.toDegrees((theta + _2PI) % _2PI),
                (float) Math.toDegrees(Math.atan(-point.y() / xz)));
    }

    @Contract(pure = true)
    public @NotNull Location withYaw(float yaw) {
        return new Location(instance, x, y, z, yaw, pitch);
    }

    @Contract(pure = true)
    public @NotNull Location withYaw(@NotNull DoubleUnaryOperator operator) {
        return withYaw((float) operator.applyAsDouble(yaw));
    }

    @Contract(pure = true)
    public @NotNull Location withPitch(float pitch) {
        return new Location(instance, x, y, z, yaw, pitch);
    }

    @Contract(pure = true)
    public @NotNull Location withPitch(@NotNull DoubleUnaryOperator operator) {
        return withPitch((float) operator.applyAsDouble(pitch));
    }

    public Location withInstance(@Nullable Instance instance) {
        return new Location(instance, x(), y(), z(), yaw(), pitch());
    }

    public float pitch() {
        return pitch;
    }

    public float yaw() {
        return yaw;
    }

    public double z() {
        return z;
    }

    public double y() {
        return y;
    }

    public double x() {
        return x;
    }

    /**
     * Checks if two positions have a similar view (yaw/pitch).
     *
     * @param position the position to compare
     * @return true if the two positions have the same view
     */
    public boolean sameView(@NotNull Location position) {
        return Float.compare(position.yaw, yaw) == 0 &&
                Float.compare(position.pitch, pitch) == 0;
    }

    /**
     * Gets a unit-vector pointing in the direction that this Location is
     * facing.
     *
     * @return a vector pointing the direction of this location's {@link
     * #pitch() pitch} and {@link #yaw() yaw}
     */
    public @NotNull Vec direction() {
        final float rotX = yaw;
        final float rotY = pitch;
        final double xz = Math.cos(Math.toRadians(rotY));
        return new Vec(-xz * Math.sin(Math.toRadians(rotX)),
                -Math.sin(Math.toRadians(rotY)),
                xz * Math.cos(Math.toRadians(rotX)));
    }

    /**
     * Returns a new position based on this position fields.
     *
     * @param operator the operator deconstructing this object and providing a new position
     * @return the new position
     */
    @Contract(pure = true)
    public @NotNull Location apply(@NotNull Location.Operator operator) {
        return operator.apply(x, y, z, yaw, pitch);
    }


    @Contract(pure = true)
    public @NotNull Location withX(@NotNull DoubleUnaryOperator operator) {
        return new Location(instance, operator.applyAsDouble(x), y, z, yaw, pitch);
    }


    @Contract(pure = true)
    public @NotNull Location withX(double x) {
        return new Location(instance, x, y, z, yaw, pitch);
    }


    @Contract(pure = true)
    public @NotNull Location withY(@NotNull DoubleUnaryOperator operator) {
        return new Location(instance, x, operator.applyAsDouble(y), z, yaw, pitch);
    }


    @Contract(pure = true)
    public @NotNull Location withY(double y) {
        return new Location(instance, x, y, z, yaw, pitch);
    }


    @Contract(pure = true)
    public @NotNull Location withZ(@NotNull DoubleUnaryOperator operator) {
        return new Location(instance, x, y, operator.applyAsDouble(z), yaw, pitch);
    }


    @Contract(pure = true)
    public @NotNull Location withZ(double z) {
        return new Location(instance, x, y, z, yaw, pitch);
    }


    public @NotNull Location add(double x, double y, double z) {
        return new Location(instance, this.x + x, this.y + y, this.z + z, yaw, pitch);
    }


    public @NotNull Location add(@NotNull Point point) {
        return add(point.x(), point.y(), point.z());
    }


    public @NotNull Location add(double value) {
        return add(value, value, value);
    }


    public @NotNull Location sub(double x, double y, double z) {
        return new Location(instance, this.x - x, this.y - y, this.z - z, yaw, pitch);
    }


    public @NotNull Location sub(@NotNull Point point) {
        return sub(point.x(), point.y(), point.z());
    }


    public @NotNull Location sub(double value) {
        return sub(value, value, value);
    }


    public @NotNull Location mul(double x, double y, double z) {
        return new Location(instance, this.x * x, this.y * y, this.z * z, yaw, pitch);
    }


    public @NotNull Location mul(@NotNull Point point) {
        return mul(point.x(), point.y(), point.z());
    }


    public @NotNull Location mul(double value) {
        return mul(value, value, value);
    }


    public @NotNull Location div(double x, double y, double z) {
        return new Location(instance, this.x / x, this.y / y, this.z / z, yaw, pitch);
    }


    public @NotNull Location div(@NotNull Point point) {
        return div(point.x(), point.y(), point.z());
    }


    public @NotNull Location div(double value) {
        return div(value, value, value);
    }


    @Contract(pure = true)
    public @NotNull Vec asVec() {
        return new Vec(x, y, z);
    }

    public @NotNull Pos pos() {
        return new Pos(x, y, z, yaw, pitch);
    }

    public Instance instance() {
        return instance;
    }

    @FunctionalInterface
    public interface Operator {
        @NotNull Location apply(double x, double y, double z, float yaw, float pitch);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", instance=" + instance +
                '}';
    }
}
