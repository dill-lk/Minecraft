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

public class ServerboundClientCommandPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundClientCommandPacket> STREAM_CODEC = Packet.codec(ServerboundClientCommandPacket::write, ServerboundClientCommandPacket::new);
    private final Action action;

    public ServerboundClientCommandPacket(Action action) {
        this.action = action;
    }

    private ServerboundClientCommandPacket(FriendlyByteBuf input) {
        this.action = input.readEnum(Action.class);
    }

    private void write(FriendlyByteBuf output) {
        output.writeEnum(this.action);
    }

    @Override
    public PacketType<ServerboundClientCommandPacket> type() {
        return GamePacketTypes.SERVERBOUND_CLIENT_COMMAND;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleClientCommand(this);
    }

    public Action getAction() {
        return this.action;
    }

    public static enum Action {
        PERFORM_RESPAWN,
        REQUEST_STATS,
        REQUEST_GAMERULE_VALUES;

    }
}

