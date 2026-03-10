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
import net.mayaan.core.Holder;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.Mth;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.damagesource.DamageType;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.LevelBasedValue;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.phys.Vec3;

public record DamageEntity(LevelBasedValue minDamage, LevelBasedValue maxDamage, Holder<DamageType> damageType) implements EnchantmentEntityEffect
{
    public static final MapCodec<DamageEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntity::minDamage), (App)LevelBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntity::maxDamage), (App)DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntity::damageType)).apply((Applicative)i, DamageEntity::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        float damage = Mth.randomBetween(entity.getRandom(), this.minDamage.calculate(enchantmentLevel), this.maxDamage.calculate(enchantmentLevel));
        entity.hurtServer(serverLevel, new DamageSource(this.damageType, item.owner()), damage);
    }

    public MapCodec<DamageEntity> codec() {
        return CODEC;
    }
}

