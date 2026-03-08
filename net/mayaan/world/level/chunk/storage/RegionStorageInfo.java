/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.chunk.storage;

import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.Level;

public record RegionStorageInfo(String level, ResourceKey<Level> dimension, String type) {
    public RegionStorageInfo withTypeSuffix(String suffix) {
        return new RegionStorageInfo(this.level, this.dimension, this.type + suffix);
    }
}

