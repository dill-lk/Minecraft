/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debug;

import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.entity.BeehiveBlockEntity;

public record DebugHiveInfo(Block type, int occupantCount, int honeyLevel, boolean sedated) {
    public static final StreamCodec<RegistryFriendlyByteBuf, DebugHiveInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(Registries.BLOCK), DebugHiveInfo::type, ByteBufCodecs.VAR_INT, DebugHiveInfo::occupantCount, ByteBufCodecs.VAR_INT, DebugHiveInfo::honeyLevel, ByteBufCodecs.BOOL, DebugHiveInfo::sedated, DebugHiveInfo::new);

    public static DebugHiveInfo pack(BeehiveBlockEntity beehive) {
        return new DebugHiveInfo(beehive.getBlockState().getBlock(), beehive.getOccupantCount(), BeehiveBlockEntity.getHoneyLevel(beehive.getBlockState()), beehive.isSedated());
    }
}

