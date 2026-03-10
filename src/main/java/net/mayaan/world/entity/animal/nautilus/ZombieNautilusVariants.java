/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.nautilus;

import net.mayaan.core.HolderSet;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.BiomeTags;
import net.mayaan.tags.TagKey;
import net.mayaan.world.entity.animal.TemperatureVariants;
import net.mayaan.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.mayaan.world.entity.variant.BiomeCheck;
import net.mayaan.world.entity.variant.ModelAndTexture;
import net.mayaan.world.entity.variant.SpawnPrioritySelectors;
import net.mayaan.world.level.biome.Biome;

public class ZombieNautilusVariants {
    public static final ResourceKey<ZombieNautilusVariant> TEMPERATE = ZombieNautilusVariants.createKey(TemperatureVariants.TEMPERATE);
    public static final ResourceKey<ZombieNautilusVariant> WARM = ZombieNautilusVariants.createKey(TemperatureVariants.WARM);
    public static final ResourceKey<ZombieNautilusVariant> DEFAULT = TEMPERATE;

    private static ResourceKey<ZombieNautilusVariant> createKey(Identifier id) {
        return ResourceKey.create(Registries.ZOMBIE_NAUTILUS_VARIANT, id);
    }

    public static void bootstrap(BootstrapContext<ZombieNautilusVariant> context) {
        ZombieNautilusVariants.register(context, TEMPERATE, ZombieNautilusVariant.ModelType.NORMAL, "zombie_nautilus", SpawnPrioritySelectors.fallback(0));
        ZombieNautilusVariants.register(context, WARM, ZombieNautilusVariant.ModelType.WARM, "zombie_nautilus_coral", BiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS);
    }

    private static void register(BootstrapContext<ZombieNautilusVariant> context, ResourceKey<ZombieNautilusVariant> name, ZombieNautilusVariant.ModelType modelType, String textureName, TagKey<Biome> spawnBiome) {
        HolderSet.Named<Biome> biomes = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
        ZombieNautilusVariants.register(context, name, modelType, textureName, SpawnPrioritySelectors.single(new BiomeCheck(biomes), 1));
    }

    private static void register(BootstrapContext<ZombieNautilusVariant> context, ResourceKey<ZombieNautilusVariant> name, ZombieNautilusVariant.ModelType modelType, String textureName, SpawnPrioritySelectors selectors) {
        Identifier textureId = Identifier.withDefaultNamespace("entity/nautilus/" + textureName);
        context.register(name, new ZombieNautilusVariant(new ModelAndTexture<ZombieNautilusVariant.ModelType>(modelType, textureId), selectors));
    }
}

