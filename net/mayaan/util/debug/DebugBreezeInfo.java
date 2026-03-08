/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.util.debug;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;

public record DebugBreezeInfo(Optional<Integer> attackTarget, Optional<BlockPos> jumpTarget) {
    public static final StreamCodec<ByteBuf, DebugBreezeInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT.apply(ByteBufCodecs::optional), DebugBreezeInfo::attackTarget, BlockPos.STREAM_CODEC.apply(ByteBufCodecs::optional), DebugBreezeInfo::jumpTarget, DebugBreezeInfo::new);
}

