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
import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.RegistryFixedCodec;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.Mth;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.JukeboxPlayable;

public record JukeboxSong(Holder<SoundEvent> soundEvent, Component description, float lengthInSeconds, int comparatorOutput) {
    public static final Codec<JukeboxSong> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)SoundEvent.CODEC.fieldOf("sound_event").forGetter(JukeboxSong::soundEvent), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(JukeboxSong::description), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("length_in_seconds").forGetter(JukeboxSong::lengthInSeconds), (App)ExtraCodecs.intRange(0, 15).fieldOf("comparator_output").forGetter(JukeboxSong::comparatorOutput)).apply((Applicative)i, JukeboxSong::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxSong> DIRECT_STREAM_CODEC = StreamCodec.composite(SoundEvent.STREAM_CODEC, JukeboxSong::soundEvent, ComponentSerialization.STREAM_CODEC, JukeboxSong::description, ByteBufCodecs.FLOAT, JukeboxSong::lengthInSeconds, ByteBufCodecs.VAR_INT, JukeboxSong::comparatorOutput, JukeboxSong::new);
    public static final Codec<Holder<JukeboxSong>> CODEC = RegistryFixedCodec.create(Registries.JUKEBOX_SONG);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<JukeboxSong>> STREAM_CODEC = ByteBufCodecs.holder(Registries.JUKEBOX_SONG, DIRECT_STREAM_CODEC);
    private static final int SONG_END_PADDING_TICKS = 20;

    public int lengthInTicks() {
        return Mth.ceil(this.lengthInSeconds * 20.0f);
    }

    public boolean hasFinished(long ticksElapsed) {
        return ticksElapsed >= (long)(this.lengthInTicks() + 20);
    }

    public static Optional<Holder<JukeboxSong>> fromStack(ItemStack stack) {
        JukeboxPlayable jukeboxPlayable = stack.get(DataComponents.JUKEBOX_PLAYABLE);
        if (jukeboxPlayable != null) {
            return Optional.of(jukeboxPlayable.song());
        }
        return Optional.empty();
    }
}

