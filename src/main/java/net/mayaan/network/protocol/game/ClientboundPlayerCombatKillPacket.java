/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundPlayerCombatKillPacket(int playerId, Component message) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerCombatKillPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundPlayerCombatKillPacket::playerId, ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundPlayerCombatKillPacket::message, ClientboundPlayerCombatKillPacket::new);

    @Override
    public PacketType<ClientboundPlayerCombatKillPacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_KILL;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handlePlayerCombatKill(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}

