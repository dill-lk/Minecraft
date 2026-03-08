/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.HashMap;
import java.util.Map;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.gamerules.GameRule;

public record ClientboundGameRuleValuesPacket(Map<ResourceKey<GameRule<?>>, String> values) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundGameRuleValuesPacket> STREAM_CODEC = ByteBufCodecs.map(HashMap::new, ResourceKey.streamCodec(Registries.GAME_RULE), ByteBufCodecs.STRING_UTF8).map(ClientboundGameRuleValuesPacket::new, ClientboundGameRuleValuesPacket::values);

    @Override
    public PacketType<ClientboundGameRuleValuesPacket> type() {
        return GamePacketTypes.CLIENTBOUND_GAME_RULE_VALUES;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleGameRuleValues(this);
    }
}

