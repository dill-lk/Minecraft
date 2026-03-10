/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.Optional;
import net.mayaan.core.GlobalPos;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.GameType;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.dimension.DimensionType;
import org.jspecify.annotations.Nullable;

public record CommonPlayerSpawnInfo(Holder<DimensionType> dimensionType, ResourceKey<Level> dimension, long seed, GameType gameType, @Nullable GameType previousGameType, boolean isDebug, boolean isFlat, Optional<GlobalPos> lastDeathLocation, int portalCooldown, int seaLevel) {
    public CommonPlayerSpawnInfo(RegistryFriendlyByteBuf input) {
        this((Holder)DimensionType.STREAM_CODEC.decode(input), input.readResourceKey(Registries.DIMENSION), input.readLong(), GameType.byId(input.readByte()), GameType.byNullableId(input.readByte()), input.readBoolean(), input.readBoolean(), input.readOptional(FriendlyByteBuf::readGlobalPos), input.readVarInt(), input.readVarInt());
    }

    public void write(RegistryFriendlyByteBuf output) {
        DimensionType.STREAM_CODEC.encode(output, this.dimensionType);
        output.writeResourceKey(this.dimension);
        output.writeLong(this.seed);
        output.writeByte(this.gameType.getId());
        output.writeByte(GameType.getNullableId(this.previousGameType));
        output.writeBoolean(this.isDebug);
        output.writeBoolean(this.isFlat);
        output.writeOptional(this.lastDeathLocation, FriendlyByteBuf::writeGlobalPos);
        output.writeVarInt(this.portalCooldown);
        output.writeVarInt(this.seaLevel);
    }
}

