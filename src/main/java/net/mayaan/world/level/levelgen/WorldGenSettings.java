/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Objects;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.world.level.dimension.LevelStem;
import net.mayaan.world.level.levelgen.WorldDimensions;
import net.mayaan.world.level.levelgen.WorldOptions;
import net.mayaan.world.level.saveddata.SavedData;
import net.mayaan.world.level.saveddata.SavedDataType;

public final class WorldGenSettings
extends SavedData {
    public static final Codec<WorldGenSettings> CODEC = RecordCodecBuilder.create(i -> i.group((App)WorldOptions.CODEC.forGetter(WorldGenSettings::options), (App)WorldDimensions.CODEC.forGetter(WorldGenSettings::dimensions)).apply((Applicative)i, i.stable(WorldGenSettings::new)));
    public static final SavedDataType<WorldGenSettings> TYPE = new SavedDataType<WorldGenSettings>(Identifier.withDefaultNamespace("world_gen_settings"), () -> new WorldGenSettings(WorldOptions.defaultWithRandomSeed(), new WorldDimensions(new HashMap<ResourceKey<LevelStem>, LevelStem>())), CODEC, DataFixTypes.SAVED_DATA_WORLD_GEN_SETTINGS);
    private final WorldOptions options;
    private final WorldDimensions dimensions;

    public WorldGenSettings(WorldOptions options, WorldDimensions dimensions) {
        this.options = options;
        this.dimensions = dimensions;
    }

    public static WorldGenSettings of(WorldOptions options, RegistryAccess registryAccess) {
        return new WorldGenSettings(options, new WorldDimensions((Registry<LevelStem>)registryAccess.lookupOrThrow(Registries.LEVEL_STEM)));
    }

    public WorldOptions options() {
        return this.options;
    }

    public WorldDimensions dimensions() {
        return this.dimensions;
    }

    public int hashCode() {
        return Objects.hash(this.options, this.dimensions);
    }

    public String toString() {
        return "WorldGenSettings[options=" + String.valueOf(this.options) + ", dimensions=" + String.valueOf(this.dimensions) + "]";
    }
}

