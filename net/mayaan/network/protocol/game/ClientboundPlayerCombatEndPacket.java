/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.damagesource.CombatTracker;

public class ClientboundPlayerCombatEndPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerCombatEndPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerCombatEndPacket::write, ClientboundPlayerCombatEndPacket::new);
    private final int duration;

    public ClientboundPlayerCombatEndPacket(CombatTracker tracker) {
        this(tracker.getCombatDuration());
    }

    public ClientboundPlayerCombatEndPacket(int duration) {
        this.duration = duration;
    }

    private ClientboundPlayerCombatEndPacket(FriendlyByteBuf input) {
        this.duration = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.duration);
    }

    @Override
    public PacketType<ClientboundPlayerCombatEndPacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_END;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handlePlayerCombatEnd(this);
    }
}

