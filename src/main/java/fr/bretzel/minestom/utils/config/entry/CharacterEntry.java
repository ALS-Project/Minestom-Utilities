package fr.bretzel.minestom.utils.config.entry;

public class CharacterEntry extends Entry<Character> {
    public CharacterEntry(String key, Character defaultValue) {
        super(key, Character.TYPE, defaultValue);
    }

    public CharacterEntry(String key) {
        super(key, Character.TYPE);
    }

    @Override
    public Entry<Character> copy() {
        return new CharacterEntry(key(), defaultValue());
    }
}
