package fr.bretzel.minestom.utils.raytrace;

import fr.bretzel.minestom.utils.particle.ParticleUtils;
import fr.bretzel.minestom.utils.particle.data.ParticleColor;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.particle.Particle;

public class RayUtils {

    public static void drawBlockShape(Pos blockPosition, BlockShape shape, Player... players) {
        for (BlockSection section : shape.blockSections()) {
            drawBlockSection(blockPosition, section, players);
        }
    }

    public static void drawBlockSection(Pos blockPosition, BlockSection section, Player... players) {
        var shape = section.parent();

        if (shape.offsetType() != OffsetType.NONE)
            blockPosition = blockPosition.add(shape.offsetType().operator().apply(blockPosition));

        var minx = blockPosition.x() + section.minX();
        var miny = blockPosition.y() + section.minY();
        var minz = blockPosition.z() + section.minZ();

        var maxx = blockPosition.x() + section.maxX();
        var maxy = blockPosition.y() + section.maxY();
        var maxz = blockPosition.z() + section.maxZ();

        for (Player player : players) {
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(minx, miny, minz), new ParticleColor(new Color(100, 100, 255)));
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(minx, miny, maxz), new ParticleColor(new Color(100, 100, 255)));
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(maxx, miny, maxz), new ParticleColor(new Color(100, 100, 255)));
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(maxx, miny, minz), new ParticleColor(new Color(100, 100, 255)));

            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(minx, maxy, minz), new ParticleColor(new Color(100, 100, 255)));
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(minx, maxy, maxz), new ParticleColor(new Color(100, 100, 255)));
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(maxx, maxy, maxz), new ParticleColor(new Color(100, 100, 255)));
            ParticleUtils.spawnParticle(player, Particle.DUST, new Pos(maxx, maxy, minz), new ParticleColor(new Color(100, 100, 255)));

            drawLine(new Pos(minx, miny, minz), new Pos(minx, miny, maxz), player);
            drawLine(new Pos(minx, miny, maxz), new Pos(maxx, miny, maxz), player);
            drawLine(new Pos(maxx, miny, maxz), new Pos(maxx, miny, minz), player);
            drawLine(new Pos(maxx, miny, minz), new Pos(minx, miny, minz), player);

            drawLine(new Pos(minx, maxy, minz), new Pos(minx, maxy, maxz), player);
            drawLine(new Pos(minx, maxy, maxz), new Pos(maxx, maxy, maxz), player);
            drawLine(new Pos(maxx, maxy, maxz), new Pos(maxx, maxy, minz), player);
            drawLine(new Pos(maxx, maxy, minz), new Pos(minx, maxy, minz), player);

            drawLine(new Pos(minx, miny, minz), new Pos(minx, maxy, minz), player);
            drawLine(new Pos(minx, miny, maxz), new Pos(minx, maxy, maxz), player);
            drawLine(new Pos(maxx, miny, maxz), new Pos(maxx, maxy, maxz), player);
            drawLine(new Pos(maxx, miny, minz), new Pos(maxx, maxy, minz), player);

        }
    }

    public static void drawLine(Point from, Point to, Player... players) {
        var dir = new Vec(to.x() - from.x(), to.y() - from.y(), to.z() - from.z()).normalize().div(10);
        double d = from.distance(to) / 16;


        for (int i = 0; i < 16; i++) {
            var pos = from.add(dir.mul(i * d));
            for (Player player : players) {
                ParticleUtils.spawnParticle(player, Particle.DUST, pos, new ParticleColor(new Color(255, 100, 100)));
            }
        }
    }
}
