/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debug;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;

public record DebugGameEventListenerInfo(int listenerRadius) {
    public static final StreamCodec<RegistryFriendlyByteBuf, DebugGameEventListenerInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, DebugGameEventListenerInfo::listenerRadius, DebugGameEventListenerInfo::new);
}

