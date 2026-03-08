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
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.mayaan.world.level.BaseCommandBlock;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class ServerboundSetCommandMinecartPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundSetCommandMinecartPacket> STREAM_CODEC = Packet.codec(ServerboundSetCommandMinecartPacket::write, ServerboundSetCommandMinecartPacket::new);
    private final int entity;
    private final String command;
    private final boolean trackOutput;

    public ServerboundSetCommandMinecartPacket(int entity, String command, boolean trackOutput) {
        this.entity = entity;
        this.command = command;
        this.trackOutput = trackOutput;
    }

    private ServerboundSetCommandMinecartPacket(FriendlyByteBuf input) {
        this.entity = input.readVarInt();
        this.command = input.readUtf();
        this.trackOutput = input.readBoolean();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.entity);
        output.writeUtf(this.command);
        output.writeBoolean(this.trackOutput);
    }

    @Override
    public PacketType<ServerboundSetCommandMinecartPacket> type() {
        return GamePacketTypes.SERVERBOUND_SET_COMMAND_MINECART;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleSetCommandMinecart(this);
    }

    public @Nullable BaseCommandBlock getCommandBlock(Level level) {
        Entity entity = level.getEntity(this.entity);
        if (entity instanceof MinecartCommandBlock) {
            return ((MinecartCommandBlock)entity).getCommandBlock();
        }
        return null;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isTrackOutput() {
        return this.trackOutput;
    }
}

