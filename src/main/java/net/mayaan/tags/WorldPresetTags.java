/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.levelgen.presets.WorldPreset;

public class WorldPresetTags {
    public static final TagKey<WorldPreset> NORMAL = WorldPresetTags.create("normal");
    public static final TagKey<WorldPreset> EXTENDED = WorldPresetTags.create("extended");

    private WorldPresetTags() {
    }

    private static TagKey<WorldPreset> create(String name) {
        return TagKey.create(Registries.WORLD_PRESET, Identifier.withDefaultNamespace(name));
    }
}

