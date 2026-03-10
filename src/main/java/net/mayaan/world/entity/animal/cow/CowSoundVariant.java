/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.entity.animal.cow;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.RegistryFixedCodec;
import net.mayaan.sounds.SoundEvent;

public record CowSoundVariant(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound) {
    public static final Codec<CowSoundVariant> DIRECT_CODEC = CowSoundVariant.codec();
    public static final Codec<CowSoundVariant> NETWORK_CODEC = CowSoundVariant.codec();
    public static final Codec<Holder<CowSoundVariant>> CODEC = RegistryFixedCodec.create(Registries.COW_SOUND_VARIANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CowSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.COW_SOUND_VARIANT);

    private static Codec<CowSoundVariant> codec() {
        return RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(CowSoundVariant::ambientSound), (App)SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(CowSoundVariant::hurtSound), (App)SoundEvent.CODEC.fieldOf("death_sound").forGetter(CowSoundVariant::deathSound), (App)SoundEvent.CODEC.fieldOf("step_sound").forGetter(CowSoundVariant::stepSound)).apply((Applicative)i, CowSoundVariant::new));
    }
}

