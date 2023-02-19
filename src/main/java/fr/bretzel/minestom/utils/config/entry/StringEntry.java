package fr.bretzel.minestom.utils.config.entry;

public class StringEntry extends Entry<String> {
    public StringEntry(String key) {
        super(key, String.class);
    }

    public StringEntry(String key, String defaultValue) {
        super(key, String.class, defaultValue);
    }

    @Override
    public Entry<String> copy() {
        return new StringEntry(key(), defaultValue());
    }
}
