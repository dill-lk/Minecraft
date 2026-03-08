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
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.consume_effects.ConsumeEffect;
import net.mayaan.world.level.Level;

public record PlaySoundConsumeEffect(Holder<SoundEvent> sound) implements ConsumeEffect
{
    public static final MapCodec<PlaySoundConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundConsumeEffect::sound)).apply((Applicative)i, PlaySoundConsumeEffect::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlaySoundConsumeEffect> STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, PlaySoundConsumeEffect::sound, PlaySoundConsumeEffect::new);

    public ConsumeEffect.Type<PlaySoundConsumeEffect> getType() {
        return ConsumeEffect.Type.PLAY_SOUND;
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        level.playSound(null, user.blockPosition(), this.sound.value(), user.getSoundSource(), 1.0f, 1.0f);
        return true;
    }
}

