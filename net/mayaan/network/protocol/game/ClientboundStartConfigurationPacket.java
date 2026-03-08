/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public class ClientboundStartConfigurationPacket
implements Packet<ClientGamePacketListener> {
    public static final ClientboundStartConfigurationPacket INSTANCE = new ClientboundStartConfigurationPacket();
    public static final StreamCodec<ByteBuf, ClientboundStartConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ClientboundStartConfigurationPacket() {
    }

    @Override
    public PacketType<ClientboundStartConfigurationPacket> type() {
        return GamePacketTypes.CLIENTBOUND_START_CONFIGURATION;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleConfigurationStart(this);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}

