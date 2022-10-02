package fr.bretzel.minestom.utils.block.shapes;

import fr.bretzel.minestom.utils.math.Edge;
import fr.bretzel.minestom.utils.raytrace.RayBlockResult;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shapes extends Shape {
    private final List<Shape> shapes = new ArrayList<>();

    private Vec min;
    private Vec max;

    private Vec[] points = new Vec[0];
    private Edge[] edges = new Edge[0];

    public Shapes() {
        super();
    }

    public Shapes(Shape... shapes) {
        this(Arrays.asList(shapes));
    }

    public Shapes(List<Shape> list) {
        super();
        this.shapes.addAll(list);
        refreshMinAndMax();
    }

    public Shapes add(Shape shape) {
        if (!shapes.contains(shape)) {
            shapes.add(shape);
            refreshMinAndMax();
            shape.setOffsetType(getOffsetType());
        }
        return this;
    }

    public Shapes add(double x, double y, double z, double x1, double y1, double z1) {
        add(new Shape(x, y, z, x1, y1, z1));
        return this;
    }

    @Override
    public boolean isInside(Vec position) {
        return getShapes().stream().anyMatch(boundingBox -> boundingBox.isInside(position));
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    @Override
    public Edge[] getEdges() {
        return edges;
    }

    @Override
    public Vec[] getPoints() {
        return points;
    }

    @Override
    public Vec getMax() {
        return max;
    }

    @Override
    public Vec getMin() {
        return min;
    }

    public void refreshMinAndMax() {
        double minX = -1, minY = -1, minZ = -1;
        double maxX = -1, maxY = -1, maxZ = -1;

        for (Shape shape : getShapes()) {
            if (minX == -1 || minX > shape.getMin().x())
                minX = shape.getMin().x();

            if (minY == -1 || minY > shape.getMin().y())
                minY = shape.getMin().y();

            if (minZ == -1 || minZ > shape.getMin().z())
                minZ = shape.getMin().z();

            if (maxX == -1 || maxX < shape.getMax().x())
                maxX = shape.getMax().x();

            if (maxY == -1 || maxY < shape.getMax().y())
                maxY = shape.getMax().y();

            if (maxZ == -1 || maxZ < shape.getMax().z())
                maxZ = shape.getMax().z();
        }

        this.max = new Vec(maxX, maxY, maxZ);
        this.min = new Vec(minX, minY, minZ);

        ArrayList<Vec> pointList = new ArrayList<>();

        for (Shape shape : getShapes())
            pointList.addAll(Arrays.asList(shape.getPoints()));

        this.points = pointList.toArray(new Vec[0]);


        ArrayList<Edge> arrayList = new ArrayList<>();

        for (Shape shape : getShapes())
            arrayList.addAll(Arrays.asList(shape.getEdges()));

        this.edges = arrayList.toArray(new Edge[0]);
    }

    @Nullable
    @Override
    public RayBlockResult rayTraceBlocks(RayTraceContext context, Point blockPosition, Point offset) {
        RayBlockResult result = null;
        for (Shape box : getShapes()) {
            RayBlockResult newResult = box.rayTraceBlocks(context, blockPosition, offset);
            if (result == null && newResult != null || newResult != null && newResult.getHit().distance(context.start()) < result.getHit().distance(context.start())) {
                result = newResult;
            }
        }
        return result == null ? null : new RayBlockResult(blockPosition, result.getHit(), context, result.getBlockFace(), this);
    }
}
