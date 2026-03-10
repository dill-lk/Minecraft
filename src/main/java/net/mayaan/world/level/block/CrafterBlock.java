/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  java.lang.MatchException
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.FrontAndTop;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Container;
import net.mayaan.world.Containers;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.item.crafting.CraftingInput;
import net.mayaan.world.item.crafting.CraftingRecipe;
import net.mayaan.world.item.crafting.RecipeCache;
import net.mayaan.world.item.crafting.RecipeHolder;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.CrafterBlockEntity;
import net.mayaan.world.level.block.entity.HopperBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.redstone.Orientation;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CrafterBlock
extends BaseEntityBlock {
    public static final MapCodec<CrafterBlock> CODEC = CrafterBlock.simpleCodec(CrafterBlock::new);
    public static final BooleanProperty CRAFTING = BlockStateProperties.CRAFTING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
    private static final int MAX_CRAFTING_TICKS = 6;
    private static final int CRAFTING_TICK_DELAY = 4;
    private static final RecipeCache RECIPE_CACHE = new RecipeCache(10);
    private static final int CRAFTER_ADVANCEMENT_DIAMETER = 17;

    public CrafterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(ORIENTATION, FrontAndTop.NORTH_UP)).setValue(TRIGGERED, false)).setValue(CRAFTING, false));
    }

    protected MapCodec<CrafterBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrafterBlockEntity) {
            CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
            return crafterBlockEntity.getRedstoneSignal();
        }
        return 0;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        boolean shouldTrigger = level.hasNeighborSignal(pos);
        boolean isTriggered = state.getValue(TRIGGERED);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (shouldTrigger && !isTriggered) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, (BlockState)state.setValue(TRIGGERED, true), 2);
            this.setBlockEntityTriggered(blockEntity, true);
        } else if (!shouldTrigger && isTriggered) {
            level.setBlock(pos, (BlockState)((BlockState)state.setValue(TRIGGERED, false)).setValue(CRAFTING, false), 2);
            this.setBlockEntityTriggered(blockEntity, false);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.dispenseFrom(state, level, pos);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : CrafterBlock.createTickerHelper(type, BlockEntityType.CRAFTER, CrafterBlockEntity::serverTick);
    }

    private void setBlockEntityTriggered(@Nullable BlockEntity blockEntity, boolean triggered) {
        if (blockEntity instanceof CrafterBlockEntity) {
            CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
            crafterBlockEntity.setTriggered(triggered);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        CrafterBlockEntity crafterBlockEntity = new CrafterBlockEntity(worldPosition, blockState);
        crafterBlockEntity.setTriggered(blockState.hasProperty(TRIGGERED) && blockState.getValue(TRIGGERED) != false);
        return crafterBlockEntity;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction nearestLookingDirection = context.getNearestLookingDirection().getOpposite();
        Direction verticalDirection = switch (nearestLookingDirection) {
            default -> throw new MatchException(null, null);
            case Direction.DOWN -> context.getHorizontalDirection().getOpposite();
            case Direction.UP -> context.getHorizontalDirection();
            case Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> Direction.UP;
        };
        return (BlockState)((BlockState)this.defaultBlockState().setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(nearestLookingDirection, verticalDirection))).setValue(TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        if (state.getValue(TRIGGERED).booleanValue()) {
            level.scheduleTick(pos, this, 4);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity;
        if (!level.isClientSide() && (blockEntity = level.getBlockEntity(pos)) instanceof CrafterBlockEntity) {
            CrafterBlockEntity crafter = (CrafterBlockEntity)blockEntity;
            player.openMenu(crafter);
        }
        return InteractionResult.SUCCESS;
    }

    protected void dispenseFrom(BlockState state, ServerLevel level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof CrafterBlockEntity)) {
            return;
        }
        CrafterBlockEntity blockEntity2 = (CrafterBlockEntity)blockEntity;
        CraftingInput craftInput = blockEntity2.asCraftInput();
        Optional<RecipeHolder<CraftingRecipe>> recipe = CrafterBlock.getPotentialResults(level, craftInput);
        if (recipe.isEmpty()) {
            level.levelEvent(1050, pos, 0);
            return;
        }
        RecipeHolder<CraftingRecipe> pickedRecipe = recipe.get();
        ItemStack results = pickedRecipe.value().assemble(craftInput);
        if (results.isEmpty()) {
            level.levelEvent(1050, pos, 0);
            return;
        }
        blockEntity2.setCraftingTicksRemaining(6);
        level.setBlock(pos, (BlockState)state.setValue(CRAFTING, true), 2);
        results.onCraftedBySystem(level);
        this.dispenseItem(level, pos, blockEntity2, results, state, pickedRecipe);
        for (ItemStack remainingItem : pickedRecipe.value().getRemainingItems(craftInput)) {
            if (remainingItem.isEmpty()) continue;
            this.dispenseItem(level, pos, blockEntity2, remainingItem, state, pickedRecipe);
        }
        blockEntity2.getItems().forEach(it -> {
            if (it.isEmpty()) {
                return;
            }
            it.shrink(1);
        });
        blockEntity2.setChanged();
    }

    public static Optional<RecipeHolder<CraftingRecipe>> getPotentialResults(ServerLevel level, CraftingInput input) {
        return RECIPE_CACHE.get(level, input);
    }

    private void dispenseItem(ServerLevel level, BlockPos pos, CrafterBlockEntity blockEntity, ItemStack results, BlockState blockState, RecipeHolder<?> recipe) {
        Direction direction = blockState.getValue(ORIENTATION).front();
        Container into = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
        ItemStack remaining = results.copy();
        if (into != null && (into instanceof CrafterBlockEntity || results.getCount() > into.getMaxStackSize(results))) {
            ItemStack copy;
            ItemStack itemStack;
            while (!remaining.isEmpty() && (itemStack = HopperBlockEntity.addItem(blockEntity, into, copy = remaining.copyWithCount(1), direction.getOpposite())).isEmpty()) {
                remaining.shrink(1);
            }
        } else if (into != null) {
            int oldSize;
            while (!remaining.isEmpty() && (oldSize = remaining.getCount()) != (remaining = HopperBlockEntity.addItem(blockEntity, into, remaining, direction.getOpposite())).getCount()) {
            }
        }
        if (!remaining.isEmpty()) {
            Vec3 centerPos = Vec3.atCenterOf(pos);
            Vec3 itemSpawnOffset = centerPos.relative(direction, 0.7);
            DefaultDispenseItemBehavior.spawnItem(level, remaining, 6, direction, itemSpawnOffset);
            for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(centerPos, 17.0, 17.0, 17.0))) {
                CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.trigger(player, recipe.id(), blockEntity.getItems());
            }
            level.levelEvent(1049, pos, 0);
            level.levelEvent(2010, pos, direction.get3DDataValue());
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(ORIENTATION, rotation.rotation().rotate(state.getValue(ORIENTATION)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return (BlockState)state.setValue(ORIENTATION, mirror.rotation().rotate(state.getValue(ORIENTATION)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION, TRIGGERED, CRAFTING);
    }
}

