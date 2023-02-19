package fr.bretzel.minestom.utils.config.entry;

public class BooleanEntry extends Entry<Boolean> {
    public BooleanEntry(String key, Boolean defaultValue) {
        super(key, Boolean.TYPE, defaultValue);
    }

    public BooleanEntry(String key) {
        super(key, Boolean.TYPE);
    }

    @Override
    public Entry<Boolean> copy() {
        return new BooleanEntry(key(), defaultValue());
    }
}
