package fr.bretzel.minestom.utils.raytrace;

import com.google.gson.JsonObject;
import net.minestom.server.instance.block.Block;

public record MultiBlockShape(BlockShape shape, BlockShape visualShape, BlockShape collisionShape,
                              BlockShape interactionShape, Block block) {

    public static MultiBlockShape of(JsonObject state) {
        var stateId = state.get("stateId").getAsShort();
        var block = Block.fromStateId(stateId);
        var offsetType = OffsetType.valueOf(state.get("offsetType").getAsString());
        var shape = BlockShape.of(state.get("shape").getAsString(), block, offsetType);
        var collisionShape = BlockShape.of(state.get("collisionShape").getAsString(), block, offsetType);
        var interactionShape = BlockShape.of(state.get("interactionShape").getAsString(), block, offsetType);
        var visualShape = BlockShape.of(state.get("visualShape").getAsString(), block, offsetType);
        return new MultiBlockShape(shape, visualShape, collisionShape, interactionShape, block);
    }

    public BlockShape getCorrectShape(BlockMode blockMode) {
        return switch (blockMode) {
            case VISUAL -> visualShape;
            case INTERACTION -> interactionShape;
            case COLLISION -> collisionShape;
            default -> shape;
        };
    }
}