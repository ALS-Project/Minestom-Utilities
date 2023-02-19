package fr.bretzel.minestom.utils.config.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.als.core.instance.ALSInstanceManager;
import fr.bretzel.minestom.utils.math.Location;

public class LocationEntry extends Entry<Location> {

    public LocationEntry(String key) {
        super(key, Location.class, new Location(null, 0, 0, 0, 0, 0));
    }

    public LocationEntry(String key, Location defaultValue) {
        super(key, Location.class, defaultValue);
    }

    @Override
    public Location fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        var x = object.get("x").getAsDouble();
        var y = object.get("y").getAsDouble();
        var z = object.get("z").getAsDouble();

        var yaw = object.get("yaw").getAsFloat();
        var pitch = object.get("pitch").getAsFloat();

        var instance = ALSInstanceManager.getInstance(object.get("world").getAsString());

        return new Location(instance, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement parse(Location value) {
        JsonObject object = new JsonObject();

        object.addProperty("x", value.x());
        object.addProperty("y", value.y());
        object.addProperty("z", value.z());

        object.addProperty("yaw", value.yaw());
        object.addProperty("pitch", value.pitch());

        object.addProperty("world", value.instance().getDimensionType().toString());

        return object;
    }

    @Override
    public Entry<Location> copy() {
        return new LocationEntry(key(), defaultValue());
    }
}
