/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.gamerules.GameRule;

public record ServerboundSetGameRulePacket(List<Entry> entries) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundSetGameRulePacket> STREAM_CODEC = StreamCodec.composite(Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundSetGameRulePacket::entries, ServerboundSetGameRulePacket::new);

    @Override
    public PacketType<ServerboundSetGameRulePacket> type() {
        return GamePacketTypes.SERVERBOUND_SET_GAME_RULE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleSetGameRule(this);
    }

    public record Entry(ResourceKey<GameRule<?>> gameRuleKey, String value) {
        public static final StreamCodec<ByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(ResourceKey.streamCodec(Registries.GAME_RULE), Entry::gameRuleKey, ByteBufCodecs.STRING_UTF8, Entry::value, Entry::new);
    }
}

