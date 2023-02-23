package fr.bretzel.minestom.utils.instance.biomes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.bretzel.minestom.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class BiomesGenerator {
    public static void main(String[] args) throws IOException {
        File outputFile = new File("src/main/java/fr/als/core/instance/biomes/Biomes.java");

        URL url = new URL("https://raw.githubusercontent.com/Articdive/ArticData/1.19.2/1_19_2_biomes.json");
        URLConnection request = url.openConnection();
        request.connect();

        FileUtils.createNewFile(outputFile);
        JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject object = element.getAsJsonObject();

        StringBuilder builder = new StringBuilder("package fr.als.core.instance.biomes;").append("\n").append("\n");
        builder.append("import net.minestom.server.world.biomes.Biome;").append("\n")
                .append("import net.minestom.server.world.biomes.BiomeEffects;").append("\n")
                .append("import net.minestom.server.MinecraftServer;").append("\n")
                .append("import net.minestom.server.utils.NamespaceID;").append("\n")
                .append("\n")
                .append("import java.util.Arrays;").append("\n").append("\n");
        builder.append("public class Biomes {").append("\n").append("\n");
        object.entrySet().forEach(entry -> {
            String nameSpaceBiomes = entry.getKey().toLowerCase(Locale.ROOT);
            JsonObject biomeObject = entry.getValue().getAsJsonObject();

            StringBuilder biomeBuilder = new StringBuilder("    public static Biome " + nameSpaceBiomes.split(":")[1].toUpperCase() + " = ");

            biomeBuilder.append("Biome.builder()");

            biomeBuilder.append(".name(").append("NamespaceID.from(\"").append(nameSpaceBiomes).append("\")").append(")");
            biomeBuilder.append(".temperature(").append(biomeObject.get("temperature").getAsFloat()).append("F)");
            biomeBuilder.append(".downfall(").append(biomeObject.get("downfall").getAsFloat()).append("F)");
            //biomeBuilder.append(".category(").append("Biome.Category.").append(biomeObject.get("category").getAsString().toUpperCase()).append(")");
            biomeBuilder.append(".precipitation(").append("Biome.Precipitation.").append(biomeObject.get("precipitation").getAsString().toUpperCase()).append(")");

            StringBuilder effectBuilder = new StringBuilder("BiomeEffects.builder()");

            effectBuilder.append(".fogColor(").append(biomeObject.get("fogColor").getAsInt()).append(")");
            effectBuilder.append(".waterColor(").append(biomeObject.get("waterColor").getAsInt()).append(")");
            effectBuilder.append(".waterFogColor(").append(biomeObject.get("waterFogColor").getAsInt()).append(")");
            effectBuilder.append(".skyColor(").append(biomeObject.get("skyColor").getAsInt()).append(")");
            effectBuilder.append(".grassColorModifier(").append("BiomeEffects.GrassColorModifier.").append(biomeObject.get("grassColorModifier").getAsString().toUpperCase()).append(")");

            if (biomeObject.has("foliageColorOverride"))
                effectBuilder.append(".foliageColor(").append(biomeObject.get("foliageColorOverride").getAsInt()).append(")");
            else if (biomeObject.has("grassColorOverride"))
                effectBuilder.append(".grassColor(").append(biomeObject.get("grassColorOverride").getAsInt()).append(")");

            effectBuilder.append(".build()");

            biomeBuilder.append(".effects(").append(effectBuilder).append(")");
            biomeBuilder.append(".build();");
            builder.append(biomeBuilder).append("\n");
        });

        builder.append("\n");

        builder.append("""
                public static void init() {
                    Arrays.stream(Biomes.class.getFields()).forEach(field -> {
                        try {
                            Biome biome = (Biome) field.get(null);
                            if (MinecraftServer.getBiomeManager().getByName(biome.name()) == null)
                                MinecraftServer.getBiomeManager().addBiome(biome);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                }""".indent(4));

        builder.append("\n").append("}");

        System.out.println(outputFile);
        FileUtils.writeFile(outputFile, builder.toString());
    }
}
