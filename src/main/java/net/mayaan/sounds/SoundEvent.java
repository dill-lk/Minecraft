/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.sounds;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.RegistryFileCodec;

public record SoundEvent(Identifier location, Optional<Float> fixedRange) {
    public static final Codec<SoundEvent> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sound_id").forGetter(SoundEvent::location), (App)Codec.FLOAT.lenientOptionalFieldOf("range").forGetter(SoundEvent::fixedRange)).apply((Applicative)i, SoundEvent::create));
    public static final Codec<Holder<SoundEvent>> CODEC = RegistryFileCodec.create(Registries.SOUND_EVENT, DIRECT_CODEC);
    public static final StreamCodec<ByteBuf, SoundEvent> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, SoundEvent::location, ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional), SoundEvent::fixedRange, SoundEvent::create);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SoundEvent>> STREAM_CODEC = ByteBufCodecs.holder(Registries.SOUND_EVENT, DIRECT_STREAM_CODEC);

    private static SoundEvent create(Identifier location, Optional<Float> range) {
        return range.map(r -> SoundEvent.createFixedRangeEvent(location, r.floatValue())).orElseGet(() -> SoundEvent.createVariableRangeEvent(location));
    }

    public static SoundEvent createVariableRangeEvent(Identifier location) {
        return new SoundEvent(location, Optional.empty());
    }

    public static SoundEvent createFixedRangeEvent(Identifier location, float range) {
        return new SoundEvent(location, Optional.of(Float.valueOf(range)));
    }

    public float getRange(float volume) {
        return this.fixedRange.orElse(Float.valueOf(volume > 1.0f ? 16.0f * volume : 16.0f)).floatValue();
    }
}

