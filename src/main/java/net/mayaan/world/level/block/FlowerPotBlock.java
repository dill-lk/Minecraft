/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.block;

import com.google.common.collect.Maps;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.attribute.EnvironmentAttributes;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.EyeblossomBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class FlowerPotBlock
extends Block {
    public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("potted").forGetter(b -> b.potted), FlowerPotBlock.propertiesCodec()).apply((Applicative)i, FlowerPotBlock::new));
    private static final Map<Block, Block> POTTED_BY_CONTENT = Maps.newHashMap();
    private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 6.0);
    private final Block potted;

    public MapCodec<FlowerPotBlock> codec() {
        return CODEC;
    }

    public FlowerPotBlock(Block potted, BlockBehaviour.Properties properties) {
        super(properties);
        this.potted = potted;
        POTTED_BY_CONTENT.put(potted, this);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Block block;
        Item item = itemStack.getItem();
        if (item instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)item;
            block = POTTED_BY_CONTENT.getOrDefault(blockItem.getBlock(), Blocks.AIR);
        } else {
            block = Blocks.AIR;
        }
        BlockState newContents = block.defaultBlockState();
        if (newContents.isAir()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!this.isEmpty()) {
            return InteractionResult.CONSUME;
        }
        level.setBlock(pos, newContents, 3);
        level.gameEvent((Entity)player, GameEvent.BLOCK_CHANGE, pos);
        player.awardStat(Stats.POT_FLOWER);
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.isEmpty()) {
            return InteractionResult.CONSUME;
        }
        ItemStack plant = new ItemStack(this.potted);
        if (!player.addItem(plant)) {
            player.drop(plant, false);
        }
        level.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), 3);
        level.gameEvent((Entity)player, GameEvent.BLOCK_CHANGE, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        if (this.isEmpty()) {
            return super.getCloneItemStack(level, pos, state, includeData);
        }
        return new ItemStack(this.potted);
    }

    private boolean isEmpty() {
        return this.potted == Blocks.AIR;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    public Block getPotted() {
        return this.potted;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.is(Blocks.POTTED_OPEN_EYEBLOSSOM) || state.is(Blocks.POTTED_CLOSED_EYEBLOSSOM);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean shouldBeOpen;
        boolean isOpen;
        if (this.isRandomlyTicking(state) && (isOpen = this.potted == Blocks.OPEN_EYEBLOSSOM) != (shouldBeOpen = level.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos).toBoolean(isOpen))) {
            level.setBlock(pos, this.opposite(state), 3);
            EyeblossomBlock.Type newType = EyeblossomBlock.Type.fromBoolean(isOpen).transform();
            newType.spawnTransformParticle(level, pos, random);
            level.playSound(null, pos, newType.longSwitchSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        super.randomTick(state, level, pos, random);
    }

    public BlockState opposite(BlockState state) {
        if (state.is(Blocks.POTTED_OPEN_EYEBLOSSOM)) {
            return Blocks.POTTED_CLOSED_EYEBLOSSOM.defaultBlockState();
        }
        if (state.is(Blocks.POTTED_CLOSED_EYEBLOSSOM)) {
            return Blocks.POTTED_OPEN_EYEBLOSSOM.defaultBlockState();
        }
        return state;
    }
}

