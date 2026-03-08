/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.consume_effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.consume_effects.ConsumeEffect;
import net.mayaan.world.level.Level;

public record RemoveStatusEffectsConsumeEffect(HolderSet<MobEffect> effects) implements ConsumeEffect
{
    public static final MapCodec<RemoveStatusEffectsConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("effects").forGetter(RemoveStatusEffectsConsumeEffect::effects)).apply((Applicative)i, RemoveStatusEffectsConsumeEffect::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.holderSet(Registries.MOB_EFFECT), RemoveStatusEffectsConsumeEffect::effects, RemoveStatusEffectsConsumeEffect::new);

    public RemoveStatusEffectsConsumeEffect(Holder<MobEffect> only) {
        this(HolderSet.direct(only));
    }

    public ConsumeEffect.Type<RemoveStatusEffectsConsumeEffect> getType() {
        return ConsumeEffect.Type.REMOVE_EFFECTS;
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        boolean hasRemovedAny = false;
        for (Holder holder : this.effects) {
            if (!user.removeEffect(holder)) continue;
            hasRemovedAny = true;
        }
        return hasRemovedAny;
    }
}

