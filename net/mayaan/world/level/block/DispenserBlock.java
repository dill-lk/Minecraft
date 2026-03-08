/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.world.level.block;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import java.util.IdentityHashMap;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Position;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.core.dispenser.DispenseItemBehavior;
import net.mayaan.core.dispenser.EquipmentDispenseItemBehavior;
import net.mayaan.core.dispenser.ProjectileDispenseBehavior;
import net.mayaan.core.dispenser.SpawnEggItemBehavior;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.stats.Stats;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Containers;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.SpawnEggItem;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.ItemLike;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.DirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.DispenserBlockEntity;
import net.mayaan.world.level.block.entity.DropperBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.redstone.Orientation;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class DispenserBlock
extends BaseEntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<DispenserBlock> CODEC = DispenserBlock.simpleCodec(DispenserBlock::new);
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    private static final DefaultDispenseItemBehavior DEFAULT_BEHAVIOR = new DefaultDispenseItemBehavior();
    public static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY = new IdentityHashMap<Item, DispenseItemBehavior>();
    private static final int TRIGGER_DURATION = 4;

    public MapCodec<? extends DispenserBlock> codec() {
        return CODEC;
    }

    public static void registerBehavior(ItemLike item, DispenseItemBehavior behavior) {
        DISPENSER_REGISTRY.put(item.asItem(), behavior);
    }

    public static void registerProjectileBehavior(ItemLike item) {
        DISPENSER_REGISTRY.put(item.asItem(), new ProjectileDispenseBehavior(item.asItem()));
    }

    protected DispenserBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TRIGGERED, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity;
        if (!level.isClientSide() && (blockEntity = level.getBlockEntity(pos)) instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = (DispenserBlockEntity)blockEntity;
            player.openMenu(dispenser);
            player.awardStat(dispenser instanceof DropperBlockEntity ? Stats.INSPECT_DROPPER : Stats.INSPECT_DISPENSER);
        }
        return InteractionResult.SUCCESS;
    }

    protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
        DispenserBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityType.DISPENSER).orElse(null);
        if (blockEntity == null) {
            LOGGER.warn("Ignoring dispensing attempt for Dispenser without matching block entity at {}", (Object)pos);
            return;
        }
        BlockSource source = new BlockSource(level, pos, state, blockEntity);
        int slot = blockEntity.getRandomSlot(level.getRandom());
        if (slot < 0) {
            level.levelEvent(1001, pos, 0);
            level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(blockEntity.getBlockState()));
            return;
        }
        ItemStack itemStack = blockEntity.getItem(slot);
        DispenseItemBehavior behavior = this.getDispenseMethod(level, itemStack);
        if (behavior != DispenseItemBehavior.NOOP) {
            blockEntity.setItem(slot, behavior.dispense(source, itemStack));
        }
    }

    protected DispenseItemBehavior getDispenseMethod(Level level, ItemStack itemStack) {
        if (!itemStack.isItemEnabled(level.enabledFeatures())) {
            return DEFAULT_BEHAVIOR;
        }
        DispenseItemBehavior behavior = DISPENSER_REGISTRY.get(itemStack.getItem());
        if (behavior != null) {
            return behavior;
        }
        return DispenserBlock.getDefaultDispenseMethod(itemStack);
    }

    private static DispenseItemBehavior getDefaultDispenseMethod(ItemStack itemStack) {
        if (itemStack.has(DataComponents.EQUIPPABLE)) {
            return EquipmentDispenseItemBehavior.INSTANCE;
        }
        if (itemStack.getItem() instanceof SpawnEggItem && itemStack.has(DataComponents.ENTITY_DATA)) {
            return SpawnEggItemBehavior.INSTANCE;
        }
        return DEFAULT_BEHAVIOR;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        boolean shouldTrigger = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean isTriggered = state.getValue(TRIGGERED);
        if (shouldTrigger && !isTriggered) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, (BlockState)state.setValue(TRIGGERED, true), 2);
        } else if (!shouldTrigger && isTriggered) {
            level.setBlock(pos, (BlockState)state.setValue(TRIGGERED, false), 2);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.dispenseFrom(level, state, pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new DispenserBlockEntity(worldPosition, blockState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    public static Position getDispensePosition(BlockSource source) {
        return DispenserBlock.getDispensePosition(source, 0.7, Vec3.ZERO);
    }

    public static Position getDispensePosition(BlockSource source, double scale, Vec3 offset) {
        Direction direction = source.state().getValue(FACING);
        return source.center().add(scale * (double)direction.getStepX() + offset.x(), scale * (double)direction.getStepY() + offset.y(), scale * (double)direction.getStepZ() + offset.z());
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }
}

