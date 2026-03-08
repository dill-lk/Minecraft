/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen.placement;

import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.features.PileFeatures;
import net.mayaan.data.worldgen.features.TreeFeatures;
import net.mayaan.data.worldgen.features.VegetationFeatures;
import net.mayaan.data.worldgen.placement.PlacementUtils;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;
import net.mayaan.world.level.levelgen.placement.PlacementModifier;

public class VillagePlacements {
    public static final ResourceKey<PlacedFeature> PILE_HAY_VILLAGE = PlacementUtils.createKey("pile_hay");
    public static final ResourceKey<PlacedFeature> PILE_MELON_VILLAGE = PlacementUtils.createKey("pile_melon");
    public static final ResourceKey<PlacedFeature> PILE_SNOW_VILLAGE = PlacementUtils.createKey("pile_snow");
    public static final ResourceKey<PlacedFeature> PILE_ICE_VILLAGE = PlacementUtils.createKey("pile_ice");
    public static final ResourceKey<PlacedFeature> PILE_PUMPKIN_VILLAGE = PlacementUtils.createKey("pile_pumpkin");
    public static final ResourceKey<PlacedFeature> OAK_VILLAGE = PlacementUtils.createKey("oak");
    public static final ResourceKey<PlacedFeature> ACACIA_VILLAGE = PlacementUtils.createKey("acacia");
    public static final ResourceKey<PlacedFeature> SPRUCE_VILLAGE = PlacementUtils.createKey("spruce");
    public static final ResourceKey<PlacedFeature> PINE_VILLAGE = PlacementUtils.createKey("pine");
    public static final ResourceKey<PlacedFeature> PATCH_CACTUS_VILLAGE = PlacementUtils.createKey("patch_cactus");
    public static final ResourceKey<PlacedFeature> FLOWER_PLAIN_VILLAGE = PlacementUtils.createKey("flower_plain");
    public static final ResourceKey<PlacedFeature> PATCH_TAIGA_GRASS_VILLAGE = PlacementUtils.createKey("patch_taiga_grass");
    public static final ResourceKey<PlacedFeature> PATCH_BERRY_BUSH_VILLAGE = PlacementUtils.createKey("patch_berry_bush");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder.Reference<ConfiguredFeature<?, ?>> pileHay = configuredFeatures.getOrThrow(PileFeatures.PILE_HAY);
        Holder.Reference<ConfiguredFeature<?, ?>> pileMelon = configuredFeatures.getOrThrow(PileFeatures.PILE_MELON);
        Holder.Reference<ConfiguredFeature<?, ?>> pileSnow = configuredFeatures.getOrThrow(PileFeatures.PILE_SNOW);
        Holder.Reference<ConfiguredFeature<?, ?>> pileIce = configuredFeatures.getOrThrow(PileFeatures.PILE_ICE);
        Holder.Reference<ConfiguredFeature<?, ?>> pilePumpkin = configuredFeatures.getOrThrow(PileFeatures.PILE_PUMPKIN);
        Holder.Reference<ConfiguredFeature<?, ?>> oak = configuredFeatures.getOrThrow(TreeFeatures.OAK);
        Holder.Reference<ConfiguredFeature<?, ?>> acacia = configuredFeatures.getOrThrow(TreeFeatures.ACACIA);
        Holder.Reference<ConfiguredFeature<?, ?>> spruce = configuredFeatures.getOrThrow(TreeFeatures.SPRUCE);
        Holder.Reference<ConfiguredFeature<?, ?>> pine = configuredFeatures.getOrThrow(TreeFeatures.PINE);
        Holder.Reference<ConfiguredFeature<?, ?>> patchCactus = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_CACTUS);
        Holder.Reference<ConfiguredFeature<?, ?>> flowerPlain = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PLAIN);
        Holder.Reference<ConfiguredFeature<?, ?>> patchTaigaGrass = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_TAIGA_GRASS);
        Holder.Reference<ConfiguredFeature<?, ?>> patchBerryBush = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_BERRY_BUSH);
        PlacementUtils.register(context, PILE_HAY_VILLAGE, pileHay, new PlacementModifier[0]);
        PlacementUtils.register(context, PILE_MELON_VILLAGE, pileMelon, new PlacementModifier[0]);
        PlacementUtils.register(context, PILE_SNOW_VILLAGE, pileSnow, new PlacementModifier[0]);
        PlacementUtils.register(context, PILE_ICE_VILLAGE, pileIce, new PlacementModifier[0]);
        PlacementUtils.register(context, PILE_PUMPKIN_VILLAGE, pilePumpkin, new PlacementModifier[0]);
        PlacementUtils.register(context, OAK_VILLAGE, oak, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(context, ACACIA_VILLAGE, acacia, PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING));
        PlacementUtils.register(context, SPRUCE_VILLAGE, spruce, PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
        PlacementUtils.register(context, PINE_VILLAGE, pine, PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
        PlacementUtils.register(context, PATCH_CACTUS_VILLAGE, patchCactus, new PlacementModifier[0]);
        PlacementUtils.register(context, FLOWER_PLAIN_VILLAGE, flowerPlain, new PlacementModifier[0]);
        PlacementUtils.register(context, PATCH_TAIGA_GRASS_VILLAGE, patchTaigaGrass, new PlacementModifier[0]);
        PlacementUtils.register(context, PATCH_BERRY_BUSH_VILLAGE, patchBerryBush, new PlacementModifier[0]);
    }
}

