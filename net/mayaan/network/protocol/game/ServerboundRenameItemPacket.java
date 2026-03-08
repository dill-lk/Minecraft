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

public class ServerboundRenameItemPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundRenameItemPacket> STREAM_CODEC = Packet.codec(ServerboundRenameItemPacket::write, ServerboundRenameItemPacket::new);
    private final String name;

    public ServerboundRenameItemPacket(String name) {
        this.name = name;
    }

    private ServerboundRenameItemPacket(FriendlyByteBuf input) {
        this.name = input.readUtf();
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.name);
    }

    @Override
    public PacketType<ServerboundRenameItemPacket> type() {
        return GamePacketTypes.SERVERBOUND_RENAME_ITEM;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleRenameItem(this);
    }

    public String getName() {
        return this.name;
    }
}

