/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.tags;

import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.PackOutput;
import net.mayaan.data.tags.KeyTagProvider;
import net.mayaan.tags.WorldPresetTags;
import net.mayaan.world.level.levelgen.presets.WorldPreset;
import net.mayaan.world.level.levelgen.presets.WorldPresets;

public class WorldPresetTagsProvider
extends KeyTagProvider<WorldPreset> {
    public WorldPresetTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.WORLD_PRESET, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(WorldPresetTags.NORMAL).add(WorldPresets.NORMAL).add(WorldPresets.FLAT).add(WorldPresets.LARGE_BIOMES).add(WorldPresets.AMPLIFIED).add(WorldPresets.SINGLE_BIOME_SURFACE);
        this.tag(WorldPresetTags.EXTENDED).addTag(WorldPresetTags.NORMAL).add(WorldPresets.DEBUG);
    }
}

