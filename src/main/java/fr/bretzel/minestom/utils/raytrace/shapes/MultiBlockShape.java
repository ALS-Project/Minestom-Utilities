package fr.bretzel.minestom.utils.raytrace.shapes;

public record MultiBlockShape(BlockShape shape, BlockShape visualShape, BlockShape collisionShape,
                              BlockShape interactionShape, BlockShape occlusionShape) {
}
