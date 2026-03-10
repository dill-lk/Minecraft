/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.placement.VegetationPlacements;
import net.mayaan.references.BlockIds;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.SpreadingSnowyBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class GrassBlock
extends SpreadingSnowyBlock
implements BonemealableBlock {
    public static final MapCodec<GrassBlock> CODEC = GrassBlock.simpleCodec(GrassBlock::new);

    public MapCodec<GrassBlock> codec() {
        return CODEC;
    }

    public GrassBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockIds.DIRT);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).isAir() && level.isInsideBuildHeight(pos.above());
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState grass = Blocks.SHORT_GRASS.defaultBlockState();
        Optional grassFeature = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL);
        block0: for (int j = 0; j < 128; ++j) {
            Holder<PlacedFeature> placementFeature;
            BonemealableBlock bonemealableBlock;
            BlockPos testPos = above;
            for (int i = 0; i < j / 16; ++i) {
                if (!level.getBlockState((testPos = testPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1)).below()).is(this) || level.getBlockState(testPos).isCollisionShapeFullBlock(level, testPos)) continue block0;
            }
            BlockState testState = level.getBlockState(testPos);
            if (testState.is(grass.getBlock()) && random.nextInt(10) == 0 && (bonemealableBlock = (BonemealableBlock)((Object)grass.getBlock())).isValidBonemealTarget(level, testPos, testState)) {
                bonemealableBlock.performBonemeal(level, random, testPos, testState);
            }
            if (!testState.isAir() || level.isOutsideBuildHeight(testPos)) continue;
            if (random.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> features = level.getBiome(testPos).value().getGenerationSettings().getFlowerFeatures();
                if (features.isEmpty()) continue;
                int randomFlowerFeature = random.nextInt(features.size());
                placementFeature = ((RandomPatchConfiguration)features.get(randomFlowerFeature).config()).feature();
            } else {
                if (!grassFeature.isPresent()) continue;
                placementFeature = (Holder<PlacedFeature>)grassFeature.get();
            }
            ((PlacedFeature)placementFeature.value()).place(level, level.getChunkSource().getGenerator(), random, testPos);
        }
    }

    @Override
    public BonemealableBlock.Type getType() {
        return BonemealableBlock.Type.NEIGHBOR_SPREADER;
    }
}

