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

public class ServerboundUseItemPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundUseItemPacket> STREAM_CODEC = Packet.codec(ServerboundUseItemPacket::write, ServerboundUseItemPacket::new);
    private final InteractionHand hand;
    private final int sequence;
    private final float yRot;
    private final float xRot;

    public ServerboundUseItemPacket(InteractionHand hand, int sequence, float yRot, float xRot) {
        this.hand = hand;
        this.sequence = sequence;
        this.yRot = yRot;
        this.xRot = xRot;
    }

    private ServerboundUseItemPacket(FriendlyByteBuf input) {
        this.hand = input.readEnum(InteractionHand.class);
        this.sequence = input.readVarInt();
        this.yRot = input.readFloat();
        this.xRot = input.readFloat();
    }

    private void write(FriendlyByteBuf output) {
        output.writeEnum(this.hand);
        output.writeVarInt(this.sequence);
        output.writeFloat(this.yRot);
        output.writeFloat(this.xRot);
    }

    @Override
    public PacketType<ServerboundUseItemPacket> type() {
        return GamePacketTypes.SERVERBOUND_USE_ITEM;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleUseItem(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public int getSequence() {
        return this.sequence;
    }

    public float getYRot() {
        return this.yRot;
    }

    public float getXRot() {
        return this.xRot;
    }
}

