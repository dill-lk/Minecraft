/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.DustParticleOptions;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.UniformInt;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.RedstoneTorchBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.phys.BlockHitResult;

public class RedStoneOreBlock
extends Block {
    public static final MapCodec<RedStoneOreBlock> CODEC = RedStoneOreBlock.simpleCodec(RedStoneOreBlock::new);
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public MapCodec<RedStoneOreBlock> codec() {
        return CODEC;
    }

    public RedStoneOreBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        RedStoneOreBlock.interact(state, level, pos);
        super.attack(state, level, pos, player);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
        if (!entity.isSteppingCarefully()) {
            RedStoneOreBlock.interact(onState, level, pos);
        }
        super.stepOn(level, pos, onState, entity);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            RedStoneOreBlock.spawnParticles(level, pos);
        } else {
            RedStoneOreBlock.interact(state, level, pos);
        }
        if (itemStack.getItem() instanceof BlockItem && new BlockPlaceContext(player, hand, itemStack, hitResult).canPlace()) {
            return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    private static void interact(BlockState state, Level level, BlockPos pos) {
        RedStoneOreBlock.spawnParticles(level, pos);
        if (!state.getValue(LIT).booleanValue()) {
            level.setBlock(pos, (BlockState)state.setValue(LIT, true), 3);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(LIT);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT).booleanValue()) {
            level.setBlock(pos, (BlockState)state.setValue(LIT, false), 3);
        }
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        if (dropExperience) {
            this.tryDropExperience(level, pos, tool, UniformInt.of(1, 5));
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT).booleanValue()) {
            RedStoneOreBlock.spawnParticles(level, pos);
        }
    }

    private static void spawnParticles(Level level, BlockPos pos) {
        double offset = 0.5625;
        RandomSource random = level.getRandom();
        for (Direction direction : Direction.values()) {
            BlockPos relative = pos.relative(direction);
            if (level.getBlockState(relative).isSolidRender()) continue;
            Direction.Axis axis = direction.getAxis();
            double dx = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double)direction.getStepX() : (double)random.nextFloat();
            double dy = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double)direction.getStepY() : (double)random.nextFloat();
            double dz = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double)direction.getStepZ() : (double)random.nextFloat();
            level.addParticle(DustParticleOptions.REDSTONE, (double)pos.getX() + dx, (double)pos.getY() + dy, (double)pos.getZ() + dz, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}

