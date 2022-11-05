package fr.bretzel.minestom.utils;

public class SelfExpiringValue<V> {

    private final V defaultValue;
    private V value;
    private long timeToRemain = 0;
    private boolean expired = false;

    public SelfExpiringValue(V defaultValue, V value) {
        this.defaultValue = defaultValue;
        this.value = value;
    }

    public SelfExpiringValue(V defaultValue, V value, long expiring) {
        this.defaultValue = defaultValue;
        this.value = value;
        this.timeToRemain = System.currentTimeMillis() + expiring;
    }

    public V getDefaultValue() {
        return defaultValue;
    }

    public V getValue() {
        cleanup();
        return value;
    }

    /**
     * @param value    the value wanted to store
     * @param expiring time to store the value in MS
     */
    public void set(V value, long expiring) {
        this.value = value;
        this.timeToRemain = System.currentTimeMillis() + expiring;
        expired = false;
    }

    public void updateExpiring(long expiring) {
        cleanup();

        if (!expired) {
            timeToRemain = timeToRemain + expiring;
        }
    }

    /**
     * @return true if the value has been expired and restored to default
     */
    public boolean isExpired() {
        cleanup();
        return expired;
    }

    private void cleanup() {
        if (System.currentTimeMillis() >= timeToRemain) {
            this.value = defaultValue;
            expired = true;
        }
    }
}
