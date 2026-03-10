/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.common;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.UUID;
import net.mayaan.core.UUIDUtil;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;

public record ClientboundResourcePackPushPacket(UUID id, String url, String hash, boolean required, Optional<Component> prompt) implements Packet<ClientCommonPacketListener>
{
    public static final int MAX_HASH_LENGTH = 40;
    public static final StreamCodec<ByteBuf, ClientboundResourcePackPushPacket> STREAM_CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, ClientboundResourcePackPushPacket::id, ByteBufCodecs.STRING_UTF8, ClientboundResourcePackPushPacket::url, ByteBufCodecs.stringUtf8(40), ClientboundResourcePackPushPacket::hash, ByteBufCodecs.BOOL, ClientboundResourcePackPushPacket::required, ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.apply(ByteBufCodecs::optional), ClientboundResourcePackPushPacket::prompt, ClientboundResourcePackPushPacket::new);

    public ClientboundResourcePackPushPacket {
        if (hash.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
        }
    }

    @Override
    public PacketType<ClientboundResourcePackPushPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_PUSH;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleResourcePackPush(this);
    }
}

