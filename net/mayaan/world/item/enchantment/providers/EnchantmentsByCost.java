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
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.IntProvider;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.Enchantment;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.item.enchantment.EnchantmentInstance;
import net.mayaan.world.item.enchantment.ItemEnchantments;
import net.mayaan.world.item.enchantment.providers.EnchantmentProvider;

public record EnchantmentsByCost(HolderSet<Enchantment> enchantments, IntProvider cost) implements EnchantmentProvider
{
    public static final MapCodec<EnchantmentsByCost> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantmentsByCost::enchantments), (App)IntProvider.CODEC.fieldOf("cost").forGetter(EnchantmentsByCost::cost)).apply((Applicative)i, EnchantmentsByCost::new));

    @Override
    public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
        List<EnchantmentInstance> instances = EnchantmentHelper.selectEnchantment(random, item, this.cost.sample(random), this.enchantments.stream());
        for (EnchantmentInstance instance : instances) {
            itemEnchantments.upgrade(instance.enchantment(), instance.level());
        }
    }

    public MapCodec<EnchantmentsByCost> codec() {
        return CODEC;
    }
}

