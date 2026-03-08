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
package net.minecraft.world.clock;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClockState(long totalTicks, boolean paused) {
    public static final Codec<ClockState> CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.LONG.fieldOf("total_ticks").forGetter(ClockState::totalTicks), (App)Codec.BOOL.optionalFieldOf("paused", (Object)false).forGetter(ClockState::paused)).apply((Applicative)i, ClockState::new));
    public static final StreamCodec<ByteBuf, ClockState> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, ClockState::totalTicks, ByteBufCodecs.BOOL, ClockState::paused, ClockState::new);
}

