package fr.bretzel.minestom.utils.particle;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;

public class ParticleUtils {

    public static void spawnParticle(Instance instance, Particle particle, Point position) {
        spawnParticle(instance, particle, position.x(), position.y(), position.z());
    }

    public static void spawnParticle(Instance instance, Particle particle, Point position, int count) {
        spawnParticle(instance, particle, position.x(), position.y(), position.z(), count);
    }

    public static void spawnParticle(Instance instance, Particle particle, Point position, int count, IParticleData data) {
        spawnParticle(instance, particle, position.x(), position.y(), position.z(), count, data);
    }

    public static void spawnParticle(Instance instance, Particle particle, double x, double y, double z, int count, IParticleData data) {
        spawnParticle(instance, particle, x, y, z, 0, 0, 0, count, data);
    }

    public static void spawnParticle(Instance instance, Particle particle, double x, double y, double z, int count) {
        spawnParticle(instance, particle, x, y, z, 0F, 0F, 0F, count, null);
    }

    public static void spawnParticle(Instance instance, Particle particle, double x, double y, double z) {
        spawnParticle(instance, particle, x, y, z, 0F, 0F, 0F, 1, null);
    }

    public static void spawnParticle(Instance instance, Particle particle, double x, double y, double z, float offsetX, float offsetY, float offsetZ, int count, IParticleData data) {
        final var particlePacket = getPacket(particle, x, y, z, offsetX, offsetY, offsetZ, count, data);
        instance.getPlayers().forEach(player -> player.sendPacket(particlePacket));
    }

    public static void spawnParticle(Player instance, Particle particle, Point position) {
        spawnParticle(instance, particle, position.x(), position.y(), position.z());
    }

    public static void spawnParticle(Player instance, Particle particle, Point position, int count) {
        spawnParticle(instance, particle, position.x(), position.y(), position.z(), count);
    }

    public static void spawnParticle(Player instance, Particle particle, Point position, int count, IParticleData data) {
        spawnParticle(instance, particle, position.x(), position.y(), position.z(), count, data);
    }

    public static void spawnParticle(Player instance, Particle particle, double x, double y, double z, int count, IParticleData data) {
        spawnParticle(instance, particle, x, y, z, 0, 0, 0, count, data);
    }

    public static void spawnParticle(Player instance, Particle particle, double x, double y, double z, int count) {
        spawnParticle(instance, particle, x, y, z, 0F, 0F, 0F, count, null);
    }

    public static void spawnParticle(Player instance, Particle particle, double x, double y, double z) {
        spawnParticle(instance, particle, x, y, z, 0F, 0F, 0F, 1, null);
    }

    public static void spawnParticle(Player player, Particle particle, Point point, IParticleData data) {
        spawnParticle(player, particle, point, 0, data);
    }

    public static void spawnParticle(Player player, Particle particle, double x, double y, double z, float offsetX, float offsetY, float offsetZ, int count, IParticleData data) {
        player.sendPacket(getPacket(particle, x, y, z, offsetX, offsetY, offsetZ, count, data));
    }

    protected static ParticlePacket getPacket(Particle particle, double x, double y, double z, float offsetX, float offsetY, float offsetZ, int count, IParticleData data) {
        return ParticleCreator.createParticlePacket(particle, true, x, y,
                z, offsetX, offsetY, offsetZ, 0, count, data == null ? binaryWriter -> {
                } : data);
    }
}
