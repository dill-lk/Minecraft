/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.util.RandomSource;
import net.mayaan.world.item.enchantment.LevelBasedValue;
import net.mayaan.world.item.enchantment.effects.EnchantmentValueEffect;

public record SetValue(LevelBasedValue value) implements EnchantmentValueEffect
{
    public static final MapCodec<SetValue> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("value").forGetter(SetValue::value)).apply((Applicative)i, SetValue::new));

    @Override
    public float process(int enchantmentLevel, RandomSource random, float inputValue) {
        return this.value.calculate(enchantmentLevel);
    }

    public MapCodec<SetValue> codec() {
        return CODEC;
    }
}

