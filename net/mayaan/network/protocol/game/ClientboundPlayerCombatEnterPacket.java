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

public class ClientboundPlayerCombatEnterPacket
implements Packet<ClientGamePacketListener> {
    public static final ClientboundPlayerCombatEnterPacket INSTANCE = new ClientboundPlayerCombatEnterPacket();
    public static final StreamCodec<ByteBuf, ClientboundPlayerCombatEnterPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ClientboundPlayerCombatEnterPacket() {
    }

    @Override
    public PacketType<ClientboundPlayerCombatEnterPacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_ENTER;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handlePlayerCombatEnter(this);
    }
}

