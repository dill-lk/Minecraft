/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.core.BlockPos;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public class ClientboundBlockDestructionPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundBlockDestructionPacket> STREAM_CODEC = Packet.codec(ClientboundBlockDestructionPacket::write, ClientboundBlockDestructionPacket::new);
    private final int id;
    private final BlockPos pos;
    private final int progress;

    public ClientboundBlockDestructionPacket(int id, BlockPos pos, int progress) {
        this.id = id;
        this.pos = pos;
        this.progress = progress;
    }

    private ClientboundBlockDestructionPacket(FriendlyByteBuf input) {
        this.id = input.readVarInt();
        this.pos = input.readBlockPos();
        this.progress = input.readUnsignedByte();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.id);
        output.writeBlockPos(this.pos);
        output.writeByte(this.progress);
    }

    @Override
    public PacketType<ClientboundBlockDestructionPacket> type() {
        return GamePacketTypes.CLIENTBOUND_BLOCK_DESTRUCTION;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleBlockDestruction(this);
    }

    public int getId() {
        return this.id;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getProgress() {
        return this.progress;
    }
}

