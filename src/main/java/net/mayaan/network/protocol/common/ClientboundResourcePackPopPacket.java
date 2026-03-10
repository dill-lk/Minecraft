/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import java.util.Optional;
import java.util.UUID;
import net.mayaan.core.UUIDUtil;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;

public record ClientboundResourcePackPopPacket(Optional<UUID> id) implements Packet<ClientCommonPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundResourcePackPopPacket> STREAM_CODEC = Packet.codec(ClientboundResourcePackPopPacket::write, ClientboundResourcePackPopPacket::new);

    private ClientboundResourcePackPopPacket(FriendlyByteBuf input) {
        this(input.readOptional(UUIDUtil.STREAM_CODEC));
    }

    private void write(FriendlyByteBuf output) {
        output.writeOptional(this.id, UUIDUtil.STREAM_CODEC);
    }

    @Override
    public PacketType<ClientboundResourcePackPopPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleResourcePackPop(this);
    }
}

