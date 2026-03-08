/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixUtils
 */
package net.mayaan.data.registries;

import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.mayaan.core.Cloner;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.RegistrySetBuilder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.registries.VanillaRegistries;
import net.mayaan.resources.RegistryDataLoader;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class RegistryPatchGenerator {
    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanilla, RegistrySetBuilder packBuilder) {
        return vanilla.thenApply(parent -> {
            RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
            Cloner.Factory cloner = new Cloner.Factory();
            RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(registryData -> registryData.runWithArguments(cloner::addCodec));
            RegistrySetBuilder.PatchedRegistries newRegistries = packBuilder.buildPatch(staticRegistries, (HolderLookup.Provider)parent, cloner);
            HolderLookup.Provider fullPatchedRegistry = newRegistries.full();
            Optional<HolderLookup.RegistryLookup<Biome>> biomes = fullPatchedRegistry.lookup(Registries.BIOME);
            Optional<HolderLookup.RegistryLookup<PlacedFeature>> features = fullPatchedRegistry.lookup(Registries.PLACED_FEATURE);
            if (biomes.isPresent() || features.isPresent()) {
                VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter((HolderGetter)DataFixUtils.orElseGet(features, () -> parent.lookupOrThrow(Registries.PLACED_FEATURE)), (HolderLookup)DataFixUtils.orElseGet(biomes, () -> parent.lookupOrThrow(Registries.BIOME)));
            }
            return newRegistries;
        });
    }
}

