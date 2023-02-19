package fr.bretzel.minestom.utils.config.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.parser.SNBTParser;

import java.io.StringReader;

public class ItemStackEntry extends Entry<ItemStack> {

    public ItemStackEntry(String key) {
        super(key, ItemStack.class, ItemStack.AIR);
    }

    public ItemStackEntry(String key, ItemStack defaultValue) {
        super(key, ItemStack.class, defaultValue);
    }

    @Override
    public JsonElement parse(ItemStack value) {
        JsonObject object = new JsonObject();
        object.addProperty("id", value.material().key().asString());
        object.addProperty("amount", value.amount());
        object.addProperty("meta", value.meta().toSNBT());
        return object;
    }

    @Override
    public Entry<ItemStack> copy() {
        return new ItemStackEntry(key(), defaultValue());
    }

    @Override
    @NotNull
    public ItemStack fromJson(JsonElement element) {
        if (element instanceof JsonObject object) {
            Material material = Material.fromNamespaceId(NamespaceID.from(Key.key(object.get("id").getAsString())));
            Check.notNull(material, "Material cannot be null");
            int amount = object.get("amount").getAsInt();

            return ItemStack.fromNBT(material, parseNBT(object.get("meta").getAsString()), amount);
        } else {
            return ItemStack.AIR;
        }
    }

    @Override
    public JsonElement toJson() {
        if (hasDefaultValue()) {
            return parse(defaultValue());
        }
        return new JsonObject();
    }

    public NBTCompound parseNBT(@NotNull String snbt) throws ArgumentSyntaxException {
        final String sNBT = snbt.replace("\\\"", "\"");

        NBTCompound compound;
        try {
            compound = (NBTCompound) new SNBTParser(new StringReader(sNBT)).parse();
        } catch (NBTException ignored) {
            compound = new NBTCompound();
        }

        return compound;

    }
}
