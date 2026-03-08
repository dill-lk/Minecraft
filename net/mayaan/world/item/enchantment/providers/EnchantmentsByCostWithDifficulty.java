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
import java.util.List;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.Registries;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.Enchantment;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.item.enchantment.EnchantmentInstance;
import net.mayaan.world.item.enchantment.ItemEnchantments;
import net.mayaan.world.item.enchantment.providers.EnchantmentProvider;

public record EnchantmentsByCostWithDifficulty(HolderSet<Enchantment> enchantments, int minCost, int maxCostSpan) implements EnchantmentProvider
{
    public static final int MAX_ALLOWED_VALUE_PART = 10000;
    public static final MapCodec<EnchantmentsByCostWithDifficulty> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantmentsByCostWithDifficulty::enchantments), (App)ExtraCodecs.intRange(1, 10000).fieldOf("min_cost").forGetter(EnchantmentsByCostWithDifficulty::minCost), (App)ExtraCodecs.intRange(0, 10000).fieldOf("max_cost_span").forGetter(EnchantmentsByCostWithDifficulty::maxCostSpan)).apply((Applicative)i, EnchantmentsByCostWithDifficulty::new));

    @Override
    public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
        float difficultyModifier = difficulty.getSpecialMultiplier();
        int cost = Mth.randomBetweenInclusive(random, this.minCost, this.minCost + (int)(difficultyModifier * (float)this.maxCostSpan));
        List<EnchantmentInstance> instances = EnchantmentHelper.selectEnchantment(random, item, cost, this.enchantments.stream());
        for (EnchantmentInstance instance : instances) {
            itemEnchantments.upgrade(instance.enchantment(), instance.level());
        }
    }

    public MapCodec<EnchantmentsByCostWithDifficulty> codec() {
        return CODEC;
    }
}

