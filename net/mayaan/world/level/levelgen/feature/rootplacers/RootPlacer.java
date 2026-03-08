/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.Products$P3
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Mu
 */
package net.mayaan.world.level.levelgen.feature.rootplacers;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.IntProvider;
import net.mayaan.world.level.LevelSimulatedReader;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.levelgen.feature.TreeFeature;
import net.mayaan.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.mayaan.world.level.levelgen.feature.rootplacers.AboveRootPlacement;
import net.mayaan.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public abstract class RootPlacer {
    public static final Codec<RootPlacer> CODEC = BuiltInRegistries.ROOT_PLACER_TYPE.byNameCodec().dispatch(RootPlacer::type, RootPlacerType::codec);
    protected final IntProvider trunkOffsetY;
    protected final BlockStateProvider rootProvider;
    protected final Optional<AboveRootPlacement> aboveRootPlacement;

    protected static <P extends RootPlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, BlockStateProvider, Optional<AboveRootPlacement>> rootPlacerParts(RecordCodecBuilder.Instance<P> instance) {
        return instance.group((App)IntProvider.CODEC.fieldOf("trunk_offset_y").forGetter(c -> c.trunkOffsetY), (App)BlockStateProvider.CODEC.fieldOf("root_provider").forGetter(c -> c.rootProvider), (App)AboveRootPlacement.CODEC.optionalFieldOf("above_root_placement").forGetter(c -> c.aboveRootPlacement));
    }

    public RootPlacer(IntProvider trunkOffsetY, BlockStateProvider rootProvider, Optional<AboveRootPlacement> aboveRootPlacement) {
        this.trunkOffsetY = trunkOffsetY;
        this.rootProvider = rootProvider;
        this.aboveRootPlacement = aboveRootPlacement;
    }

    protected abstract RootPlacerType<?> type();

    public abstract boolean placeRoots(WorldGenLevel var1, BiConsumer<BlockPos, BlockState> var2, RandomSource var3, BlockPos var4, BlockPos var5, TreeConfiguration var6);

    protected boolean canPlaceRoot(LevelSimulatedReader level, BlockPos pos) {
        return TreeFeature.validTreePos(level, pos);
    }

    protected void placeRoot(WorldGenLevel level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
        if (!this.canPlaceRoot(level, pos)) {
            return;
        }
        rootSetter.accept(pos, this.getPotentiallyWaterloggedState(level, pos, this.rootProvider.getState(level, random, pos)));
        if (this.aboveRootPlacement.isPresent()) {
            AboveRootPlacement abovePlacement = this.aboveRootPlacement.get();
            BlockPos above = pos.above();
            if (random.nextFloat() < abovePlacement.aboveRootPlacementChance() && level.isStateAtPosition(above, BlockBehaviour.BlockStateBase::isAir)) {
                rootSetter.accept(above, this.getPotentiallyWaterloggedState(level, above, abovePlacement.aboveRootProvider().getState(level, random, above)));
            }
        }
    }

    protected BlockState getPotentiallyWaterloggedState(LevelSimulatedReader level, BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            boolean waterlogged = level.isFluidAtPosition(pos, s -> s.is(FluidTags.WATER));
            return (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
        }
        return state;
    }

    public BlockPos getTrunkOrigin(BlockPos origin, RandomSource random) {
        return origin.above(this.trunkOffsetY.sample(random));
    }
}

