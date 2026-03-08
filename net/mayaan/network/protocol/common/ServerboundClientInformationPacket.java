/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.network.protocol.common.ServerCommonPacketListener;
import net.mayaan.server.level.ClientInformation;

public record ServerboundClientInformationPacket(ClientInformation information) implements Packet<ServerCommonPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundClientInformationPacket> STREAM_CODEC = Packet.codec(ServerboundClientInformationPacket::write, ServerboundClientInformationPacket::new);

    private ServerboundClientInformationPacket(FriendlyByteBuf input) {
        this(new ClientInformation(input));
    }

    private void write(FriendlyByteBuf output) {
        this.information.write(output);
    }

    @Override
    public PacketType<ServerboundClientInformationPacket> type() {
        return CommonPacketTypes.SERVERBOUND_CLIENT_INFORMATION;
    }

    @Override
    public void handle(ServerCommonPacketListener listener) {
        listener.handleClientInformation(this);
    }
}

