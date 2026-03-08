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
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.BeaconBeamBlock;
import net.mayaan.world.level.block.entity.BeaconBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class BeaconBlock
extends BaseEntityBlock
implements BeaconBeamBlock {
    public static final MapCodec<BeaconBlock> CODEC = BeaconBlock.simpleCodec(BeaconBlock::new);

    public MapCodec<BeaconBlock> codec() {
        return CODEC;
    }

    public BeaconBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.WHITE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new BeaconBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return BeaconBlock.createTickerHelper(type, BlockEntityType.BEACON, BeaconBlockEntity::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity;
        if (!level.isClientSide() && (blockEntity = level.getBlockEntity(pos)) instanceof BeaconBlockEntity) {
            BeaconBlockEntity beacon = (BeaconBlockEntity)blockEntity;
            player.openMenu(beacon);
            player.awardStat(Stats.INTERACT_WITH_BEACON);
        }
        return InteractionResult.SUCCESS;
    }
}

