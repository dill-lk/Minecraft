/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.RegistryFileCodec;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.ExtraCodecs;

public record Instrument(Holder<SoundEvent> soundEvent, float useDuration, float range, Component description) {
    public static final Codec<Instrument> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound_event").forGetter(Instrument::soundEvent), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("use_duration").forGetter(Instrument::useDuration), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("range").forGetter(Instrument::range), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(Instrument::description)).apply((Applicative)i, Instrument::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Instrument> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, Instrument::soundEvent, ByteBufCodecs.FLOAT, Instrument::useDuration, ByteBufCodecs.FLOAT, Instrument::range, ComponentSerialization.STREAM_CODEC, Instrument::description, Instrument::new);
    public static final Codec<Holder<Instrument>> CODEC = RegistryFileCodec.create(Registries.INSTRUMENT, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Instrument>> STREAM_CODEC = ByteBufCodecs.holder(Registries.INSTRUMENT, DIRECT_STREAM_CODEC);
}

