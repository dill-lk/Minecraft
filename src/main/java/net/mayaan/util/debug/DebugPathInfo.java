/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debug;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.level.pathfinder.Path;

public record DebugPathInfo(Path path, float maxNodeDistance) {
    public static final StreamCodec<FriendlyByteBuf, DebugPathInfo> STREAM_CODEC = StreamCodec.composite(Path.STREAM_CODEC, DebugPathInfo::path, ByteBufCodecs.FLOAT, DebugPathInfo::maxNodeDistance, DebugPathInfo::new);
}

