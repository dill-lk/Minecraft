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
import net.mayaan.core.component.DataComponents;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.LevelBasedValue;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.phys.Vec3;

public record ChangeItemDamage(LevelBasedValue amount) implements EnchantmentEntityEffect
{
    public static final MapCodec<ChangeItemDamage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(e -> e.amount)).apply((Applicative)i, ChangeItemDamage::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        ItemStack itemStack = item.itemStack();
        if (itemStack.has(DataComponents.MAX_DAMAGE) && itemStack.has(DataComponents.DAMAGE)) {
            ServerPlayer sp;
            LivingEntity livingEntity = item.owner();
            ServerPlayer player = livingEntity instanceof ServerPlayer ? (sp = (ServerPlayer)livingEntity) : null;
            int change = (int)this.amount.calculate(enchantmentLevel);
            itemStack.hurtAndBreak(change, serverLevel, player, item.onBreak());
        }
    }

    public MapCodec<ChangeItemDamage> codec() {
        return CODEC;
    }
}

