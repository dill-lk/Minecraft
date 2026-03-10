/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.List;
import java.util.UUID;
import net.mayaan.core.UUIDUtil;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundPlayerInfoRemovePacket(List<UUID> profileIds) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerInfoRemovePacket> STREAM_CODEC = Packet.codec(ClientboundPlayerInfoRemovePacket::write, ClientboundPlayerInfoRemovePacket::new);

    private ClientboundPlayerInfoRemovePacket(FriendlyByteBuf input) {
        this(input.readList(UUIDUtil.STREAM_CODEC));
    }

    private void write(FriendlyByteBuf output) {
        output.writeCollection(this.profileIds, UUIDUtil.STREAM_CODEC);
    }

    @Override
    public PacketType<ClientboundPlayerInfoRemovePacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLAYER_INFO_REMOVE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handlePlayerInfoRemove(this);
    }
}

