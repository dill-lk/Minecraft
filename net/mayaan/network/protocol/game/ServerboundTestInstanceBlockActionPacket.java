/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.IntFunction;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Vec3i;
import net.mayaan.gametest.framework.GameTestInstance;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.resources.ResourceKey;
import net.mayaan.util.ByIdMap;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.TestInstanceBlockEntity;

public record ServerboundTestInstanceBlockActionPacket(BlockPos pos, Action action, TestInstanceBlockEntity.Data data) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTestInstanceBlockActionPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundTestInstanceBlockActionPacket::pos, Action.STREAM_CODEC, ServerboundTestInstanceBlockActionPacket::action, TestInstanceBlockEntity.Data.STREAM_CODEC, ServerboundTestInstanceBlockActionPacket::data, ServerboundTestInstanceBlockActionPacket::new);

    public ServerboundTestInstanceBlockActionPacket(BlockPos pos, Action action, Optional<ResourceKey<GameTestInstance>> test, Vec3i size, Rotation rotation, boolean ignoreEntities) {
        this(pos, action, new TestInstanceBlockEntity.Data(test, size, rotation, ignoreEntities, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
    }

    @Override
    public PacketType<ServerboundTestInstanceBlockActionPacket> type() {
        return GamePacketTypes.SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleTestInstanceBlockAction(this);
    }

    public static enum Action {
        INIT(0),
        QUERY(1),
        SET(2),
        RESET(3),
        SAVE(4),
        EXPORT(5),
        RUN(6);

        private static final IntFunction<Action> BY_ID;
        public static final StreamCodec<ByteBuf, Action> STREAM_CODEC;
        private final int id;

        private Action(int id) {
            this.id = id;
        }

        static {
            BY_ID = ByIdMap.continuous(e -> e.id, Action.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
            STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
        }
    }
}

