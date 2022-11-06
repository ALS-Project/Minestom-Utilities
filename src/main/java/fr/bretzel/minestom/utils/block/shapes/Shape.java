package fr.bretzel.minestom.utils.block.shapes;

import fr.bretzel.minestom.states.state.Axis;
import fr.bretzel.minestom.states.state.Facing;
import fr.bretzel.minestom.utils.math.Edge;
import fr.bretzel.minestom.utils.math.MathsUtils;
import fr.bretzel.minestom.utils.raytrace.OffsetType;
import fr.bretzel.minestom.utils.raytrace.RayBlockResult;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Shape {

    private Edge[] edges = new Edge[0];
    private Vec[] points = new Vec[0];

    private OffsetType offsetType = OffsetType.NONE;
    //min and max points of hit box
    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    public Shape() {
        this(0, 0, 0, 1, 1, 1);
    }

    public Shape(Vec min, Vec max) {
        this(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }

    public Shape(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);

        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        refreshPointAndEdge();
    }

    public Shape(Shape shape) {
        this.maxX = Math.max(shape.minX, shape.maxX);
        this.maxY = Math.max(shape.minY, shape.maxY);
        this.maxZ = Math.max(shape.minZ, shape.maxZ);

        this.minX = Math.min(shape.minX, shape.maxX);
        this.minY = Math.min(shape.minY, shape.maxY);
        this.minZ = Math.min(shape.minZ, shape.maxZ);

        this.edges = shape.edges;
        this.points = shape.points;
        this.offsetType = shape.offsetType;
    }

    public Shape(BoundingBox boundingBox) {
        this(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
    }

    protected void refreshPointAndEdge() {
        this.points = new Vec[]{
                //Top Point
                new Vec(maxX, maxY, maxZ),//F Point   Index 0
                new Vec(minX, maxY, maxZ),//E Point   Index 1
                new Vec(minX, maxY, minZ),//H Point   Index 2
                new Vec(maxX, maxY, minZ),//G Point   Index 3

                //Bottom Point
                new Vec(minX, minY, minZ),//D Point    Index 4
                new Vec(minX, minY, maxX),//A Point    Index 5
                new Vec(maxX, minY, maxZ),//B Point    Index 6
                new Vec(maxX, minY, minZ) //C Point    Index 7
        };

        this.edges = new Edge[]{
                //Vertical Edge
                new Edge(minX, minY, minZ, minX, maxY, minZ), //D->H
                new Edge(maxX, minY, minZ, maxX, maxY, minZ), //C->G
                new Edge(maxX, minY, maxZ, maxX, maxY, maxZ), //B->F
                new Edge(minX, minY, maxX, minX, maxY, maxZ), //A->E

                //Horizontal Edge bottom
                new Edge(minX, minY, minZ, maxX, minY, minZ), //D->C
                new Edge(maxX, minY, minZ, maxX, minY, maxZ), //C->B
                new Edge(maxX, minY, maxZ, minX, minY, maxX), //B->A
                new Edge(minX, minY, maxX, minX, minY, minZ), //A->D

                //Horizontal Edge top
                new Edge(minX, maxY, minZ, maxX, maxY, minZ), //H->G
                new Edge(maxX, maxY, minZ, maxX, maxY, maxZ), //G->F
                new Edge(maxX, maxY, maxZ, minX, maxY, maxZ), //F->E
                new Edge(minX, maxY, maxZ, minX, maxY, minZ)  //E->H
        };

    }

    public OffsetType getOffsetType() {
        return offsetType;
    }

    public void setOffsetType(OffsetType offsetType) {
        this.offsetType = offsetType;
    }

    public double getHeight() {
        return getMaxY() - getMinY();
    }

    public double getWidthX() {
        return getMaxX() - getMinX();
    }

    public double getWidthZ() {
        return getMaxZ() - getMinZ();
    }

    public double getVolume() {
        return this.getHeight() * this.getWidthX() * this.getWidthZ();
    }

    public double getCenterX() {
        return getMinX() + this.getWidthX() * 0.5F;
    }

    public double getCenterY() {
        return getMinY() + this.getHeight() * 0.5F;
    }

    public double getCenterZ() {
        return getMinZ() + this.getWidthZ() * 0.5F;
    }

    public Vec getMax() {
        return new Vec(maxX, maxY, maxZ);
    }

    public Vec getMin() {
        return new Vec(minX, minY, minZ);
    }

    public void setMax(double x, double y, double z) {
        this.maxX = Math.max(maxX, x);
        this.maxY = Math.max(maxY, y);
        this.maxZ = Math.max(maxZ, z);
        refreshPointAndEdge();
    }

    public void setMin(double x, double y, double z) {
        this.minX = Math.min(minX, x);
        this.minY = Math.min(minY, y);
        this.minZ = Math.min(minZ, z);
        refreshPointAndEdge();
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getTranslatedMinX(Point blockPos, Point offset) {
        return offset.x() + blockPos.blockX() + getMinX();
    }

    public double getTranslatedMinY(Point blockPos, Point offset) {
        return offset.y() + blockPos.blockY() + getMinY();
    }

    public double getTranslatedMinZ(Point blockPos, Point offset) {
        return offset.z() + blockPos.blockZ() + getMinZ();
    }

    public double getTranslatedMaxX(Point blockPos, Point offset) {
        return offset.x() + blockPos.blockX() + getMaxX();
    }

    public double getTranslatedMaxY(Point blockPos, Point offset) {
        return offset.y() + blockPos.blockY() + getMaxY();
    }

    public double getTranslatedMaxZ(Point blockPos, Point offset) {
        return offset.z() + blockPos.blockZ() + getMaxZ();
    }

    public Vec getCenter() {
        return new Vec(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public boolean isInside(Pos position) {
        return isInside(position.asVec());
    }

    public boolean isInside(Vec position) {
        return position.x() >= getMinX() && position.x() <= getMaxX() &&
                position.y() >= getMinY() && position.y() <= getMaxY() &&
                position.z() >= getMinZ() && position.z() <= getMaxZ();
    }

    public boolean isInside(double v, double v1, double v2) {
        return isInside(new Vec(v, v1, v2));
    }

    @Nullable
    public RayBlockResult rayTraceBlocks(RayTraceContext context, Point blockPosition, Point offset) {
        var start = context.start();
        var direction = context.direction();
        var maxDistance = context.distance();

        if (maxDistance <= 0.0D)
            return null;

        // ray start:
        var startX = start.x();
        var startY = start.y();
        var startZ = start.z();

        // ray direction:
        var dir = MathsUtils.normalizeZeros(direction).normalize();
        var dirX = dir.x();
        var dirY = dir.y();
        var dirZ = dir.z();

        // saving a few divisions below:
        // Note: If one of the direction vector components is 0.0, these
        // divisions result in infinity. But this is not a problem.
        var divX = 1.0D / dirX;
        var divY = 1.0D / dirY;
        var divZ = 1.0D / dirZ;

        //redefine min and max coordinate
        final var minX = Math.min(getTranslatedMinX(blockPosition, offset), getTranslatedMaxX(blockPosition, offset));
        final var maxX = Math.max(getTranslatedMinX(blockPosition, offset), getTranslatedMaxX(blockPosition, offset));

        final var minY = Math.min(getTranslatedMinY(blockPosition, offset), getTranslatedMaxY(blockPosition, offset));
        final var maxY = Math.max(getTranslatedMinY(blockPosition, offset), getTranslatedMaxY(blockPosition, offset));

        final var minZ = Math.min(getTranslatedMinZ(blockPosition, offset), getTranslatedMaxZ(blockPosition, offset));
        final var maxZ = Math.max(getTranslatedMinZ(blockPosition, offset), getTranslatedMaxZ(blockPosition, offset));

        double tMin;
        double tMax;
        Facing hitBlockFaceMin;
        Facing hitBlockFaceMax;

        // intersections with x planes:
        if (dirX >= 0.0D) {
            tMin = (minX - startX) * divX;
            tMax = (maxX - startX) * divX;
            hitBlockFaceMin = Facing.WEST;
            hitBlockFaceMax = Facing.EAST;
        } else {
            tMin = (maxX - startX) * divX;
            tMax = (minX - startX) * divX;
            hitBlockFaceMin = Facing.EAST;
            hitBlockFaceMax = Facing.WEST;
        }

        // intersections with y planes:
        double tyMin;
        double tyMax;
        Facing hitBlockFaceYMin;
        Facing hitBlockFaceYMax;
        if (dirY >= 0.0D) {
            tyMin = (minY - startY) * divY;
            tyMax = (maxY - startY) * divY;
            hitBlockFaceYMin = Facing.DOWN;
            hitBlockFaceYMax = Facing.UP;
        } else {
            tyMin = (maxY - startY) * divY;
            tyMax = (minY - startY) * divY;
            hitBlockFaceYMin = Facing.UP;
            hitBlockFaceYMax = Facing.DOWN;
        }
        if ((tMin > tyMax) || (tMax < tyMin)) {
            return null;
        }
        if (tyMin > tMin) {
            tMin = tyMin;
            hitBlockFaceMin = hitBlockFaceYMin;
        }
        if (tyMax < tMax) {
            tMax = tyMax;
            hitBlockFaceMax = hitBlockFaceYMax;
        }

        // intersections with z planes:
        double tzMin;
        double tzMax;
        Facing hitBlockFaceZMin;
        Facing hitBlockFaceZMax;
        if (dirZ >= 0.0D) {
            tzMin = (minZ - startZ) * divZ;
            tzMax = (maxZ - startZ) * divZ;
            hitBlockFaceZMin = Facing.NORTH;
            hitBlockFaceZMax = Facing.SOUTH;
        } else {
            tzMin = (maxZ - startZ) * divZ;
            tzMax = (minZ - startZ) * divZ;
            hitBlockFaceZMin = Facing.SOUTH;
            hitBlockFaceZMax = Facing.NORTH;
        }
        if ((tMin > tzMax) || (tMax < tzMin)) {
            return null;
        }
        if (tzMin > tMin) {
            tMin = tzMin;
            hitBlockFaceMin = hitBlockFaceZMin;
        }
        if (tzMax < tMax) {
            tMax = tzMax;
            hitBlockFaceMax = hitBlockFaceZMax;
        }

        // intersections are behind the start:
        if (tMax < 0.0D) return null;
        // intersections are to far away:
        if (tMin > maxDistance) {
            return null;
        }

        // find the closest intersection:
        double t;
        Facing hitBlockFace;
        if (tMin < 0.0D) {
            t = tMax;
            hitBlockFace = hitBlockFaceMax;
        } else {
            t = tMin;
            hitBlockFace = hitBlockFaceMin;
        }
        // reusing the newly created direction vector for the hit position:
        Vec hitPosition = dir.mul(t).add(start);
        return new RayBlockResult(blockPosition, hitPosition, context, hitBlockFace, this);
    }

    public Edge[] getEdges() {
        return edges;
    }

    public Vec[] getPoints() {
        return points;
    }

    public boolean intersect(@NotNull Point start, @NotNull Point end) {
        return intersect(
                Math.min(start.x(), end.x()),
                Math.min(start.y(), end.y()),
                Math.min(start.z(), end.z()),
                Math.max(start.x(), end.x()),
                Math.max(start.y(), end.y()),
                Math.max(start.z(), end.z())
        );
    }

    public boolean intersect(double x1, double y1, double z1, double x2, double y2, double z2) {
        // originally from http://www.3dkingdoms.com/weekly/weekly.php?a=3
        double x3 = getMinX();
        double x4 = getMaxX();
        double y3 = getMinY();
        double y4 = getMaxY();
        double z3 = getMinZ();
        double z4 = getMaxZ();
        if (x1 > x3 && x1 < x4 && y1 > y3 && y1 < y4 && z1 > z3 && z1 < z4) {
            return true;
        }
        if (x1 < x3 && x2 < x3 || x1 > x4 && x2 > x4 ||
                y1 < y3 && y2 < y3 || y1 > y4 && y2 > y4 ||
                z1 < z3 && z2 < z3 || z1 > z4 && z2 > z4) {
            return false;
        }
        return isInsideBoxWithAxis(Axis.X, getSegmentIntersection(x1 - x3, x2 - x3, x1, y1, z1, x2, y2, z2)) ||
                isInsideBoxWithAxis(Axis.X, getSegmentIntersection(x1 - x4, x2 - x4, x1, y1, z1, x2, y2, z2)) ||
                isInsideBoxWithAxis(Axis.Y, getSegmentIntersection(y1 - y3, y2 - y3, x1, y1, z1, x2, y2, z2)) ||
                isInsideBoxWithAxis(Axis.Y, getSegmentIntersection(y1 - y4, y2 - y4, x1, y1, z1, x2, y2, z2)) ||
                isInsideBoxWithAxis(Axis.Z, getSegmentIntersection(z1 - z3, z2 - z3, x1, y1, z1, x2, y2, z2)) ||
                isInsideBoxWithAxis(Axis.Z, getSegmentIntersection(z1 - z4, z2 - z4, x1, y1, z1, x2, y2, z2));
    }

    private @Nullable Vec getSegmentIntersection(double dst1, double dst2, double x1, double y1, double z1, double x2, double y2, double z2) {
        if (dst1 == dst2 || dst1 * dst2 >= 0D) return null;
        final double delta = dst1 / (dst1 - dst2);
        return new Vec(
                x1 + (x2 - x1) * delta,
                y1 + (y2 - y1) * delta,
                z1 + (z2 - z1) * delta
        );
    }

    private boolean isInsideBoxWithAxis(Axis axis, @Nullable Vec intersection) {
        if (intersection == null) return false;
        double x1 = getMinX();
        double x2 = getMaxX();
        double y1 = getMinY();
        double y2 = getMaxY();
        double z1 = getMinZ();
        double z2 = getMaxZ();
        return axis == Axis.X && intersection.z() > z1 && intersection.z() < z2 && intersection.y() > y1 && intersection.y() < y2 ||
                axis == Axis.Y && intersection.z() > z1 && intersection.z() < z2 && intersection.x() > x1 && intersection.x() < x2 ||
                axis == Axis.Z && intersection.x() > x1 && intersection.x() < x2 && intersection.y() > y1 && intersection.y() < y2;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "edges=" + Arrays.toString(edges) +
                ", points=" + Arrays.toString(points) +
                ", offsetType=" + offsetType +
                ", minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shape shape = (Shape) o;

        if (Double.compare(shape.minX, minX) != 0) return false;
        if (Double.compare(shape.minY, minY) != 0) return false;
        if (Double.compare(shape.minZ, minZ) != 0) return false;
        if (Double.compare(shape.maxX, maxX) != 0) return false;
        if (Double.compare(shape.maxY, maxY) != 0) return false;
        if (Double.compare(shape.maxZ, maxZ) != 0) return false;
        return offsetType == shape.offsetType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(offsetType, minX, minY, minZ, maxX, maxY, maxZ);
        result = 31 * result + Arrays.hashCode(edges);
        result = 31 * result + Arrays.hashCode(points);
        return result;
    }
}
