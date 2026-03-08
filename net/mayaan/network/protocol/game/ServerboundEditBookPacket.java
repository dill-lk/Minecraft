/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.List;
import java.util.Optional;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public record ServerboundEditBookPacket(int slot, List<String> pages, Optional<String> title) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundEditBookPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundEditBookPacket::slot, ByteBufCodecs.stringUtf8(1024).apply(ByteBufCodecs.list(100)), ServerboundEditBookPacket::pages, ByteBufCodecs.stringUtf8(32).apply(ByteBufCodecs::optional), ServerboundEditBookPacket::title, ServerboundEditBookPacket::new);

    public ServerboundEditBookPacket {
        pages = List.copyOf(pages);
    }

    @Override
    public PacketType<ServerboundEditBookPacket> type() {
        return GamePacketTypes.SERVERBOUND_EDIT_BOOK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleEditBook(this);
    }
}

