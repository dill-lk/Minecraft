/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.enchantment.providers;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.world.item.enchantment.providers.EnchantmentProvider;
import net.mayaan.world.item.enchantment.providers.EnchantmentsByCost;
import net.mayaan.world.item.enchantment.providers.EnchantmentsByCostWithDifficulty;
import net.mayaan.world.item.enchantment.providers.SingleEnchantment;

public interface EnchantmentProviderTypes {
    public static MapCodec<? extends EnchantmentProvider> bootstrap(Registry<MapCodec<? extends EnchantmentProvider>> registry) {
        Registry.register(registry, "by_cost", EnchantmentsByCost.CODEC);
        Registry.register(registry, "by_cost_with_difficulty", EnchantmentsByCostWithDifficulty.CODEC);
        return Registry.register(registry, "single", SingleEnchantment.CODEC);
    }
}

