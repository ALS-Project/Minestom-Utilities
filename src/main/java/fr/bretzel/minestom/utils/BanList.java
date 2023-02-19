package fr.bretzel.minestom.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bretzel.minestom.utils.config.Config;
import fr.bretzel.minestom.utils.config.entry.JsonElementEntry;
import net.minestom.server.entity.Player;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.UUID;

public class BanList {
    private final Config banned;

    public BanList() {
        banned = new Config("banned");
        banned.setWriteOnUpdate(true);
    }

    public void ban(Player player, String reason, long duration, TemporalUnit timeUnit) {
        ban(player.getUuid(), reason, player.getUsername(), duration, timeUnit);
    }

    public void ban(UUID uuid, String reason, String name, long duration, TemporalUnit timeUnit) {
        JsonObject objectBannedEntry = new JsonObject();
        objectBannedEntry.addProperty("reason", reason);
        objectBannedEntry.addProperty("name", name);
        objectBannedEntry.addProperty("banned_time", Calendar.getInstance().getTimeInMillis());

        if (duration < 0)
            objectBannedEntry.addProperty("duration", -1);
        else
            objectBannedEntry.addProperty("duration", Calendar.getInstance().getTimeInMillis() + Duration.of(duration, timeUnit).toMillis());

        JsonElementEntry entry = new JsonElementEntry(uuid.toString(), objectBannedEntry);
        banned.add(entry);
    }

    public boolean has(Player player) {
        return has(player.getUuid());
    }

    public boolean has(UUID uuid) {
        return banned.has(uuid.toString());
    }

    public void unban(Player player) {
        unban(player.getUuid());
    }

    public void unban(UUID uuid) {
        if (banned.has(uuid.toString())) {
            banned.remove(uuid.toString());
        }
    }

    public boolean isBanned(Player player) {
        return isBanned(player.getUuid());
    }

    public boolean isBanned(UUID uuid) {
        return has(uuid) && getEntry(uuid).isBanned();
    }

    public BanEntry getEntry(Player player) {
        return getEntry(player.getUuid());
    }

    public BanEntry getEntry(UUID uuid) {
        if (banned.has(uuid.toString()))
            return new BanEntry(uuid, banned.get(new JsonElementEntry(uuid.toString())).getAsJsonObject());
        return null;
    }

    public BanEntry getEntryByUsername(String username) {
        for (String key : banned.getKeys()) {
            JsonElement element = banned.get(new JsonElementEntry(key));
            if (element instanceof JsonObject object) {
                BanEntry entry = new BanEntry(UUID.fromString(key), object);
                if (entry.name.equalsIgnoreCase(username))
                    return entry;
            }
        }
        return null;
    }

    public Config getConfig() {
        return banned;
    }

    public static class BanEntry {
        private final UUID uuid;
        private final String reason;
        private final String name;
        private final long banned_time;
        private final long duration;

        public BanEntry(UUID uuid, JsonObject object) {
            this.uuid = uuid;
            this.reason = object.get("reason").getAsString();
            this.name = object.get("name").getAsString();
            this.banned_time = object.get("banned_time").getAsLong();
            this.duration = object.get("duration").getAsLong();
        }

        public BanEntry(UUID uuid, String reason, String name, long banned_time, long duration) {
            this.uuid = uuid;
            this.reason = reason;
            this.name = name;
            this.banned_time = banned_time;
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }

        public String getReason() {
            return reason;
        }

        public UUID getUuid() {
            return uuid;
        }

        public long getBannedTime() {
            return banned_time;
        }

        public String getName() {
            return name;
        }

        public boolean isBanned() {
            return duration == -1 || Calendar.getInstance().getTimeInMillis() < banned_time;
        }
    }
}
