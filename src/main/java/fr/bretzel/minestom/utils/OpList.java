package fr.bretzel.minestom.utils;

import fr.bretzel.minestom.utils.config.Config;
import fr.bretzel.minestom.utils.config.entry.NumberEntry;
import net.minestom.server.entity.Player;

public class OpList extends Config {

    public OpList() {
        super("ops");
    }

    public void setOp(Player player, int level) {
        String key = player.getUuid().toString();

        if (has(key)) {
            remove(key);
        }

        if (level > 0) {
            add(new NumberEntry(key, level));
            player.setPermissionLevel(level);
        }

        write();
    }

    public boolean hasOp(Player player) {
        return has(player.getUuid().toString());
    }

    public int getLevel(Player player) {
        return get(new NumberEntry(player.getUuid().toString())).intValue();
    }
}
