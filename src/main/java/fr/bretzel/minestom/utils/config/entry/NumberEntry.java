package fr.bretzel.minestom.utils.config.entry;

public class NumberEntry extends Entry<Number> {
    public NumberEntry(String key) {
        super(key, Number.class);
    }

    public NumberEntry(String key, Number defaultValue) {
        super(key, Number.class, defaultValue);
    }

    @Override
    public Entry<Number> copy() {
        return new NumberEntry(key(), defaultValue());
    }
}
