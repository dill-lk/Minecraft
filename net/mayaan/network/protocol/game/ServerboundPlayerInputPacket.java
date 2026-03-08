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
import net.mayaan.world.entity.player.Input;

public record ServerboundPlayerInputPacket(Input input) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerInputPacket> STREAM_CODEC = StreamCodec.composite(Input.STREAM_CODEC, ServerboundPlayerInputPacket::input, ServerboundPlayerInputPacket::new);

    @Override
    public PacketType<ServerboundPlayerInputPacket> type() {
        return GamePacketTypes.SERVERBOUND_PLAYER_INPUT;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handlePlayerInput(this);
    }
}

