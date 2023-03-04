package fr.bretzel.minestom.utils.raytrace;

public enum RenderType {
    INVISIBLE,
    BLOCK_ENTITY,
    MODEL;

    public static RenderType fromString(String string) {
        return switch (string) {
            case "INVISIBLE" -> INVISIBLE;
            case "ENTITYBLOCK_ANIMATED", "BLOCK_ENTITY" -> BLOCK_ENTITY;
            case "MODEL" -> MODEL;
            default -> null;
        };
    }
}
