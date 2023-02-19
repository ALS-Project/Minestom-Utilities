package fr.bretzel.minestom.utils.instance;

import fr.bretzel.minestom.utils.config.Config;
import fr.bretzel.minestom.utils.config.entry.InstanceDataEntry;
import fr.bretzel.minestom.utils.instance.generator.FlatGenerator;
import fr.bretzel.minestom.utils.instance.generator.VoidGenerator;
import fr.bretzel.minestom.utils.light.LightEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.DimensionTypeManager;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InstanceManager {

    private static final Logger logger = LoggerFactory.getLogger(InstanceManager.class);
    private static final LightEngine LIGHT_ENGINE = new LightEngine();
    private static final File worldDirectory = new File("worlds/");
    private static final net.minestom.server.instance.InstanceManager INSTANCE_MANAGER = MinecraftServer.getInstanceManager();
    private static final DimensionTypeManager DIMENSION_TYPE_MANAGER = MinecraftServer.getDimensionTypeManager();
    private static final Map<Instance, InstanceData> loadedInstance = new ConcurrentHashMap<>();
    private static Config LOADED_WORLDS;
    private static Supplier<Instance> defaultInstance;

    public static void init() {
        logger.info("World Initialization");

        //Other World Init
        worldDirectory.mkdirs();

        //Load Config/Saved Worlds
        LOADED_WORLDS = new Config("worlds/loaded", config -> config.setWriteOnUpdate(true));

        ArrayList<String> invalidWorld = new ArrayList<>();

        for (String key : LOADED_WORLDS.getKeys()) {
            InstanceData instanceData = LOADED_WORLDS.get(new InstanceDataEntry(key));
            if (instanceData != null && Objects.requireNonNull(instanceData.directory()).toFile().exists()) {
                importWorld(instanceData.directory(), instanceData.autoChunkLoad(), instanceData.isVanilla(), Objects.equals(instanceData.chunkGenerator(), "void") ? new VoidGenerator() : new FlatGenerator(), instanceContainer -> {
                    if (instanceData.isVanilla())
                        return;

                    int viewDistance = MinecraftServer.getChunkViewDistance() / 2;
                    for (int x = -viewDistance; x < viewDistance; x++) {
                        for (int z = -viewDistance; z < viewDistance; z++) {
                            instanceContainer.loadChunk(x, z).thenAccept(LIGHT_ENGINE::recalculateChunk);
                        }
                    }
                });
            } else {
                invalidWorld.add(key);
            }
        }
        invalidWorld.forEach(s -> LOADED_WORLDS.remove(s));
    }

    public static Instance createWorld(String worldName, InstanceData instanceData) {
        File worldFile = new File(worldDirectory, worldName);
        worldFile.mkdirs();
        Instance instance = INSTANCE_MANAGER.createInstanceContainer(getOrCreateDimension(worldName), new AnvilLoader(worldFile.toPath()));
        instance.enableAutoChunkLoad(instanceData.autoChunkLoad());
        instance.setGenerator(instanceData.toChunkGenerator());
        loadedInstance.put(instance, instanceData);

        int viewDistance = MinecraftServer.getChunkViewDistance() / 2;
        for (int x = -viewDistance; x < viewDistance; x++) {
            for (int z = -viewDistance; z < viewDistance; z++) {
                instance.loadChunk(x, z).thenAccept(LIGHT_ENGINE::recalculateChunk);
            }
        }

        if (!LOADED_WORLDS.has(worldName)) {
            logger.info("Added world: " + worldName);
            LOADED_WORLDS.add(new InstanceDataEntry(worldName, instanceData));
        }

        return instance;
    }

    public static InstanceContainer importWorld(Path worldDirectory, boolean autoChunkLoad, Generator generator, Consumer<InstanceContainer> callBack) {
        return importWorld(worldDirectory, autoChunkLoad, isVanillaWorld(worldDirectory), generator, callBack);
    }

    public static InstanceContainer importWorld(Path worldDirectory, boolean autoChunkLoad, boolean vanillaWorld, Generator generator, Consumer<InstanceContainer> callBack) {
        String worldKey = worldDirectory.getFileName().toString();
        InstanceContainer instance = INSTANCE_MANAGER.createInstanceContainer(getOrCreateDimension(worldKey));
        InstanceData instanceData = new InstanceData(worldDirectory, autoChunkLoad, vanillaWorld, generator);
        instance.setChunkLoader(new AnvilLoader(worldDirectory));
        instance.setGenerator(generator);
        instance.enableAutoChunkLoad(autoChunkLoad);

        loadedInstance.put(instance, instanceData);

        //if is a new world
        if (!LOADED_WORLDS.has(worldKey)) {
            logger.info("Added world: " + worldKey);
            LOADED_WORLDS.add(new InstanceDataEntry(worldKey, instanceData));
        }

        if (callBack != null) callBack.accept(instance);

        return instance;
    }

    private static boolean isVanillaWorld(Path worldDirectory) {
        File worldDir = worldDirectory.toFile();
        if (worldDir.isDirectory()) {
            File levelDat = new File(worldDir, "level.dat");
            try {
                NBTCompound compound = (NBTCompound) new NBTReader(levelDat).read();
                return compound.getSize() > 0;
            } catch (IOException | NBTException e) {
                return false;
            }
        }

        return false;
    }

    public static void unloadInstance(@NotNull Instance instance) {
        for (Player player : instance.getPlayers()) {
            player.kick(Component.text("The world " + instance.getDimensionType().getName().asString() + " has been unloaded, return to the default instance", NamedTextColor.RED));
        }

        logger.info("Saving Instance " + instance.getDimensionType().toString());

        instance.saveInstance().thenRun(() -> instance.saveChunksToStorage().thenRun(() -> {
            INSTANCE_MANAGER.unregisterInstance(instance);
            InstanceData instanceData = loadedInstance.get(instance);
            loadedInstance.remove(instance);
            if (instanceData != null) {
                assert instanceData.directory() != null;
                String worldName = instanceData.directory().getFileName().toFile().getName();
                Audiences.all(audience -> audience instanceof Player player && player.getPermissionLevel() > 0).sendMessage(Component.text("Unloaded world: " + instanceData.directory()));
                LOADED_WORLDS.remove(worldName);
            }
        }));

        logger.info("Saved Instance " + instance.getDimensionType().toString());
    }

    public static void saveAll() {
        worldDirectory.mkdirs();

        if (loadedInstance.size() > 0) {

            final var countDownLatch = new CountDownLatch(loadedInstance.size() - 1);

            loadedInstance.forEach((instance, instanceData) -> {

                if (!Objects.requireNonNull(instanceData.directory()).toFile().exists())
                    return;

                final var start = System.currentTimeMillis();
                logger.info("Saving instance: " + instance.getDimensionType().getName());
                instance.saveChunksToStorage().thenRun(() -> {
                    countDownLatch.countDown();
                    saved(instance, start);
                });
            });

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<Instance, InstanceData> getLoadedInstanceData() {
        return loadedInstance;
    }

    public static Set<Instance> loadedWorlds() {
        return loadedInstance.keySet();
    }

    private static void saved(Instance instance, long start) {
        logger.info("Saved instance: " + instance.getDimensionType().getName() + " in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static DimensionType getOrCreateDimension(String worldName) {
        NamespaceID id = NamespaceID.from(worldName.toLowerCase());
        if (DIMENSION_TYPE_MANAGER.isRegistered(id)) return DIMENSION_TYPE_MANAGER.getDimension(id);
        else {
            DimensionType dimensionType = DimensionType.builder(id)
                    .ultrawarm(false)
                    .natural(true)
                    .piglinSafe(false)
                    .respawnAnchorSafe(false)
                    .bedSafe(true)
                    .raidCapable(true)
                    .skylightEnabled(true)
                    .ceilingEnabled(false)
                    .fixedTime(null)
                    .ambientLight(0.0f)
                    .logicalHeight(256)
                    .infiniburn(NamespaceID.from("minecraft:infiniburn_overworld"))
                    .build();

            DIMENSION_TYPE_MANAGER.addDimension(dimensionType);
            return dimensionType;
        }
    }

    public static boolean hasInstance(Path path) {
        return loadedInstance.values().stream().anyMatch(instanceData -> instanceData.directory() != null && instanceData.directory().equals(path));
    }

    public static boolean hasInstance(File path) {
        return hasInstance(path.toPath());
    }

    public static Instance getInstance(String dimensionType) {
        return getInstance(getOrCreateDimension(dimensionType));
    }

    public static Instance getInstance(DimensionType dimensionType) {
        return loadedInstance.keySet().stream().filter(instance -> instance.getDimensionType() == dimensionType).findFirst().orElse(getDefaultInstance().get());
    }

    public static LightEngine getLightEngine() {
        return LIGHT_ENGINE;
    }

    public static File getWorldDirectory() {
        return worldDirectory;
    }

    public static Supplier<Instance> getDefaultInstance() {
        return defaultInstance;
    }

    public static void setDefaultInstance(Supplier<Instance> defaultInstance) {
        InstanceManager.defaultInstance = defaultInstance;
    }
}
