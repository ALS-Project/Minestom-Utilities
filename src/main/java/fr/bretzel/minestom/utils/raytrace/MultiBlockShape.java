package fr.bretzel.minestom.utils.raytrace;

import com.google.gson.JsonObject;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;


public record MultiBlockShape(BlockShape shape, BlockShape visualShape, BlockShape collisionShape,
                              BlockShape interactionShape, @NotNull short stateId, Block block,
                              NamespaceID namespaceID) {

    public static MultiBlockShape of(JsonObject state, NamespaceID id) {
        var stateId = state.get("stateId").getAsShort();
        var block = Block.fromStateId(stateId);

        if (block != null && !block.namespace().equals(id)) {
            throw new IllegalStateException("Block " + block + " has not the same namespace " + id);
        }

        var offsetType = OffsetType.valueOf(state.get("offsetType").getAsString());
        var renderType = RenderType.fromString(state.get("renderShape").getAsString());
        var shape = BlockShape.of(state.get("shape").getAsString(), block, offsetType, renderType);
        var collisionShape = BlockShape.of(state.get("collisionShape").getAsString(), block, offsetType, renderType);
        var interactionShape = BlockShape.of(state.get("interactionShape").getAsString(), block, offsetType, renderType);
        var visualShape = BlockShape.of(state.get("visualShape").getAsString(), block, offsetType, renderType);
        return new MultiBlockShape(shape, visualShape, collisionShape, interactionShape, stateId, block, id);
    }

    public BlockShape getCorrectShape(BlockMode blockMode) {
        return switch (blockMode) {
            case VISUAL -> visualShape;
            case INTERACTION -> interactionShape;
            case COLLISION -> collisionShape;
            default -> shape;
        };
    }

    @Override
    public String toString() {
        return "MultiBlockShape{" +
                "shape=" + shape +
                ", visualShape=" + visualShape +
                ", collisionShape=" + collisionShape +
                ", interactionShape=" + interactionShape +
                ", block=" + block +
                '}';
    }
}
