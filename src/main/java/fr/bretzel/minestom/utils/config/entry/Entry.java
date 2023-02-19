package fr.bretzel.minestom.utils.config.entry;

import com.google.gson.*;

public abstract class Entry<V> {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final String key;
    private final Class<V> classType;
    private V value;

    protected Entry(String key, Class<V> classType) {
        this.key = key;
        this.classType = classType;
    }

    protected Entry(String key, Class<V> classType, V value) {
        this(key, classType);
        this.value = value;
    }

    public V defaultValue() {
        return value;
    }

    public String key() {
        return key;
    }

    public boolean hasDefaultValue() {
        return defaultValue() != null;
    }

    public Class<V> typeClass() {
        return classType;
    }

    public JsonElement toJson() {
        if (hasDefaultValue())
            return parse(defaultValue());
        return new JsonNull();
    }

    public V fromJson(JsonElement element) {
        return gson.fromJson(element, typeClass());
    }

    public JsonElement parse(V value) {
        return JsonParser.parseString(gson.toJson(value));
    }

    public abstract Entry<V> copy();
}
