/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.InteractionHand;

public class ServerboundSwingPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundSwingPacket> STREAM_CODEC = Packet.codec(ServerboundSwingPacket::write, ServerboundSwingPacket::new);
    private final InteractionHand hand;

    public ServerboundSwingPacket(InteractionHand hand) {
        this.hand = hand;
    }

    private ServerboundSwingPacket(FriendlyByteBuf input) {
        this.hand = input.readEnum(InteractionHand.class);
    }

    private void write(FriendlyByteBuf output) {
        output.writeEnum(this.hand);
    }

    @Override
    public PacketType<ServerboundSwingPacket> type() {
        return GamePacketTypes.SERVERBOUND_SWING;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleAnimate(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }
}

