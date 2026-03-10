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
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class MushroomBlock
extends VegetationBlock
implements BonemealableBlock {
    public static final MapCodec<MushroomBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(b -> b.feature), MushroomBlock.propertiesCodec()).apply((Applicative)i, MushroomBlock::new));
    private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 6.0);
    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    public MapCodec<MushroomBlock> codec() {
        return CODEC;
    }

    public MushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
        super(properties);
        this.feature = feature;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(25) == 0) {
            int max = 5;
            int r = 4;
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (!level.getBlockState(blockPos).is(this) || --max > 0) continue;
                return;
            }
            BlockPos offset = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            for (int i = 0; i < 4; ++i) {
                if (level.isEmptyBlock(offset) && state.canSurvive(level, offset)) {
                    pos = offset;
                }
                offset = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            if (level.isEmptyBlock(offset) && state.canSurvive(level, offset)) {
                level.setBlock(offset, state, 2);
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isSolidRender();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        if (below.is(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT)) {
            return true;
        }
        return level.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(below, level, belowPos);
    }

    public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        Optional feature = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
        if (feature.isEmpty()) {
            return false;
        }
        level.removeBlock(pos, false);
        if (((ConfiguredFeature)((Holder)feature.get()).value()).place(level, level.getChunkSource().getGenerator(), random, pos)) {
            return true;
        }
        level.setBlock(pos, state, 3);
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Optional featureHolder = serverLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
            if (featureHolder.isPresent()) {
                Object FC;
                ConfiguredFeature configuredFeature = (ConfiguredFeature)((Holder)featureHolder.get()).value();
                if (configuredFeature.feature() instanceof AbstractHugeMushroomFeature && (FC = configuredFeature.config()) instanceof HugeMushroomFeatureConfiguration) {
                    HugeMushroomFeatureConfiguration config = (HugeMushroomFeatureConfiguration)FC;
                    int minHeight = 4 + config.foliageRadius();
                    return level.isInsideBuildHeight(pos.above(minHeight));
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return (double)random.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.growMushroom(level, pos, state, random);
    }
}

