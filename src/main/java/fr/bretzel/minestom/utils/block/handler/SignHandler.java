package fr.bretzel.minestom.utils.block.handler;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class SignHandler implements BlockHandler {

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return List.of(Tag.Byte("GlowingText"), Tag.String("Color"), Tag.String("Text1"), Tag.String("Text2"), Tag.String("Text3"), Tag.String("Text4"));
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from(Key.key("minecraft:sign"));
    }
}
