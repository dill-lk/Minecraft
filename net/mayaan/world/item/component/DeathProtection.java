/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.component;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.mayaan.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.mayaan.world.item.consume_effects.ConsumeEffect;

public record DeathProtection(List<ConsumeEffect> deathEffects) {
    public static final Codec<DeathProtection> CODEC = RecordCodecBuilder.create(i -> i.group((App)ConsumeEffect.CODEC.listOf().optionalFieldOf("death_effects", List.of()).forGetter(DeathProtection::deathEffects)).apply((Applicative)i, DeathProtection::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DeathProtection> STREAM_CODEC = StreamCodec.composite(ConsumeEffect.STREAM_CODEC.apply(ByteBufCodecs.list()), DeathProtection::deathEffects, DeathProtection::new);
    public static final DeathProtection TOTEM_OF_UNDYING = new DeathProtection(List.of(new ClearAllStatusEffectsConsumeEffect(), new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 900, 1), new MobEffectInstance(MobEffects.ABSORPTION, 100, 1), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0)))));

    public void applyEffects(ItemStack itemStack, LivingEntity entity) {
        for (ConsumeEffect effect : this.deathEffects) {
            effect.apply(entity.level(), itemStack, entity);
        }
    }
}

