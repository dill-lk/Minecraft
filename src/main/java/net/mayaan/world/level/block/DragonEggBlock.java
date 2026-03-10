/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.FallingBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.border.WorldBorder;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class DragonEggBlock
extends FallingBlock {
    public static final MapCodec<DragonEggBlock> CODEC = DragonEggBlock.simpleCodec(DragonEggBlock::new);
    private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 16.0);

    public MapCodec<DragonEggBlock> codec() {
        return CODEC;
    }

    public DragonEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        this.teleport(state, level, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        this.teleport(state, level, pos);
    }

    private void teleport(BlockState state, Level level, BlockPos pos) {
        WorldBorder worldBorder = level.getWorldBorder();
        RandomSource random = level.getRandom();
        for (int i = 0; i < 1000; ++i) {
            BlockPos testPos = pos.offset(random.nextInt(16) - random.nextInt(16), random.nextInt(8) - random.nextInt(8), random.nextInt(16) - random.nextInt(16));
            if (!level.getBlockState(testPos).isAir() || level.getBlockState(testPos.below()).isAir() || !worldBorder.isWithinBounds(testPos) || !level.isInsideBuildHeight(testPos)) continue;
            if (level.isClientSide()) {
                for (int j = 0; j < 128; ++j) {
                    double d = random.nextDouble();
                    float xa = (random.nextFloat() - 0.5f) * 0.2f;
                    float ya = (random.nextFloat() - 0.5f) * 0.2f;
                    float za = (random.nextFloat() - 0.5f) * 0.2f;
                    double x = Mth.lerp(d, (double)testPos.getX(), (double)pos.getX()) + (random.nextDouble() - 0.5) + 0.5;
                    double y = Mth.lerp(d, (double)testPos.getY(), (double)pos.getY()) + random.nextDouble() - 0.5;
                    double z = Mth.lerp(d, (double)testPos.getZ(), (double)pos.getZ()) + (random.nextDouble() - 0.5) + 0.5;
                    level.addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
                }
            } else {
                level.setBlock(testPos, state, 2);
                level.removeBlock(pos, false);
            }
            return;
        }
    }

    @Override
    protected int getDelayAfterPlace() {
        return 5;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
        return -16777216;
    }
}

