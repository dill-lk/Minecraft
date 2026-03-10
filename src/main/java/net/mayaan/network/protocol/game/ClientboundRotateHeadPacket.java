/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class ClientboundRotateHeadPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundRotateHeadPacket> STREAM_CODEC = Packet.codec(ClientboundRotateHeadPacket::write, ClientboundRotateHeadPacket::new);
    private final int entityId;
    private final byte yHeadRot;

    public ClientboundRotateHeadPacket(Entity entity, byte yHeadRot) {
        this.entityId = entity.getId();
        this.yHeadRot = yHeadRot;
    }

    private ClientboundRotateHeadPacket(FriendlyByteBuf input) {
        this.entityId = input.readVarInt();
        this.yHeadRot = input.readByte();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.entityId);
        output.writeByte(this.yHeadRot);
    }

    @Override
    public PacketType<ClientboundRotateHeadPacket> type() {
        return GamePacketTypes.CLIENTBOUND_ROTATE_HEAD;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleRotateMob(this);
    }

    public @Nullable Entity getEntity(Level level) {
        return level.getEntity(this.entityId);
    }

    public float getYHeadRot() {
        return Mth.unpackDegrees(this.yHeadRot);
    }
}

