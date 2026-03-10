/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.consume_effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.consume_effects.ConsumeEffect;
import net.mayaan.world.level.Level;

public record ApplyStatusEffectsConsumeEffect(List<MobEffectInstance> effects, float probability) implements ConsumeEffect
{
    public static final MapCodec<ApplyStatusEffectsConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(ApplyStatusEffectsConsumeEffect::effects), (App)Codec.floatRange((float)0.0f, (float)1.0f).optionalFieldOf("probability", (Object)Float.valueOf(1.0f)).forGetter(ApplyStatusEffectsConsumeEffect::probability)).apply((Applicative)i, ApplyStatusEffectsConsumeEffect::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ApplyStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.composite(MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()), ApplyStatusEffectsConsumeEffect::effects, ByteBufCodecs.FLOAT, ApplyStatusEffectsConsumeEffect::probability, ApplyStatusEffectsConsumeEffect::new);

    public ApplyStatusEffectsConsumeEffect(MobEffectInstance effect, float probability) {
        this(List.of(effect), probability);
    }

    public ApplyStatusEffectsConsumeEffect(List<MobEffectInstance> effects) {
        this(effects, 1.0f);
    }

    public ApplyStatusEffectsConsumeEffect(MobEffectInstance effect) {
        this(effect, 1.0f);
    }

    public ConsumeEffect.Type<ApplyStatusEffectsConsumeEffect> getType() {
        return ConsumeEffect.Type.APPLY_EFFECTS;
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        if (user.getRandom().nextFloat() >= this.probability) {
            return false;
        }
        boolean anyApplied = false;
        for (MobEffectInstance effect : this.effects) {
            if (!user.addEffect(new MobEffectInstance(effect))) continue;
            anyApplied = true;
        }
        return anyApplied;
    }
}

