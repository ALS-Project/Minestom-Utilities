package fr.bretzel.minestom.utils;

import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.shapes.Shapes;
import fr.bretzel.minestom.utils.math.Edge;
import fr.bretzel.minestom.utils.particle.IParticleData;
import fr.bretzel.minestom.utils.particle.ParticleUtils;
import fr.bretzel.minestom.utils.raytrace.OffsetType;
import fr.bretzel.minestom.utils.raytrace.RayBlockResult;
import fr.bretzel.minestom.utils.raytrace.RayTrace;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.utils.binary.BinaryWriter;

import java.util.function.Consumer;

public class RayUtils {

    public static void drawHitBox(Shape shape, Point translated, Instance instance, Particle particle, IParticleData data) {
        instance.getPlayers().forEach(player -> drawHitBox(shape, translated, player, particle, data));
    }

    public static void drawHitBox(Shape boundingBlockBox, Point translated, Player player, Particle particle, IParticleData data) {
        if (boundingBlockBox instanceof Shapes shapes)
            shapes.getShapes().forEach(shape -> drawHitBox(shape, translated, player, particle, data));
        else {
            for (Vec point : boundingBlockBox.getPoints()) {
                ParticleUtils.spawnParticle(player, particle, translated.add(point), data);
            }
        }
    }

    public static void drawRayBlock(RayBlockResult result, Player player, boolean edge) {
        result.lines(0.1, result.getContext().start().distance(result.getHit()),
                vec -> player.getPlayerConnection().sendPacket(getPacket(vec,
                        Pos.ZERO, binaryWriter -> {
                            binaryWriter.writeFloat((float) 0 / 255);//R
                            binaryWriter.writeFloat((float) 255 / 255);//G
                            binaryWriter.writeFloat((float) 0 / 255);//B
                            binaryWriter.writeFloat(0.1F);//Size
                        })));

        player.getPlayerConnection().sendPacket(getPacket(result.getHit(), new Pos(0, 0, 0), binaryWriter -> {
            binaryWriter.writeFloat((float) 255 / 255);//R
            binaryWriter.writeFloat((float) 0 / 255);//G
            binaryWriter.writeFloat((float) 0 / 255);//B
            binaryWriter.writeFloat(0.22F);//Size
        }));

        drawHitBox(result.getHitShape(), player, result.getBlockPosition(), edge);
    }

    public static void drawHitBox(Shape shape, Player player, Pos translated, boolean drawEdge) {
        Vec offset = shape.getOffsetType() == OffsetType.NONE ? new Vec(0, 0, 0) : RayTrace.getOffset(translated, shape.getOffsetType());

        for (Vec pos : shape.getPoints())
            player.getPlayerConnection().sendPacket(getPacket(pos.add(offset), translated, binaryWriter -> {
                binaryWriter.writeFloat((float) 255 / 255);//R
                binaryWriter.writeFloat((float) 0 / 255);//G
                binaryWriter.writeFloat((float) 255 / 255);//B
                binaryWriter.writeFloat(0.1F);//Size
            }));

        if (drawEdge)
            for (Edge edge : shape.getEdges())
                drawEdge(edge, offset, player, translated);
    }

    public static void drawEdge(Edge edge, Vec offset, Player player, Pos translated) {
        edge.getLines(0.15).forEach(position -> player.getPlayerConnection().sendPacket(getPacket(position.add(offset), translated, binaryWriter -> {
            binaryWriter.writeFloat((float) 0 / 255);//R
            binaryWriter.writeFloat((float) 0 / 255);//G
            binaryWriter.writeFloat((float) 255 / 255);//B
            binaryWriter.writeFloat(0.1F);//Size
        })));
    }

    public static Shape getCorrectBox(Pos newPos, Player player) {
        return new Shape(newPos.x() - (0.6 / 2), (newPos.y() - (1.8 / 2)) + 1, newPos.z() - (0.6 / 2),
                newPos.x() + (0.6 / 2), (newPos.y() + ((player.isSneaking() ? 1.3 : 1.8) / 2)) + 1, newPos.z() + (0.6 / 2));
    }

    private static ParticlePacket getPacket(Point position, Point translated, Consumer<BinaryWriter> writerConsumer) {
        if (writerConsumer == null)
            writerConsumer = binaryWriter -> {
            };
        return ParticleCreator.createParticlePacket(Particle.DUST, true, translated.x() + position.x(), translated.y() + position.y(),
                translated.z() + position.z(), 0f, 0f, 0f, 0, 0, writerConsumer);
    }
}
