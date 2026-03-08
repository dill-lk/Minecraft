/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.core.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleType;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;

public class BlockParticleOption
implements ParticleOptions {
    private static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.withAlternative(BlockState.CODEC, BuiltInRegistries.BLOCK.byNameCodec(), Block::defaultBlockState);
    private final ParticleType<BlockParticleOption> type;
    private final BlockState state;

    public static MapCodec<BlockParticleOption> codec(ParticleType<BlockParticleOption> type) {
        return BLOCK_STATE_CODEC.xmap(state -> new BlockParticleOption(type, (BlockState)state), o -> o.state).fieldOf("block_state");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> streamCodec(ParticleType<BlockParticleOption> type) {
        return ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).map(state -> new BlockParticleOption(type, (BlockState)state), o -> o.state);
    }

    public BlockParticleOption(ParticleType<BlockParticleOption> type, BlockState state) {
        this.type = type;
        this.state = state;
    }

    public ParticleType<BlockParticleOption> getType() {
        return this.type;
    }

    public BlockState getState() {
        return this.state;
    }
}

