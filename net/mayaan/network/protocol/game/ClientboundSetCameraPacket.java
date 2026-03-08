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
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class ClientboundSetCameraPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetCameraPacket> STREAM_CODEC = Packet.codec(ClientboundSetCameraPacket::write, ClientboundSetCameraPacket::new);
    private final int cameraId;

    public ClientboundSetCameraPacket(Entity camera) {
        this.cameraId = camera.getId();
    }

    private ClientboundSetCameraPacket(FriendlyByteBuf input) {
        this.cameraId = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.cameraId);
    }

    @Override
    public PacketType<ClientboundSetCameraPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_CAMERA;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetCamera(this);
    }

    public @Nullable Entity getEntity(Level level) {
        return level.getEntity(this.cameraId);
    }
}

