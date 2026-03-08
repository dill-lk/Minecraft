/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.cauldron.CauldronInteraction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.PointedDripstoneBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.BooleanOp;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public abstract class AbstractCauldronBlock
extends Block {
    protected static final int FLOOR_LEVEL = 4;
    private static final VoxelShape SHAPE_INSIDE = Block.column(12.0, 4.0, 16.0);
    protected static final VoxelShape SHAPE = Util.make(() -> {
        int legWidth = 4;
        int legHeight = 3;
        int legThickness = 2;
        return Shapes.join(Shapes.block(), Shapes.or(Block.column(16.0, 8.0, 0.0, 3.0), Block.column(8.0, 16.0, 0.0, 3.0), Block.column(12.0, 0.0, 3.0), SHAPE_INSIDE), BooleanOp.ONLY_FIRST);
    });
    protected final CauldronInteraction.Dispatcher interactions;

    protected abstract MapCodec<? extends AbstractCauldronBlock> codec();

    public AbstractCauldronBlock(BlockBehaviour.Properties properties, CauldronInteraction.Dispatcher interactions) {
        super(properties);
        this.interactions = interactions;
    }

    protected double getContentHeight(BlockState state) {
        return 0.0;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CauldronInteraction behavior = this.interactions.get(itemStack);
        return behavior.interact(state, level, pos, player, hand, itemStack);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE_INSIDE;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public abstract boolean isFull(BlockState var1);

    @Override
    protected void tick(BlockState cauldronState, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos stalactitePos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
        if (stalactitePos == null) {
            return;
        }
        Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(level, stalactitePos);
        if (fluid != Fluids.EMPTY && this.canReceiveStalactiteDrip(fluid)) {
            this.receiveStalactiteDrip(cauldronState, level, pos, fluid);
        }
    }

    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return false;
    }

    protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
    }
}

