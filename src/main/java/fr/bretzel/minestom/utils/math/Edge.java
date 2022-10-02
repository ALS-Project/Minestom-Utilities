package fr.bretzel.minestom.utils.math;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.util.ArrayList;
import java.util.List;

public class Edge {
    private final double minX, minY, minZ;
    private final double maxX, maxY, maxZ;

    public Edge(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);

        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
    }

    public Edge(Vec v, Vec v1) {
        this(v.x(), v.y(), v.z(), v1.x(), v1.y(), v1.z());
    }

    public Edge(Pos p, Pos p1) {
        this(p.x(), p.y(), p.z(), p1.x(), p1.y(), p1.z());
    }

    public Vec getMaxPoint() {
        return new Vec(maxX, maxY, maxZ);
    }

    public Vec getMinPoint() {
        return new Vec(minX, minY, minZ);
    }

    public boolean isIn(Pos other) {
        return ((other.y() - getMinPoint().y()) / (other.x() - getMinPoint().x()) == (other.y() - getMaxPoint().y()) / (other.x() - getMaxPoint().y()));
    }

    public List<Vec> getLines(double accuracy) {
        Vec min = getMinPoint();
        Vec max = getMaxPoint();

        double distance = min.distance(max);

        int numbersOfLocs = (int) (distance / accuracy);
        Vec vec = max.sub(min).normalize().mul(accuracy).mul(-1);
        Vec start = getMaxPoint();

        ArrayList<Vec> list = new ArrayList<>();

        for (int i = 0; i < numbersOfLocs; i++)
            list.add(start.add(vec));

        return list;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                '}';
    }
}
