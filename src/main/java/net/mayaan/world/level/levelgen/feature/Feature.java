/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Registry;
import net.mayaan.core.Vec3i;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.tags.TagKey;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.LevelWriter;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.feature.BambooFeature;
import net.mayaan.world.level.levelgen.feature.BasaltColumnsFeature;
import net.mayaan.world.level.levelgen.feature.BasaltPillarFeature;
import net.mayaan.world.level.levelgen.feature.BlockBlobFeature;
import net.mayaan.world.level.levelgen.feature.BlockColumnFeature;
import net.mayaan.world.level.levelgen.feature.BlockPileFeature;
import net.mayaan.world.level.levelgen.feature.BlueIceFeature;
import net.mayaan.world.level.levelgen.feature.BonusChestFeature;
import net.mayaan.world.level.levelgen.feature.ChorusPlantFeature;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.CoralClawFeature;
import net.mayaan.world.level.levelgen.feature.CoralMushroomFeature;
import net.mayaan.world.level.levelgen.feature.CoralTreeFeature;
import net.mayaan.world.level.levelgen.feature.DeltaFeature;
import net.mayaan.world.level.levelgen.feature.DesertWellFeature;
import net.mayaan.world.level.levelgen.feature.DiskFeature;
import net.mayaan.world.level.levelgen.feature.DripstoneClusterFeature;
import net.mayaan.world.level.levelgen.feature.EndGatewayFeature;
import net.mayaan.world.level.levelgen.feature.EndIslandFeature;
import net.mayaan.world.level.levelgen.feature.EndPlatformFeature;
import net.mayaan.world.level.levelgen.feature.EndSpikeFeature;
import net.mayaan.world.level.levelgen.feature.FallenTreeFeature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.FillLayerFeature;
import net.mayaan.world.level.levelgen.feature.FossilFeature;
import net.mayaan.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.GeodeFeature;
import net.mayaan.world.level.levelgen.feature.GlowstoneFeature;
import net.mayaan.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.mayaan.world.level.levelgen.feature.HugeFungusConfiguration;
import net.mayaan.world.level.levelgen.feature.HugeFungusFeature;
import net.mayaan.world.level.levelgen.feature.HugeRedMushroomFeature;
import net.mayaan.world.level.levelgen.feature.IcebergFeature;
import net.mayaan.world.level.levelgen.feature.KelpFeature;
import net.mayaan.world.level.levelgen.feature.LakeFeature;
import net.mayaan.world.level.levelgen.feature.LargeDripstoneFeature;
import net.mayaan.world.level.levelgen.feature.MonsterRoomFeature;
import net.mayaan.world.level.levelgen.feature.MultifaceGrowthFeature;
import net.mayaan.world.level.levelgen.feature.NetherForestVegetationFeature;
import net.mayaan.world.level.levelgen.feature.NoOpFeature;
import net.mayaan.world.level.levelgen.feature.OreFeature;
import net.mayaan.world.level.levelgen.feature.PointedDripstoneFeature;
import net.mayaan.world.level.levelgen.feature.RandomBooleanSelectorFeature;
import net.mayaan.world.level.levelgen.feature.RandomPatchFeature;
import net.mayaan.world.level.levelgen.feature.RandomSelectorFeature;
import net.mayaan.world.level.levelgen.feature.ReplaceBlobsFeature;
import net.mayaan.world.level.levelgen.feature.ReplaceBlockFeature;
import net.mayaan.world.level.levelgen.feature.RootSystemFeature;
import net.mayaan.world.level.levelgen.feature.ScatteredOreFeature;
import net.mayaan.world.level.levelgen.feature.SculkPatchFeature;
import net.mayaan.world.level.levelgen.feature.SeaPickleFeature;
import net.mayaan.world.level.levelgen.feature.SeagrassFeature;
import net.mayaan.world.level.levelgen.feature.SimpleBlockFeature;
import net.mayaan.world.level.levelgen.feature.SimpleRandomSelectorFeature;
import net.mayaan.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.mayaan.world.level.levelgen.feature.SpikeFeature;
import net.mayaan.world.level.levelgen.feature.SpringFeature;
import net.mayaan.world.level.levelgen.feature.TreeFeature;
import net.mayaan.world.level.levelgen.feature.TwistingVinesFeature;
import net.mayaan.world.level.levelgen.feature.UnderwaterMagmaFeature;
import net.mayaan.world.level.levelgen.feature.VegetationPatchFeature;
import net.mayaan.world.level.levelgen.feature.VinesFeature;
import net.mayaan.world.level.levelgen.feature.VoidStartPlatformFeature;
import net.mayaan.world.level.levelgen.feature.WaterloggedVegetationPatchFeature;
import net.mayaan.world.level.levelgen.feature.WeepingVinesFeature;
import net.mayaan.world.level.levelgen.feature.configurations.BlockBlobConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.CountConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.EndSpikeConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.LayerConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.OreConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.RootSystemConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.SpikeConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.TwistingVinesConfig;
import net.mayaan.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;

