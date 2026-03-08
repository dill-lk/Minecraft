/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMaps
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 */
package net.mayaan.network.protocol.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.mayaan.network.HashedStack;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.inventory.ContainerInput;

public record ServerboundContainerClickPacket(int containerId, int stateId, short slotNum, byte buttonNum, ContainerInput containerInput, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem) implements Packet<ServerGamePacketListener>
{
    private static final int MAX_SLOT_COUNT = 128;
    private static final StreamCodec<RegistryFriendlyByteBuf, Int2ObjectMap<HashedStack>> SLOTS_STREAM_CODEC = ByteBufCodecs.map(Int2ObjectOpenHashMap::new, ByteBufCodecs.SHORT.map(Short::intValue, Integer::shortValue), HashedStack.STREAM_CODEC, 128);
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundContainerClickPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundContainerClickPacket::containerId, ByteBufCodecs.VAR_INT, ServerboundContainerClickPacket::stateId, ByteBufCodecs.SHORT, ServerboundContainerClickPacket::slotNum, ByteBufCodecs.BYTE, ServerboundContainerClickPacket::buttonNum, ContainerInput.STREAM_CODEC, ServerboundContainerClickPacket::containerInput, SLOTS_STREAM_CODEC, ServerboundContainerClickPacket::changedSlots, HashedStack.STREAM_CODEC, ServerboundContainerClickPacket::carriedItem, ServerboundContainerClickPacket::new);

    public ServerboundContainerClickPacket {
        changedSlots = Int2ObjectMaps.unmodifiable(changedSlots);
    }

    @Override
    public PacketType<ServerboundContainerClickPacket> type() {
        return GamePacketTypes.SERVERBOUND_CONTAINER_CLICK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleContainerClick(this);
    }
}

