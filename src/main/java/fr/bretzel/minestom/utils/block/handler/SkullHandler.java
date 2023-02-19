package fr.bretzel.minestom.utils.block.handler;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class SkullHandler implements BlockHandler {

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return List.of(Tag.NBT("SkullOwner"));
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from(Key.key("minecraft:skull"));
    }
}