public abstract class Feature<FC extends FeatureConfiguration> {
    public static final Feature<NoneFeatureConfiguration> NO_OP = Feature.register("no_op", new NoOpFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<TreeConfiguration> TREE = Feature.register("tree", new TreeFeature(TreeConfiguration.CODEC));
    public static final Feature<FallenTreeConfiguration> FALLEN_TREE = Feature.register("fallen_tree", new FallenTreeFeature(FallenTreeConfiguration.CODEC));
    public static final Feature<RandomPatchConfiguration> FLOWER = Feature.register("flower", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final Feature<RandomPatchConfiguration> NO_BONEMEAL_FLOWER = Feature.register("no_bonemeal_flower", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final Feature<RandomPatchConfiguration> RANDOM_PATCH = Feature.register("random_patch", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final Feature<BlockPileConfiguration> BLOCK_PILE = Feature.register("block_pile", new BlockPileFeature(BlockPileConfiguration.CODEC));
    public static final Feature<SpringConfiguration> SPRING = Feature.register("spring_feature", new SpringFeature(SpringConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> CHORUS_PLANT = Feature.register("chorus_plant", new ChorusPlantFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<ReplaceBlockConfiguration> REPLACE_SINGLE_BLOCK = Feature.register("replace_single_block", new ReplaceBlockFeature(ReplaceBlockConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> VOID_START_PLATFORM = Feature.register("void_start_platform", new VoidStartPlatformFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> DESERT_WELL = Feature.register("desert_well", new DesertWellFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<FossilFeatureConfiguration> FOSSIL = Feature.register("fossil", new FossilFeature(FossilFeatureConfiguration.CODEC));
    public static final Feature<HugeMushroomFeatureConfiguration> HUGE_RED_MUSHROOM = Feature.register("huge_red_mushroom", new HugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Feature<HugeMushroomFeatureConfiguration> HUGE_BROWN_MUSHROOM = Feature.register("huge_brown_mushroom", new HugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Feature<SpikeConfiguration> SPIKE = Feature.register("spike", new SpikeFeature(SpikeConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> GLOWSTONE_BLOB = Feature.register("glowstone_blob", new GlowstoneFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> FREEZE_TOP_LAYER = Feature.register("freeze_top_layer", new SnowAndFreezeFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> VINES = Feature.register("vines", new VinesFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<BlockColumnConfiguration> BLOCK_COLUMN = Feature.register("block_column", new BlockColumnFeature(BlockColumnConfiguration.CODEC));
    public static final Feature<VegetationPatchConfiguration> VEGETATION_PATCH = Feature.register("vegetation_patch", new VegetationPatchFeature(VegetationPatchConfiguration.CODEC));
    public static final Feature<VegetationPatchConfiguration> WATERLOGGED_VEGETATION_PATCH = Feature.register("waterlogged_vegetation_patch", new WaterloggedVegetationPatchFeature(VegetationPatchConfiguration.CODEC));
    public static final Feature<RootSystemConfiguration> ROOT_SYSTEM = Feature.register("root_system", new RootSystemFeature(RootSystemConfiguration.CODEC));
    public static final Feature<MultifaceGrowthConfiguration> MULTIFACE_GROWTH = Feature.register("multiface_growth", new MultifaceGrowthFeature(MultifaceGrowthConfiguration.CODEC));
    public static final Feature<UnderwaterMagmaConfiguration> UNDERWATER_MAGMA = Feature.register("underwater_magma", new UnderwaterMagmaFeature(UnderwaterMagmaConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> MONSTER_ROOM = Feature.register("monster_room", new MonsterRoomFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> BLUE_ICE = Feature.register("blue_ice", new BlueIceFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<BlockStateConfiguration> ICEBERG = Feature.register("iceberg", new IcebergFeature(BlockStateConfiguration.CODEC));
    public static final Feature<BlockBlobConfiguration> BLOCK_BLOB = Feature.register("block_blob", new BlockBlobFeature(BlockBlobConfiguration.CODEC));
    public static final Feature<DiskConfiguration> DISK = Feature.register("disk", new DiskFeature(DiskConfiguration.CODEC));
    public static final Feature<LakeFeature.Configuration> LAKE = Feature.register("lake", new LakeFeature(LakeFeature.Configuration.CODEC));
    public static final Feature<OreConfiguration> ORE = Feature.register("ore", new OreFeature(OreConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> END_PLATFORM = Feature.register("end_platform", new EndPlatformFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<EndSpikeConfiguration> END_SPIKE = Feature.register("end_spike", new EndSpikeFeature(EndSpikeConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> END_ISLAND = Feature.register("end_island", new EndIslandFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<EndGatewayConfiguration> END_GATEWAY = Feature.register("end_gateway", new EndGatewayFeature(EndGatewayConfiguration.CODEC));
    public static final SeagrassFeature SEAGRASS = Feature.register("seagrass", new SeagrassFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> KELP = Feature.register("kelp", new KelpFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> CORAL_TREE = Feature.register("coral_tree", new CoralTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> CORAL_MUSHROOM = Feature.register("coral_mushroom", new CoralMushroomFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> CORAL_CLAW = Feature.register("coral_claw", new CoralClawFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<CountConfiguration> SEA_PICKLE = Feature.register("sea_pickle", new SeaPickleFeature(CountConfiguration.CODEC));
    public static final Feature<SimpleBlockConfiguration> SIMPLE_BLOCK = Feature.register("simple_block", new SimpleBlockFeature(SimpleBlockConfiguration.CODEC));
    public static final Feature<ProbabilityFeatureConfiguration> BAMBOO = Feature.register("bamboo", new BambooFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final Feature<HugeFungusConfiguration> HUGE_FUNGUS = Feature.register("huge_fungus", new HugeFungusFeature(HugeFungusConfiguration.CODEC));
    public static final Feature<NetherForestVegetationConfig> NETHER_FOREST_VEGETATION = Feature.register("nether_forest_vegetation", new NetherForestVegetationFeature(NetherForestVegetationConfig.CODEC));
    public static final Feature<NoneFeatureConfiguration> WEEPING_VINES = Feature.register("weeping_vines", new WeepingVinesFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<TwistingVinesConfig> TWISTING_VINES = Feature.register("twisting_vines", new TwistingVinesFeature(TwistingVinesConfig.CODEC));
    public static final Feature<ColumnFeatureConfiguration> BASALT_COLUMNS = Feature.register("basalt_columns", new BasaltColumnsFeature(ColumnFeatureConfiguration.CODEC));
    public static final Feature<DeltaFeatureConfiguration> DELTA_FEATURE = Feature.register("delta_feature", new DeltaFeature(DeltaFeatureConfiguration.CODEC));
    public static final Feature<ReplaceSphereConfiguration> REPLACE_BLOBS = Feature.register("netherrack_replace_blobs", new ReplaceBlobsFeature(ReplaceSphereConfiguration.CODEC));
    public static final Feature<LayerConfiguration> FILL_LAYER = Feature.register("fill_layer", new FillLayerFeature(LayerConfiguration.CODEC));
    public static final BonusChestFeature BONUS_CHEST = Feature.register("bonus_chest", new BonusChestFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> BASALT_PILLAR = Feature.register("basalt_pillar", new BasaltPillarFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<OreConfiguration> SCATTERED_ORE = Feature.register("scattered_ore", new ScatteredOreFeature(OreConfiguration.CODEC));
    public static final Feature<RandomFeatureConfiguration> RANDOM_SELECTOR = Feature.register("random_selector", new RandomSelectorFeature(RandomFeatureConfiguration.CODEC));
    public static final Feature<SimpleRandomFeatureConfiguration> SIMPLE_RANDOM_SELECTOR = Feature.register("simple_random_selector", new SimpleRandomSelectorFeature(SimpleRandomFeatureConfiguration.CODEC));
    public static final Feature<RandomBooleanFeatureConfiguration> RANDOM_BOOLEAN_SELECTOR = Feature.register("random_boolean_selector", new RandomBooleanSelectorFeature(RandomBooleanFeatureConfiguration.CODEC));
    public static final Feature<GeodeConfiguration> GEODE = Feature.register("geode", new GeodeFeature(GeodeConfiguration.CODEC));
    public static final Feature<DripstoneClusterConfiguration> DRIPSTONE_CLUSTER = Feature.register("dripstone_cluster", new DripstoneClusterFeature(DripstoneClusterConfiguration.CODEC));
    public static final Feature<LargeDripstoneConfiguration> LARGE_DRIPSTONE = Feature.register("large_dripstone", new LargeDripstoneFeature(LargeDripstoneConfiguration.CODEC));
    public static final Feature<PointedDripstoneConfiguration> POINTED_DRIPSTONE = Feature.register("pointed_dripstone", new PointedDripstoneFeature(PointedDripstoneConfiguration.CODEC));
    public static final Feature<SculkPatchConfiguration> SCULK_PATCH = Feature.register("sculk_patch", new SculkPatchFeature(SculkPatchConfiguration.CODEC));
    private final MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec;

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        return (F)Registry.register(BuiltInRegistries.FEATURE, name, feature);
    }

    public Feature(Codec<FC> codec) {
        this.configuredCodec = codec.fieldOf("config").xmap(c -> new ConfiguredFeature<FeatureConfiguration, Feature>(this, (FeatureConfiguration)c), ConfiguredFeature::config);
    }

    public MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec() {
        return this.configuredCodec;
    }

    protected void setBlock(LevelWriter level, BlockPos pos, BlockState blockState) {
        level.setBlock(pos, blockState, 3);
    }

    public static Predicate<BlockState> isReplaceable(TagKey<Block> cannotReplaceTag) {
        return s -> !s.is(cannotReplaceTag);
    }

    protected void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> canReplace) {
        if (canReplace.test(level.getBlockState(pos))) {
            level.setBlock(pos, state, 2);
        }
    }

    public abstract boolean place(FeaturePlaceContext<FC> var1);

    public boolean place(FC config, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        if (level.ensureCanWrite(origin)) {
            return this.place(new FeaturePlaceContext<FC>(Optional.empty(), level, chunkGenerator, random, origin, config));
        }
        return false;
    }

    public static boolean checkNeighbors(Function<BlockPos, BlockState> blockGetter, BlockPos pos, Predicate<BlockState> predicate) {
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            neighborPos.setWithOffset((Vec3i)pos, direction);
            if (!predicate.test(blockGetter.apply(neighborPos))) continue;
            return true;
        }
        return false;
    }

    public static boolean isAdjacentToAir(Function<BlockPos, BlockState> blockGetter, BlockPos pos) {
        return Feature.checkNeighbors(blockGetter, pos, BlockBehaviour.BlockStateBase::isAir);
    }

    protected void markAboveForPostProcessing(WorldGenLevel level, BlockPos placePos) {
        BlockPos.MutableBlockPos pos = placePos.mutable();
        for (int i = 0; i < 2; ++i) {
            pos.move(Direction.UP);
            if (level.getBlockState(pos).isAir()) {
                return;
            }
            level.getChunk(pos).markPosForPostprocessing(pos);
        }
    }

    public static void markForPostProcessing(WorldGenLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).isAir()) {
            level.getChunk(pos).markPosForPostprocessing(pos);
        }
    }
}

