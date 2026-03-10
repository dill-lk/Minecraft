/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.mayaan.network.protocol.game;

import com.google.common.collect.Sets;
import java.util.Set;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.CommonPlayerSpawnInfo;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.Level;

public record ClientboundLoginPacket(int playerId, boolean hardcore, Set<ResourceKey<Level>> levels, int maxPlayers, int chunkRadius, int simulationDistance, boolean reducedDebugInfo, boolean showDeathScreen, boolean doLimitedCrafting, CommonPlayerSpawnInfo commonPlayerSpawnInfo, boolean enforcesSecureChat) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLoginPacket> STREAM_CODEC = Packet.codec(ClientboundLoginPacket::write, ClientboundLoginPacket::new);

    private ClientboundLoginPacket(RegistryFriendlyByteBuf input) {
        this(input.readInt(), input.readBoolean(), input.readCollection(Sets::newHashSetWithExpectedSize, buf -> buf.readResourceKey(Registries.DIMENSION)), input.readVarInt(), input.readVarInt(), input.readVarInt(), input.readBoolean(), input.readBoolean(), input.readBoolean(), new CommonPlayerSpawnInfo(input), input.readBoolean());
    }

    private void write(RegistryFriendlyByteBuf output) {
        output.writeInt(this.playerId);
        output.writeBoolean(this.hardcore);
        output.writeCollection(this.levels, FriendlyByteBuf::writeResourceKey);
        output.writeVarInt(this.maxPlayers);
        output.writeVarInt(this.chunkRadius);
        output.writeVarInt(this.simulationDistance);
        output.writeBoolean(this.reducedDebugInfo);
        output.writeBoolean(this.showDeathScreen);
        output.writeBoolean(this.doLimitedCrafting);
        this.commonPlayerSpawnInfo.write(output);
        output.writeBoolean(this.enforcesSecureChat);
    }

    @Override
    public PacketType<ClientboundLoginPacket> type() {
        return GamePacketTypes.CLIENTBOUND_LOGIN;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleLogin(this);
    }
}

