/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.enchantment.providers;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.IntProvider;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.Enchantment;
import net.mayaan.world.item.enchantment.ItemEnchantments;
import net.mayaan.world.item.enchantment.providers.EnchantmentProvider;

public record SingleEnchantment(Holder<Enchantment> enchantment, IntProvider level) implements EnchantmentProvider
{
    public static final MapCodec<SingleEnchantment> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(SingleEnchantment::enchantment), (App)IntProvider.CODEC.fieldOf("level").forGetter(SingleEnchantment::level)).apply((Applicative)i, SingleEnchantment::new));

    @Override
    public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
        itemEnchantments.upgrade(this.enchantment, Mth.clamp(this.level.sample(random), this.enchantment.value().getMinLevel(), this.enchantment.value().getMaxLevel()));
    }

    public MapCodec<SingleEnchantment> codec() {
        return CODEC;
    }
}

