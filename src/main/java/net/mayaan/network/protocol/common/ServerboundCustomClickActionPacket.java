/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.common;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.mayaan.nbt.NbtAccounter;
import net.mayaan.nbt.Tag;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.network.protocol.common.ServerCommonPacketListener;
import net.mayaan.resources.Identifier;

public record ServerboundCustomClickActionPacket(Identifier id, Optional<Tag> payload) implements Packet<ServerCommonPacketListener>
{
    private static final StreamCodec<ByteBuf, Optional<Tag>> UNTRUSTED_TAG_CODEC = ByteBufCodecs.optionalTagCodec(() -> new NbtAccounter(32768L, 16)).apply(ByteBufCodecs.lengthPrefixed(65536));
    public static final StreamCodec<ByteBuf, ServerboundCustomClickActionPacket> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, ServerboundCustomClickActionPacket::id, UNTRUSTED_TAG_CODEC, ServerboundCustomClickActionPacket::payload, ServerboundCustomClickActionPacket::new);

    @Override
    public PacketType<ServerboundCustomClickActionPacket> type() {
        return CommonPacketTypes.SERVERBOUND_CUSTOM_CLICK_ACTION;
    }

    @Override
    public void handle(ServerCommonPacketListener listener) {
        listener.handleCustomClickAction(this);
    }
}

