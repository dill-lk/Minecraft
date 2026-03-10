/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Holder;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BiomeTags;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.ParticleUtils;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.block.BaseCoralWallFanBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

public class BoneMealItem
extends Item {
    public static final int GRASS_SPREAD_WIDTH = 3;
    public static final int GRASS_SPREAD_HEIGHT = 1;
    public static final int GRASS_COUNT_MULTIPLIER = 3;

    public BoneMealItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos relative = pos.relative(context.getClickedFace());
        ItemStack boneMealStack = context.getItemInHand();
        if (BoneMealItem.growCrop(boneMealStack, level, pos)) {
            if (!level.isClientSide()) {
                boneMealStack.causeUseVibration(context.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
                level.levelEvent(1505, pos, 15);
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.PASS;
        }
        BlockState clickedState = level.getBlockState(pos);
        boolean solidBlockFace = clickedState.isFaceSturdy(level, pos, context.getClickedFace());
        if (solidBlockFace && BoneMealItem.growWaterPlant(boneMealStack, level, relative, context.getClickedFace())) {
            if (!level.isClientSide()) {
                boneMealStack.causeUseVibration(context.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
                level.levelEvent(1505, relative, 15);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static boolean growCrop(ItemStack itemStack, Level level, BlockPos pos) {
        BonemealableBlock block;
        BlockState state = level.getBlockState(pos);
        Block block2 = state.getBlock();
        if (block2 instanceof BonemealableBlock && (block = (BonemealableBlock)((Object)block2)).isValidBonemealTarget(level, pos, state)) {
            if (level instanceof ServerLevel) {
                if (block.isBonemealSuccess(level, level.getRandom(), pos, state)) {
                    block.performBonemeal((ServerLevel)level, level.getRandom(), pos, state);
                }
                itemStack.shrink(1);
            }
            return true;
        }
        return false;
    }

    public static boolean growWaterPlant(ItemStack itemStack, Level level, BlockPos pos, @Nullable Direction clickedFace) {
        if (!level.getBlockState(pos).is(Blocks.WATER) || !level.getFluidState(pos).isFull()) {
            return false;
        }
        if (!(level instanceof ServerLevel)) {
            return true;
        }
        RandomSource random = level.getRandom();
        block0: for (int j = 0; j < 128; ++j) {
            BlockPos testPos = pos;
            BlockState stateToGrow = Blocks.SEAGRASS.defaultBlockState();
            for (int i = 0; i < j / 16; ++i) {
                if (level.getBlockState(testPos = testPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1)).isCollisionShapeFullBlock(level, testPos)) continue block0;
            }
            Holder<Biome> testBiome = level.getBiome(testPos);
            if (testBiome.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                if (j == 0 && clickedFace != null && clickedFace.getAxis().isHorizontal()) {
                    stateToGrow = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, level.getRandom()).map(h -> ((Block)h.value()).defaultBlockState()).orElse(stateToGrow);
                    if (stateToGrow.hasProperty(BaseCoralWallFanBlock.FACING)) {
                        stateToGrow = (BlockState)stateToGrow.setValue(BaseCoralWallFanBlock.FACING, clickedFace);
                    }
                } else if (random.nextInt(4) == 0) {
                    stateToGrow = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.UNDERWATER_BONEMEALS, level.getRandom()).map(h -> ((Block)h.value()).defaultBlockState()).orElse(stateToGrow);
                }
            }
            if (stateToGrow.is(BlockTags.WALL_CORALS, s -> s.hasProperty(BaseCoralWallFanBlock.FACING))) {
                for (int d = 0; !stateToGrow.canSurvive(level, testPos) && d < 4; ++d) {
                    stateToGrow = (BlockState)stateToGrow.setValue(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                }
            }
            if (!stateToGrow.canSurvive(level, testPos)) continue;
            BlockState testState = level.getBlockState(testPos);
            if (testState.is(Blocks.WATER) && level.getFluidState(testPos).isFull()) {
                level.setBlock(testPos, stateToGrow, 3);
                continue;
            }
            if (!testState.is(Blocks.SEAGRASS) || !((BonemealableBlock)((Object)Blocks.SEAGRASS)).isValidBonemealTarget(level, testPos, testState) || random.nextInt(10) != 0) continue;
            ((BonemealableBlock)((Object)Blocks.SEAGRASS)).performBonemeal((ServerLevel)level, random, testPos, testState);
        }
        itemStack.shrink(1);
        return true;
    }

    public static void addGrowthParticles(LevelAccessor level, BlockPos pos, int count) {
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof BonemealableBlock) {
            BonemealableBlock bonemealableBlock = (BonemealableBlock)((Object)block);
            BlockPos particlePos = bonemealableBlock.getParticlePos(pos);
            switch (bonemealableBlock.getType()) {
                case NEIGHBOR_SPREADER: {
                    ParticleUtils.spawnParticles(level, particlePos, count * 3, 3.0, 1.0, false, ParticleTypes.HAPPY_VILLAGER);
                    break;
                }
                case GROWER: {
                    ParticleUtils.spawnParticleInBlock(level, particlePos, count, ParticleTypes.HAPPY_VILLAGER);
                }
            }
        } else if (blockState.is(Blocks.WATER)) {
            ParticleUtils.spawnParticles(level, pos, count * 3, 3.0, 1.0, false, ParticleTypes.HAPPY_VILLAGER);
        }
    }
}

