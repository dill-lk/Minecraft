/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.clock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.RegistryFixedCodec;

public record WorldClock() {
    public static final Codec<Holder<WorldClock>> CODEC = RegistryFixedCodec.create(Registries.WORLD_CLOCK);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WorldClock>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WORLD_CLOCK);
    public static final Codec<WorldClock> DIRECT_CODEC = MapCodec.unitCodec(WorldClock::new);
}

