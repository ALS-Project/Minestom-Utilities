package fr.bretzel.minestom.utils.config.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bretzel.minestom.utils.instance.InstanceData;

import java.nio.file.Path;

public class InstanceDataEntry extends Entry<InstanceData> {

    public InstanceDataEntry(String key) {
        super(key, InstanceData.class);
    }

    public InstanceDataEntry(String key, InstanceData value) {
        super(key, InstanceData.class, value);
    }

    @Override
    public InstanceData fromJson(JsonElement element) {
        if (element instanceof JsonObject object) {
            return new InstanceData(Path.of(object.get("path").getAsString()), object.get("autoChunkLoad").getAsBoolean(), object.get("isVanilla").getAsBoolean(), object.get("chunkGenerator").getAsString());
        }
        return null;
    }

    @Override
    public JsonElement parse(InstanceData value) {
        JsonObject object = new JsonObject();
        object.addProperty("autoChunkLoad", value.autoChunkLoad());
        object.addProperty("path", value.directory().toString());
        object.addProperty("isVanilla", value.isVanilla());
        object.addProperty("chunkGenerator", value.chunkGenerator());
        return object;
    }

    @Override
    public InstanceDataEntry copy() {
        return new InstanceDataEntry(key(), defaultValue());
    }
}
