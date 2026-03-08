/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.UUID;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public class ServerboundTeleportToEntityPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundTeleportToEntityPacket> STREAM_CODEC = Packet.codec(ServerboundTeleportToEntityPacket::write, ServerboundTeleportToEntityPacket::new);
    private final UUID uuid;

    public ServerboundTeleportToEntityPacket(UUID uuid) {
        this.uuid = uuid;
    }

    private ServerboundTeleportToEntityPacket(FriendlyByteBuf input) {
        this.uuid = input.readUUID();
    }

    private void write(FriendlyByteBuf output) {
        output.writeUUID(this.uuid);
    }

    @Override
    public PacketType<ServerboundTeleportToEntityPacket> type() {
        return GamePacketTypes.SERVERBOUND_TELEPORT_TO_ENTITY;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleTeleportToEntityPacket(this);
    }

    public @Nullable Entity getEntity(ServerLevel level) {
        return level.getEntity(this.uuid);
    }
}

