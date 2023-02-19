package fr.bretzel.minestom.utils.block.handler;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;

public class BlockHandlerManager {
    public static void register() {
        var blockManager = MinecraftServer.getBlockManager();
        for (var block : Block.values()) {
            var blockName = block.name().toLowerCase();

            if (blockName.contains("sign")) {
                blockManager.registerHandler("minecraft:sign", SignHandler::new);
            } else if (block == Block.PLAYER_HEAD || block == Block.PLAYER_WALL_HEAD) {
                blockManager.registerHandler("minecraft:skull", SkullHandler::new);
            }
        }
    }
}
