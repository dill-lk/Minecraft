/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.util.RandomSource;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.AbstractFurnaceBlock;
import net.mayaan.world.level.block.entity.BlastFurnaceBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BlastFurnaceBlock
extends AbstractFurnaceBlock {
    public static final MapCodec<BlastFurnaceBlock> CODEC = BlastFurnaceBlock.simpleCodec(BlastFurnaceBlock::new);

    public MapCodec<BlastFurnaceBlock> codec() {
        return CODEC;
    }

    protected BlastFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new BlastFurnaceBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return BlastFurnaceBlock.createFurnaceTicker(level, type, BlockEntityType.BLAST_FURNACE);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlastFurnaceBlockEntity) {
            player.openMenu((MenuProvider)((Object)blockEntity));
            player.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT).booleanValue()) {
            return;
        }
        double x = (double)pos.getX() + 0.5;
        double y = pos.getY();
        double z = (double)pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        Direction direction = (Direction)state.getValue(FACING);
        Direction.Axis axis = direction.getAxis();
        double r = 0.52;
        double ss = random.nextDouble() * 0.6 - 0.3;
        double dx = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : ss;
        double dy = random.nextDouble() * 9.0 / 16.0;
        double dz = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : ss;
        level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
    }
}

