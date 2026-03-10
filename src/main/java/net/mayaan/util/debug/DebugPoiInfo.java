/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debug;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.entity.ai.village.poi.PoiRecord;
import net.mayaan.world.entity.ai.village.poi.PoiType;

public record DebugPoiInfo(BlockPos pos, Holder<PoiType> poiType, int freeTicketCount) {
    public static final StreamCodec<RegistryFriendlyByteBuf, DebugPoiInfo> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, DebugPoiInfo::pos, ByteBufCodecs.holderRegistry(Registries.POINT_OF_INTEREST_TYPE), DebugPoiInfo::poiType, ByteBufCodecs.VAR_INT, DebugPoiInfo::freeTicketCount, DebugPoiInfo::new);

    public DebugPoiInfo(PoiRecord record) {
        this(record.getPos(), record.getPoiType(), record.getFreeTickets());
    }
}

