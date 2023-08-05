package fr.bretzel.minestom.utils.particle.data;

import fr.bretzel.minestom.utils.particle.IParticleData;
import net.minestom.server.color.Color;
import net.minestom.server.utils.binary.BinaryWriter;

public record ParticleColor(Color color, float size) implements IParticleData {

    public ParticleColor(Color color) {
        this(color, 0.2F);
    }

    @Override
    public void accept(BinaryWriter binaryWriter) {
        binaryWriter.writeFloat(color.red());//R
        binaryWriter.writeFloat(color.green());//G
        binaryWriter.writeFloat(color.blue());//B
        binaryWriter.writeFloat(size);//Size
    }
}
