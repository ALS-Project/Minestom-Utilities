package fr.bretzel.minestom.utils;

import net.minestom.server.entity.Entity;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;

public class PotionUtils {

    public static boolean hasPotionEffect(PotionEffect potionEffect, Entity entity) {
        return entity.getActiveEffects().stream().anyMatch(timedPotion -> timedPotion.getPotion().effect() == potionEffect);
    }

    public static byte getPotionEffectLevel(PotionEffect potionEffect, Entity entity) {
        return entity.getActiveEffects().stream()
                .filter(timedPotion -> timedPotion.getPotion().effect() == potionEffect)
                .findFirst()
                .orElseGet(() -> new TimedPotion(new Potion(potionEffect, (byte) 0, 0), 0))
                .getPotion().amplifier();
    }
}
