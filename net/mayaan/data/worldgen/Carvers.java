/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen;

import net.mayaan.core.HolderGetter;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.valueproviders.ConstantFloat;
import net.mayaan.util.valueproviders.TrapezoidFloat;
import net.mayaan.util.valueproviders.UniformFloat;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.levelgen.VerticalAnchor;
import net.mayaan.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.mayaan.world.level.levelgen.carver.CarverDebugSettings;
import net.mayaan.world.level.levelgen.carver.CaveCarverConfiguration;
import net.mayaan.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.mayaan.world.level.levelgen.carver.WorldCarver;
import net.mayaan.world.level.levelgen.heightproviders.UniformHeight;

public class Carvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE = Carvers.createKey("cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE_EXTRA_UNDERGROUND = Carvers.createKey("cave_extra_underground");
    public static final ResourceKey<ConfiguredWorldCarver<?>> CANYON = Carvers.createKey("canyon");
    public static final ResourceKey<ConfiguredWorldCarver<?>> NETHER_CAVE = Carvers.createKey("nether_cave");

    private static ResourceKey<ConfiguredWorldCarver<?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, Identifier.withDefaultNamespace(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        context.register(CAVE, WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.15f, UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)), UniformFloat.of(0.1f, 0.9f), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()), blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), UniformFloat.of(0.7f, 1.4f), UniformFloat.of(0.8f, 1.3f), UniformFloat.of(-1.0f, -0.4f))));
        context.register(CAVE_EXTRA_UNDERGROUND, WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.07f, UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(47)), UniformFloat.of(0.1f, 0.9f), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.OAK_BUTTON.defaultBlockState()), blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), UniformFloat.of(0.7f, 1.4f), UniformFloat.of(0.8f, 1.3f), UniformFloat.of(-1.0f, -0.4f))));
        context.register(CANYON, WorldCarver.CANYON.configured(new CanyonCarverConfiguration(0.01f, UniformHeight.of(VerticalAnchor.absolute(10), VerticalAnchor.absolute(67)), ConstantFloat.of(3.0f), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()), blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), UniformFloat.of(-0.125f, 0.125f), new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75f, 1.0f), TrapezoidFloat.of(0.0f, 6.0f, 2.0f), 3, UniformFloat.of(0.75f, 1.0f), 1.0f, 0.0f))));
        context.register(NETHER_CAVE, WorldCarver.NETHER_CAVE.configured(new CaveCarverConfiguration(0.2f, UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.belowTop(1)), ConstantFloat.of(0.5f), VerticalAnchor.aboveBottom(10), blocks.getOrThrow(BlockTags.NETHER_CARVER_REPLACEABLES), ConstantFloat.of(1.0f), ConstantFloat.of(1.0f), ConstantFloat.of(-0.7f))));
    }
}

