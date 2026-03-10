/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.UUID;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.ChatType;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.chat.FilterMask;
import net.mayaan.network.chat.MessageSignature;
import net.mayaan.network.chat.SignedMessageBody;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import org.jspecify.annotations.Nullable;

public record ClientboundPlayerChatPacket(int globalIndex, UUID sender, int index, @Nullable MessageSignature signature, SignedMessageBody.Packed body, @Nullable Component unsignedContent, FilterMask filterMask, ChatType.Bound chatType) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerChatPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerChatPacket::write, ClientboundPlayerChatPacket::new);

    private ClientboundPlayerChatPacket(RegistryFriendlyByteBuf input) {
        this(input.readVarInt(), input.readUUID(), input.readVarInt(), input.readNullable(MessageSignature::read), new SignedMessageBody.Packed(input), FriendlyByteBuf.readNullable(input, ComponentSerialization.TRUSTED_STREAM_CODEC), FilterMask.read(input), (ChatType.Bound)ChatType.Bound.STREAM_CODEC.decode(input));
    }

    private void write(RegistryFriendlyByteBuf output) {
        output.writeVarInt(this.globalIndex);
        output.writeUUID(this.sender);
        output.writeVarInt(this.index);
        output.writeNullable(this.signature, MessageSignature::write);
        this.body.write(output);
        FriendlyByteBuf.writeNullable(output, this.unsignedContent, ComponentSerialization.TRUSTED_STREAM_CODEC);
        FilterMask.write(output, this.filterMask);
        ChatType.Bound.STREAM_CODEC.encode(output, this.chatType);
    }

    @Override
    public PacketType<ClientboundPlayerChatPacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLAYER_CHAT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handlePlayerChat(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}

