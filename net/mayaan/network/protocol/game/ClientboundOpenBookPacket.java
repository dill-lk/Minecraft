/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.InteractionHand;

public class ClientboundOpenBookPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundOpenBookPacket> STREAM_CODEC = Packet.codec(ClientboundOpenBookPacket::write, ClientboundOpenBookPacket::new);
    private final InteractionHand hand;

    public ClientboundOpenBookPacket(InteractionHand hand) {
        this.hand = hand;
    }

    private ClientboundOpenBookPacket(FriendlyByteBuf input) {
        this.hand = input.readEnum(InteractionHand.class);
    }

    private void write(FriendlyByteBuf output) {
        output.writeEnum(this.hand);
    }

    @Override
    public PacketType<ClientboundOpenBookPacket> type() {
        return GamePacketTypes.CLIENTBOUND_OPEN_BOOK;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleOpenBook(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }
}

