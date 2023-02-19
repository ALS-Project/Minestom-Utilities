package fr.bretzel.minestom.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.bretzel.minestom.utils.config.entry.Entry;
import fr.bretzel.minestom.utils.io.FileUtils;
import fr.bretzel.minestom.utils.io.FileWatcher;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Config {
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final File file;
    private JsonObject json_config = new JsonObject();
    private Consumer<Config> onFileChange = config -> {
    };
    private boolean isFirstLoad = false;
    private boolean writeOnUpdate = false;
    private FileWatcher watcher;

    private boolean disableNextFileWatcherRefresh = false;

    public Config(String name, Entry<?>... entries) {
        this(new File(name.endsWith(".json") ? name : name + ".json"), entries);
    }

    public Config(String name, Consumer<Config> configConsumer, Entry<?>... entries) {
        this(new File(name.endsWith(".json") ? name : name + ".json"), configConsumer, entries);
    }

    public Config(File file, Entry<?>... entries) {
        this(file, (config -> {
        }), entries);
    }

    public Config(File file, Consumer<Config> configConsumer, Entry<?>... entries) {
        this.file = file;

        if (file.getParentFile() != null && file.getParentFile().isDirectory() && !file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (!file.exists()) {
            FileUtils.createNewFile(file);
            this.isFirstLoad = true;
            //Write an empty json
            write();
        }

        //Read the current file
        read();


        //If config has default entries add and rewrite the file
        if (entries.length > 0) {
            var needToReWrite = false;

            for (var entry : entries) {
                if (!has(entry)) {
                    add(entry);
                    needToReWrite = true;
                }
            }

            if (needToReWrite && !writeOnUpdate) {
                write();
            }
        }

        //A consumer for a static declaration of a config
        configConsumer.accept(this);
    }

    public void add(Entry<?> entry) {
        if (has(entry.key())) throw new IllegalArgumentException("Key " + entry.key() + "is already present !");
        else {
            json_config.add(entry.key(), entry.toJson());
            if (writeOnUpdate)
                write();
        }
    }

    public <V> V getOrAdd(Entry<V> entry) {
        if (!has(entry) && entry.hasDefaultValue()) {
            json_config.add(entry.key(), entry.parse(entry.defaultValue()));

            if (writeOnUpdate)
                write();

        } else return entry.fromJson(json_config.get(entry.key()));
        return entry.defaultValue();
    }

    public <T> T getOrDefault(Entry<T> entry, T defaultValue) {
        if (has(entry))
            return get(entry);
        else return entry.hasDefaultValue() ? entry.defaultValue() : defaultValue;
    }

    public <T> T get(Entry<T> entry) {
        if (has(entry))
            return entry.fromJson(json_config.get(entry.key()));
        return entry.hasDefaultValue() ? entry.defaultValue() : null;
    }

    public <T> void set(Entry<T> entry, T value) {
        json_config.remove(entry.key());
        json_config.add(entry.key(), entry.parse(value));

        if (writeOnUpdate)
            write();
    }

    public boolean has(Entry<?> entry) {
        return has(entry.key());
    }

    public boolean has(String key) {
        return json_config.has(key);
    }

    public void remove(String key) {
        if (has(key)) {
            json_config.remove(key);

            if (writeOnUpdate)
                write();
        }
    }

    public void remove(Entry<?> entry) {
        if (has(entry)) {
            remove(entry.key());
        }
    }

    public JsonObject getJsonConfig() {
        return json_config;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public boolean isWriteOnUpdate() {
        return writeOnUpdate;
    }

    public void setWriteOnUpdate(boolean writeOnChange) {
        this.writeOnUpdate = writeOnChange;
    }

    public Set<String> getKeys() {
        return json_config.keySet();
    }

    public void setOnFileChange(Consumer<Config> onFileChange) {
        this.onFileChange = onFileChange;
    }

    public void enableFileWatch(long ms) {
        this.watcher = new FileWatcher(file, ms) {
            @Override
            protected void onFileChange() {

                if (disableNextFileWatcherRefresh) {
                    disableNextFileWatcherRefresh = false;
                    return;
                }

                System.out.println("File " + file + " has been updated");

                read();
                onFileChange.accept(Config.this);
            }
        };
    }

    public boolean hasFileWatcher() {
        return watcher != null;
    }

    public void read() {
        try {
            json_config = (JsonObject) JsonParser.parseReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        JsonObject newObject = new JsonObject();

        List<String> keys = new ArrayList<>(List.copyOf(json_config.keySet()));

        Collections.sort(keys);

        keys.forEach(s -> newObject.add(s, json_config.get(s)));

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(newObject));
            writer.close();
            disableNextFileWatcherRefresh = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        read();
        write();

        onFileChange.accept(this);
    }
}
