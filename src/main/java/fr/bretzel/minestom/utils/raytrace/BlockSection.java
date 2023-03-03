package fr.bretzel.minestom.utils.raytrace;

import fr.bretzel.minestom.utils.math.MathsUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class BlockSection extends IRayTrace {
    private final double width, height, depth;
    private final Point offset;
    private Point relativeEnd;

    private final BlockShape parent;

    public BlockSection(double width, double height, double depth, Vec offset, BlockShape parent) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.offset = offset;
        this.parent = parent;
    }

    public BlockSection(double width, double height, double depth, BlockShape parent) {
        this(width, height, depth, new Vec(-width / 2, 0, -depth / 2), parent);
    }

    public @NotNull Point relativeStart() {
        return offset;
    }

    public @NotNull Point relativeEnd() {
        Point relativeEnd = this.relativeEnd;
        if (relativeEnd == null) this.relativeEnd = relativeEnd = offset.add(width, height, depth);
        return relativeEnd;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    public double depth() {
        return depth;
    }

    public double minX() {
        return relativeStart().x();
    }

    public double maxX() {
        return relativeEnd().x();
    }

    public double minY() {
        return relativeStart().y();
    }

    public double maxY() {
        return relativeEnd().y();
    }

    public double minZ() {
        return relativeStart().z();
    }

    public double maxZ() {
        return relativeEnd().z();
    }

    public BlockShape parent() {
        return parent;
    }

    @Override
    public RayBlockResult rayTraceBlock(RayTraceContext context, Point blockPosition, Point offset) {
        var rayStart = context.start();
        var moving = context.precision();
        var rayDirection = MathsUtils.normalizeZeros(context.direction());

        var bbCentre = new Vec(moving.minX() + moving.width() / 2, moving.minY() + moving.height() / 2, moving.minZ() + moving.depth() / 2);
        var rayCentre = rayStart.add(bbCentre).add(offset);

        // Translate bounding box
        var bbOffMin = new Vec(minX() - rayCentre.x() + blockPosition.x() - moving.width() / 2, minY() - rayCentre.y() + blockPosition.y() - moving.height() / 2, minZ() - rayCentre.z() + blockPosition.z() - moving.depth() / 2);
        var bbOffMax = new Vec(maxX() - rayCentre.x() + blockPosition.x() + moving.width() / 2, maxY() - rayCentre.y() + blockPosition.y() + moving.height() / 2, maxZ() - rayCentre.z() + blockPosition.z() + moving.depth() / 2);

        // This check is done in 2d. it can be visualised as a rectangle (the face we are checking), and a point.
        // If the point is within the rectangle, we know the vector intersects the face.

        double signumRayX = Math.signum(rayDirection.x());
        double signumRayY = Math.signum(rayDirection.y());
        double signumRayZ = Math.signum(rayDirection.z());

        var startX = rayStart.x();
        var startY = rayStart.y();
        var startZ = rayStart.z();

        final var minX = Math.min(offset.add(minX()).x(), offset.add(maxX()).x());
        final var maxX = Math.max(offset.add(minX()).x(), offset.add(maxX()).x());

        final var minY = Math.min(offset.add(minY()).y(), offset.add(maxY()).y());
        final var maxY = Math.max(offset.add(minY()).y(), offset.add(maxY()).y());

        final var minZ = Math.min(offset.add(minZ()).z(), offset.add(maxZ()).z());
        final var maxZ = Math.max(offset.add(minZ()).z(), offset.add(maxZ()).z());

        // saving a few divisions below:
        // Note: If one of the direction vector components is 0.0, these
        // divisions result in infinity. But this is not a problem.
        var divX = 1.0D / rayDirection.x();
        var divY = 1.0D / rayDirection.y();
        var divZ = 1.0D / rayDirection.z();

        double tMin = 0;
        double tMax = 0;
        BlockFace hitBlockFaceMin = null;
        BlockFace hitBlockFaceMax = null;

        double percentage = Double.MAX_VALUE;

        // Intersect X planes
        // Left side of bounding box
        if (rayDirection.x() > 0) {
            double xFac = epsilon(bbOffMin.x() / rayDirection.x());
            if (xFac < percentage) {
                double yix = rayDirection.y() * xFac + rayCentre.y();
                double zix = rayDirection.z() * xFac + rayCentre.z();

                // Check if ray passes through y/z plane
                if (((yix - rayCentre.y()) * signumRayY) >= 0
                        && ((zix - rayCentre.z()) * signumRayZ) >= 0
                        && yix >= minY() + blockPosition.y() - moving.height() / 2
                        && yix <= maxY() + blockPosition.y() + moving.height() / 2
                        && zix >= minZ() + blockPosition.z() - moving.depth() / 2
                        && zix <= maxZ() + blockPosition.z() + moving.depth() / 2) {
                    tMin = (minX - startX) * divX;
                    tMax = (maxX - startX) * divX;
                    hitBlockFaceMin = BlockFace.WEST;
                    hitBlockFaceMax = BlockFace.EAST;
                    percentage = xFac;
                }
            }
        } else {
            // Right side of bounding box
            double xFac = epsilon(bbOffMax.x() / rayDirection.x());
            if (xFac < percentage) {
                double yix = rayDirection.y() * xFac + rayCentre.y();
                double zix = rayDirection.z() * xFac + rayCentre.z();

                if (((yix - rayCentre.y()) * signumRayY) >= 0
                        && ((zix - rayCentre.z()) * signumRayZ) >= 0
                        && yix >= minY() + blockPosition.y() - moving.height() / 2
                        && yix <= maxY() + blockPosition.y() + moving.height() / 2
                        && zix >= minZ() + blockPosition.z() - moving.depth() / 2
                        && zix <= maxZ() + blockPosition.z() + moving.depth() / 2) {
                    tMin = (maxX - startX) * divX;
                    tMax = (minX - startX) * divX;
                    hitBlockFaceMin = BlockFace.EAST;
                    hitBlockFaceMax = BlockFace.WEST;
                    percentage = xFac;
                }
            }
        }

        double tyMin = 0;
        double tyMax = 0;
        BlockFace hitBlockFaceYMin = null;
        BlockFace hitBlockFaceYMax = null;

        // Intersect Y
        if (rayDirection.y() > 0) {
            double yFac = epsilon(bbOffMin.y() / rayDirection.y());
            if (yFac < percentage) {
                double xiy = rayDirection.x() * yFac + rayCentre.x();
                double ziy = rayDirection.z() * yFac + rayCentre.z();

                if (((ziy - rayCentre.z()) * signumRayZ) >= 0
                        && ((xiy - rayCentre.x()) * signumRayX) >= 0
                        && xiy >= minX() + blockPosition.x() - moving.width() / 2
                        && xiy <= maxX() + blockPosition.x() + moving.width() / 2
                        && ziy >= minZ() + blockPosition.z() - moving.depth() / 2
                        && ziy <= maxZ() + blockPosition.z() + moving.depth() / 2) {
                    tyMin = (minY - startY) * divY;
                    tyMax = (maxY - startY) * divY;
                    hitBlockFaceYMin = BlockFace.TOP;
                    hitBlockFaceYMax = BlockFace.BOTTOM;
                    percentage = yFac;
                }
            }
        } else {
            double yFac = epsilon(bbOffMax.y() / rayDirection.y());
            if (yFac < percentage) {
                double xiy = rayDirection.x() * yFac + rayCentre.x();
                double ziy = rayDirection.z() * yFac + rayCentre.z();

                if (((ziy - rayCentre.z()) * signumRayZ) >= 0
                        && ((xiy - rayCentre.x()) * signumRayX) >= 0
                        && xiy >= minX() + blockPosition.x() - moving.width() / 2
                        && xiy <= maxX() + blockPosition.x() + moving.width() / 2
                        && ziy >= minZ() + blockPosition.z() - moving.depth() / 2
                        && ziy <= maxZ() + blockPosition.z() + moving.depth() / 2) {
                    tyMin = (maxY - startY) * divY;
                    tyMax = (minY - startY) * divY;
                    hitBlockFaceYMin = BlockFace.TOP;
                    hitBlockFaceYMax = BlockFace.BOTTOM;
                    percentage = yFac;
                }
            }
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
        double tzMin = 0;
        double tzMax = 0;
        BlockFace hitBlockFaceZMin = null;
        BlockFace hitBlockFaceZMax = null;
        if (rayDirection.z() > 0) {
            double zFac = epsilon(bbOffMin.z() / rayDirection.z());
            if (zFac < percentage) {
                double xiz = rayDirection.x() * zFac + rayCentre.x();
                double yiz = rayDirection.y() * zFac + rayCentre.y();

                if (((yiz - rayCentre.y()) * signumRayY) >= 0
                        && ((xiz - rayCentre.x()) * signumRayX) >= 0
                        && xiz >= minX() + blockPosition.x() - moving.width() / 2
                        && xiz <= maxX() + blockPosition.x() + moving.width() / 2
                        && yiz >= minY() + blockPosition.y() - moving.height() / 2
                        && yiz <= maxY() + blockPosition.y() + moving.height() / 2) {
                    tzMin = (minZ - startZ) * divZ;
                    tzMax = (maxZ - startZ) * divZ;
                    hitBlockFaceZMin = BlockFace.NORTH;
                    hitBlockFaceZMax = BlockFace.SOUTH;
                }
            }
        } else {
            double zFac = epsilon(bbOffMax.z() / rayDirection.z());
            if (zFac < percentage) {
                double xiz = rayDirection.x() * zFac + rayCentre.x();
                double yiz = rayDirection.y() * zFac + rayCentre.y();

                if (((yiz - rayCentre.y()) * signumRayY) >= 0
                        && ((xiz - rayCentre.x()) * signumRayX) >= 0
                        && xiz >= minX() + blockPosition.x() - moving.width() / 2
                        && xiz <= maxX() + blockPosition.x() + moving.width() / 2
                        && yiz >= minY() + blockPosition.y() - moving.height() / 2
                        && yiz <= maxY() + blockPosition.y() + moving.height() / 2) {
                    tzMin = (maxZ - startZ) * divZ;
                    tzMax = (minZ - startZ) * divZ;
                    hitBlockFaceZMin = BlockFace.SOUTH;
                    hitBlockFaceZMax = BlockFace.NORTH;
                }
            }
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
        if (tMin > context.distance()) {
            return null;
        }

        double t;

        BlockFace hitBlockFace;

        if (tMin < 0.0D) {
            t = tMax;
            hitBlockFace = hitBlockFaceMax;
        } else {
            t = tMin;
            hitBlockFace = hitBlockFaceMin;
        }

        // reusing the newly created direction vector for the hit position:
        Vec hitPosition = rayDirection.mul(t).add(rayStart);
        return new RayBlockResult(hitPosition, context, hitBlockFace, parent());
    }

    @Override
    public String toString() {
        return "BlockSection{" +
                "width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                ", offset=" + offset +
                ", relativeEnd=" + relativeEnd +
                '}';
    }
}
