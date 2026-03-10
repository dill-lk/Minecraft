/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.enchantment.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.RandomSource;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.ItemEnchantments;

public interface EnchantmentProvider {
    public static final Codec<EnchantmentProvider> DIRECT_CODEC = BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE.byNameCodec().dispatch(EnchantmentProvider::codec, Function.identity());

    public void enchant(ItemStack var1, ItemEnchantments.Mutable var2, RandomSource var3, DifficultyInstance var4);

    public MapCodec<? extends EnchantmentProvider> codec();
}

