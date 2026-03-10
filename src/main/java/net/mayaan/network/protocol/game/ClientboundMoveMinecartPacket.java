/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.List;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public record ClientboundMoveMinecartPacket(int entityId, List<NewMinecartBehavior.MinecartStep> lerpSteps) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundMoveMinecartPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundMoveMinecartPacket::entityId, NewMinecartBehavior.MinecartStep.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundMoveMinecartPacket::lerpSteps, ClientboundMoveMinecartPacket::new);

    @Override
    public PacketType<ClientboundMoveMinecartPacket> type() {
        return GamePacketTypes.CLIENTBOUND_MOVE_MINECART_ALONG_TRACK;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleMinecartAlongTrack(this);
    }

    public @Nullable Entity getEntity(Level level) {
        return level.getEntity(this.entityId);
    }
}

