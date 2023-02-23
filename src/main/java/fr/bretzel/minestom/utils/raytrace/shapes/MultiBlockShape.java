package fr.bretzel.minestom.utils.raytrace.shapes;

import com.google.gson.JsonObject;
import net.minestom.server.instance.block.Block;

public record MultiBlockShape(BlockShape shape, BlockShape visualShape, BlockShape collisionShape,
                              BlockShape interactionShape, short stateId, Block block) {

    public static MultiBlockShape of(JsonObject object) {
        var stateId = object.get("stateId").getAsShort();
        var block = Block.fromStateId(stateId);
        var shape = BlockShape.of(object.get("shape").getAsString(), block);
        var collisionShape = BlockShape.of(object.get("collisionShape").getAsString(), block);
        var interactionShape = BlockShape.of(object.get("interactionShape").getAsString(), block);
        var visualShape = BlockShape.of(object.get("visualShape").getAsString(), block);
        return new MultiBlockShape(shape, visualShape, collisionShape, interactionShape, stateId, block);
    }

}
