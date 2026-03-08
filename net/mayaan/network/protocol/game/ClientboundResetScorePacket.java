/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import org.jspecify.annotations.Nullable;

public record ClientboundResetScorePacket(String owner, @Nullable String objectiveName) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundResetScorePacket> STREAM_CODEC = Packet.codec(ClientboundResetScorePacket::write, ClientboundResetScorePacket::new);

    private ClientboundResetScorePacket(FriendlyByteBuf input) {
        this(input.readUtf(), input.readNullable(FriendlyByteBuf::readUtf));
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.owner);
        output.writeNullable(this.objectiveName, FriendlyByteBuf::writeUtf);
    }

    @Override
    public PacketType<ClientboundResetScorePacket> type() {
        return GamePacketTypes.CLIENTBOUND_RESET_SCORE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleResetScore(this);
    }
}

