/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.frog;

import net.mayaan.core.ClientAsset;
import net.mayaan.core.HolderSet;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.BiomeTags;
import net.mayaan.tags.TagKey;
import net.mayaan.world.entity.animal.TemperatureVariants;
import net.mayaan.world.entity.animal.frog.FrogVariant;
import net.mayaan.world.entity.variant.BiomeCheck;
import net.mayaan.world.entity.variant.SpawnPrioritySelectors;
import net.mayaan.world.level.biome.Biome;

public interface FrogVariants {
    public static final ResourceKey<FrogVariant> TEMPERATE = FrogVariants.createKey(TemperatureVariants.TEMPERATE);
    public static final ResourceKey<FrogVariant> WARM = FrogVariants.createKey(TemperatureVariants.WARM);
    public static final ResourceKey<FrogVariant> COLD = FrogVariants.createKey(TemperatureVariants.COLD);

    private static ResourceKey<FrogVariant> createKey(Identifier id) {
        return ResourceKey.create(Registries.FROG_VARIANT, id);
    }

    public static void bootstrap(BootstrapContext<FrogVariant> registry) {
        FrogVariants.register(registry, TEMPERATE, "entity/frog/frog_temperate", SpawnPrioritySelectors.fallback(0));
        FrogVariants.register(registry, WARM, "entity/frog/frog_warm", BiomeTags.SPAWNS_WARM_VARIANT_FROGS);
        FrogVariants.register(registry, COLD, "entity/frog/frog_cold", BiomeTags.SPAWNS_COLD_VARIANT_FROGS);
    }

    private static void register(BootstrapContext<FrogVariant> context, ResourceKey<FrogVariant> name, String assetId, TagKey<Biome> limitToBiome) {
        HolderSet.Named<Biome> biomes = context.lookup(Registries.BIOME).getOrThrow(limitToBiome);
        FrogVariants.register(context, name, assetId, SpawnPrioritySelectors.single(new BiomeCheck(biomes), 1));
    }

    private static void register(BootstrapContext<FrogVariant> context, ResourceKey<FrogVariant> name, String assetId, SpawnPrioritySelectors selectors) {
        context.register(name, new FrogVariant(new ClientAsset.ResourceTexture(Identifier.withDefaultNamespace(assetId)), selectors));
    }
}

