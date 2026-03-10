/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.util.debug;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;

public record DebugBeeInfo(Optional<BlockPos> hivePos, Optional<BlockPos> flowerPos, int travelTicks, List<BlockPos> blacklistedHives) {
    public static final StreamCodec<ByteBuf, DebugBeeInfo> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC.apply(ByteBufCodecs::optional), DebugBeeInfo::hivePos, BlockPos.STREAM_CODEC.apply(ByteBufCodecs::optional), DebugBeeInfo::flowerPos, ByteBufCodecs.VAR_INT, DebugBeeInfo::travelTicks, BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), DebugBeeInfo::blacklistedHives, DebugBeeInfo::new);

    public boolean hasHive(BlockPos hivePos) {
        return this.hivePos.isPresent() && hivePos.equals(this.hivePos.get());
    }
}

