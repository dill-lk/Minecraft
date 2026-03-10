/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.Optional;
import net.mayaan.core.Vec3i;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundTestInstanceBlockStatus(Component status, Optional<Vec3i> size) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTestInstanceBlockStatus> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, ClientboundTestInstanceBlockStatus::status, ByteBufCodecs.optional(Vec3i.STREAM_CODEC), ClientboundTestInstanceBlockStatus::size, ClientboundTestInstanceBlockStatus::new);

    @Override
    public PacketType<ClientboundTestInstanceBlockStatus> type() {
        return GamePacketTypes.CLIENTBOUND_TEST_INSTANCE_BLOCK_STATUS;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleTestInstanceBlockStatus(this);
    }
}

