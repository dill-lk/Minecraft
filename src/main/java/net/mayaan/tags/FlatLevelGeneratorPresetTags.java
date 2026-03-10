/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.levelgen.flat.FlatLevelGeneratorPreset;

public class FlatLevelGeneratorPresetTags {
    public static final TagKey<FlatLevelGeneratorPreset> VISIBLE = FlatLevelGeneratorPresetTags.create("visible");

    private FlatLevelGeneratorPresetTags() {
    }

    private static TagKey<FlatLevelGeneratorPreset> create(String name) {
        return TagKey.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, Identifier.withDefaultNamespace(name));
    }
}

