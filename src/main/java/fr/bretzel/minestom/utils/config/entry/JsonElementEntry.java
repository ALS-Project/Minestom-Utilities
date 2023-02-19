package fr.bretzel.minestom.utils.config.entry;

import com.google.gson.JsonElement;

public class JsonElementEntry extends Entry<JsonElement> {
    public JsonElementEntry(String key, JsonElement element) {
        super(key, JsonElement.class, element);
    }

    public JsonElementEntry(String key) {
        super(key, JsonElement.class);
    }

    @Override
    public Entry<JsonElement> copy() {
        return new JsonElementEntry(key(), defaultValue());
    }
}
