/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.entity.Entity;

public class ServerboundPlayerCommandPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerCommandPacket> STREAM_CODEC = Packet.codec(ServerboundPlayerCommandPacket::write, ServerboundPlayerCommandPacket::new);
    private final int id;
    private final Action action;
    private final int data;

    public ServerboundPlayerCommandPacket(Entity entity, Action action) {
        this(entity, action, 0);
    }

    public ServerboundPlayerCommandPacket(Entity entity, Action action, int data) {
        this.id = entity.getId();
        this.action = action;
        this.data = data;
    }

    private ServerboundPlayerCommandPacket(FriendlyByteBuf input) {
        this.id = input.readVarInt();
        this.action = input.readEnum(Action.class);
        this.data = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.id);
        output.writeEnum(this.action);
        output.writeVarInt(this.data);
    }

    @Override
    public PacketType<ServerboundPlayerCommandPacket> type() {
        return GamePacketTypes.SERVERBOUND_PLAYER_COMMAND;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handlePlayerCommand(this);
    }

    public int getId() {
        return this.id;
    }

    public Action getAction() {
        return this.action;
    }

    public int getData() {
        return this.data;
    }

    public static enum Action {
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_RIDING_JUMP,
        STOP_RIDING_JUMP,
        OPEN_INVENTORY,
        START_FALL_FLYING;

    }
}

