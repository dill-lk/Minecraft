/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.function.ToIntFunction;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.MultifaceBlock;
import net.mayaan.world.level.block.MultifaceSpreadeableBlock;
import net.mayaan.world.level.block.MultifaceSpreader;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

public class GlowLichenBlock
extends MultifaceSpreadeableBlock
implements BonemealableBlock {
    public static final MapCodec<GlowLichenBlock> CODEC = GlowLichenBlock.simpleCodec(GlowLichenBlock::new);
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public MapCodec<GlowLichenBlock> codec() {
        return CODEC;
    }

    public GlowLichenBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static ToIntFunction<BlockState> emission(int lightEmission) {
        return state -> MultifaceBlock.hasAnyFace(state) ? lightEmission : 0;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return Direction.stream().anyMatch(face -> this.spreader.canSpreadInAnyDirection(state, level, pos, face.getOpposite()));
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.spreader.spreadFromRandomFaceTowardRandomDirection(state, level, pos, random);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }
}

