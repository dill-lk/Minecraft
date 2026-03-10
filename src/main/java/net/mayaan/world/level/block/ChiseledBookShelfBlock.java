/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.tags.ItemTags;
import net.mayaan.world.Containers;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.HorizontalDirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.SelectableSlotContainer;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.block.state.properties.Property;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class ChiseledBookShelfBlock
extends BaseEntityBlock
implements SelectableSlotContainer {
    public static final MapCodec<ChiseledBookShelfBlock> CODEC = ChiseledBookShelfBlock.simpleCodec(ChiseledBookShelfBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty SLOT_0_OCCUPIED = BlockStateProperties.SLOT_0_OCCUPIED;
    public static final BooleanProperty SLOT_1_OCCUPIED = BlockStateProperties.SLOT_1_OCCUPIED;
    public static final BooleanProperty SLOT_2_OCCUPIED = BlockStateProperties.SLOT_2_OCCUPIED;
    public static final BooleanProperty SLOT_3_OCCUPIED = BlockStateProperties.SLOT_3_OCCUPIED;
    public static final BooleanProperty SLOT_4_OCCUPIED = BlockStateProperties.SLOT_4_OCCUPIED;
    public static final BooleanProperty SLOT_5_OCCUPIED = BlockStateProperties.SLOT_5_OCCUPIED;
    private static final int MAX_BOOKS_IN_STORAGE = 6;
    private static final int BOOKS_PER_ROW = 3;
    public static final List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES = List.of(SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED, SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED);

    public MapCodec<ChiseledBookShelfBlock> codec() {
        return CODEC;
    }

    @Override
    public int getRows() {
        return 2;
    }

    @Override
    public int getColumns() {
        return 3;
    }

    public ChiseledBookShelfBlock(BlockBehaviour.Properties properties) {
        super(properties);
        BlockState defaultState = (BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH);
        for (BooleanProperty property : SLOT_OCCUPIED_PROPERTIES) {
            defaultState = (BlockState)defaultState.setValue(property, false);
        }
        this.registerDefaultState(defaultState);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChiseledBookShelfBlockEntity)) {
            return InteractionResult.PASS;
        }
        ChiseledBookShelfBlockEntity bookshelfBlock = (ChiseledBookShelfBlockEntity)blockEntity;
        if (!itemStack.is(ItemTags.BOOKSHELF_BOOKS)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        OptionalInt hitSlot = this.getHitSlot(hitResult, state.getValue(FACING));
        if (hitSlot.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (((Boolean)state.getValue(SLOT_OCCUPIED_PROPERTIES.get(hitSlot.getAsInt()))).booleanValue()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        ChiseledBookShelfBlock.addBook(level, pos, player, bookshelfBlock, itemStack, hitSlot.getAsInt());
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChiseledBookShelfBlockEntity)) {
            return InteractionResult.PASS;
        }
        ChiseledBookShelfBlockEntity bookshelfBlock = (ChiseledBookShelfBlockEntity)blockEntity;
        OptionalInt hitSlot = this.getHitSlot(hitResult, state.getValue(FACING));
        if (hitSlot.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!((Boolean)state.getValue(SLOT_OCCUPIED_PROPERTIES.get(hitSlot.getAsInt()))).booleanValue()) {
            return InteractionResult.CONSUME;
        }
        ChiseledBookShelfBlock.removeBook(level, pos, player, bookshelfBlock, hitSlot.getAsInt());
        return InteractionResult.SUCCESS;
    }

    private static void addBook(Level level, BlockPos pos, Player player, ChiseledBookShelfBlockEntity bookshelfBlock, ItemStack itemStack, int slot) {
        if (level.isClientSide()) {
            return;
        }
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        SoundEvent soundEvent = itemStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
        bookshelfBlock.setItem(slot, itemStack.consumeAndReturn(1, player));
        level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    private static void removeBook(Level level, BlockPos pos, Player player, ChiseledBookShelfBlockEntity bookshelfBlock, int slot) {
        if (level.isClientSide()) {
            return;
        }
        ItemStack retrievedBook = bookshelfBlock.removeItem(slot, 1);
        SoundEvent soundEvent = retrievedBook.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
        level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
        if (!player.getInventory().add(retrievedBook)) {
            player.drop(retrievedBook, false);
        }
        level.gameEvent((Entity)player, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ChiseledBookShelfBlockEntity(worldPosition, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        StateDefinition.Builder<Block, BlockState> builder2 = builder;
        Objects.requireNonNull(builder2);
        StateDefinition.Builder<Block, BlockState> builder3 = builder2;
        SLOT_OCCUPIED_PROPERTIES.forEach(xva$0 -> builder3.add((Property<?>)xva$0));
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (level.isClientSide()) {
            return 0;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ChiseledBookShelfBlockEntity) {
            ChiseledBookShelfBlockEntity blockEntity2 = (ChiseledBookShelfBlockEntity)blockEntity;
            return blockEntity2.getLastInteractedSlot() + 1;
        }
        return 0;
    }
}

