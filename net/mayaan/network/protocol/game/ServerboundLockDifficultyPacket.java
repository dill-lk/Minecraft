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

public class ServerboundLockDifficultyPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundLockDifficultyPacket> STREAM_CODEC = Packet.codec(ServerboundLockDifficultyPacket::write, ServerboundLockDifficultyPacket::new);
    private final boolean locked;

    public ServerboundLockDifficultyPacket(boolean locked) {
        this.locked = locked;
    }

    private ServerboundLockDifficultyPacket(FriendlyByteBuf input) {
        this.locked = input.readBoolean();
    }

    private void write(FriendlyByteBuf output) {
        output.writeBoolean(this.locked);
    }

    @Override
    public PacketType<ServerboundLockDifficultyPacket> type() {
        return GamePacketTypes.SERVERBOUND_LOCK_DIFFICULTY;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleLockDifficulty(this);
    }

    public boolean isLocked() {
        return this.locked;
    }
}

