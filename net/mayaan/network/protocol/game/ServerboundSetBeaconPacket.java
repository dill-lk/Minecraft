/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.effect.MobEffect;

public record ServerboundSetBeaconPacket(Optional<Holder<MobEffect>> primary, Optional<Holder<MobEffect>> secondary) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetBeaconPacket> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundSetBeaconPacket::primary, MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundSetBeaconPacket::secondary, ServerboundSetBeaconPacket::new);

    @Override
    public PacketType<ServerboundSetBeaconPacket> type() {
        return GamePacketTypes.SERVERBOUND_SET_BEACON;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleSetBeaconPacket(this);
    }
}

