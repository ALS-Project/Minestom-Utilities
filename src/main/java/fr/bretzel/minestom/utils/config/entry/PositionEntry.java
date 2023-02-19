package fr.bretzel.minestom.utils.config.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.server.coordinate.Pos;

public class PositionEntry extends Entry<Pos> {

    public PositionEntry(String key) {
        super(key, Pos.class, Pos.ZERO);
    }

    public PositionEntry(String key, Pos defaultValue) {
        super(key, Pos.class, defaultValue);
    }

    @Override
    public Pos fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        double x = object.get("x").getAsDouble();
        double y = object.get("y").getAsDouble();
        double z = object.get("z").getAsDouble();

        float yaw = object.get("yaw").getAsFloat();
        float pitch = object.get("pitch").getAsFloat();

        return new Pos(x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement parse(Pos value) {
        JsonObject object = new JsonObject();
        object.addProperty("x", value.x());
        object.addProperty("y", value.y());
        object.addProperty("z", value.z());

        object.addProperty("yaw", value.yaw());
        object.addProperty("pitch", value.pitch());
        return object;
    }

    @Override
    public Entry<Pos> copy() {
        return new PositionEntry(key(), defaultValue());
    }
}
