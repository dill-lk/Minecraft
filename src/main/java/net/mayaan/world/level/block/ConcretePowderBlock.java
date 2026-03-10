/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.block;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Vec3i;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.item.FallingBlockEntity;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.FallingBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

public class ConcretePowderBlock
extends FallingBlock {
    public static final MapCodec<ConcretePowderBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("concrete").forGetter(b -> b.concrete), ConcretePowderBlock.propertiesCodec()).apply((Applicative)i, ConcretePowderBlock::new));
    private final Block concrete;

    public MapCodec<ConcretePowderBlock> codec() {
        return CODEC;
    }

    public ConcretePowderBlock(Block concrete, BlockBehaviour.Properties properties) {
        super(properties);
        this.concrete = concrete;
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {
        if (ConcretePowderBlock.shouldSolidify(level, pos, replacedBlock)) {
            level.setBlock(pos, this.concrete.defaultBlockState(), 3);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState replacedBlock;
        BlockPos pos;
        Level level = context.getLevel();
        if (ConcretePowderBlock.shouldSolidify(level, pos = context.getClickedPos(), replacedBlock = level.getBlockState(pos))) {
            return this.concrete.defaultBlockState();
        }
        return super.getStateForPlacement(context);
    }

    private static boolean shouldSolidify(BlockGetter level, BlockPos pos, BlockState replacedBlock) {
        return ConcretePowderBlock.canSolidify(replacedBlock) || ConcretePowderBlock.touchesLiquid(level, pos);
    }

    private static boolean touchesLiquid(BlockGetter level, BlockPos pos) {
        boolean touchesLiquid = false;
        BlockPos.MutableBlockPos testPos = pos.mutable();
        for (Direction direction : Direction.values()) {
            BlockState blockState = level.getBlockState(testPos);
            if (direction == Direction.DOWN && !ConcretePowderBlock.canSolidify(blockState)) continue;
            testPos.setWithOffset((Vec3i)pos, direction);
            blockState = level.getBlockState(testPos);
            if (!ConcretePowderBlock.canSolidify(blockState) || blockState.isFaceSturdy(level, pos, direction.getOpposite())) continue;
            touchesLiquid = true;
            break;
        }
        return touchesLiquid;
    }

    private static boolean canSolidify(BlockState state) {
        return state.getFluidState().is(FluidTags.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (ConcretePowderBlock.touchesLiquid(level, pos)) {
            return this.concrete.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
        return blockState.getMapColor((BlockGetter)level, (BlockPos)pos).col;
    }
}

