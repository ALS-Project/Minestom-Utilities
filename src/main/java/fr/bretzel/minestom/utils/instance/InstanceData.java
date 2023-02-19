package fr.bretzel.minestom.utils.instance;

import fr.bretzel.minestom.utils.instance.generator.FlatGenerator;
import fr.bretzel.minestom.utils.instance.generator.VoidGenerator;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Objects;

public record InstanceData(@Nullable Path directory, boolean autoChunkLoad, boolean isVanilla,
                           @Nullable String chunkGenerator) {

    public InstanceData(Path directory, boolean autoChunkLoad, boolean isVanilla, Generator chunkGenerator) {
        this(directory, autoChunkLoad, isVanilla, chunkGenerator instanceof VoidGenerator ? "void" : "flat");
    }

    public Generator toChunkGenerator() {
        return Objects.equals(chunkGenerator, "void") ? new VoidGenerator() : new FlatGenerator();
    }

    @Override
    public String toString() {
        return "InstanceData{" +
                "directory=" + directory +
                ", autoChunkLoad=" + autoChunkLoad +
                ", chunkGenerator='" + chunkGenerator + '\'' +
                '}';
    }
}
