/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands;

import java.util.Optional;
import java.util.stream.Stream;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.flag.FeatureFlagSet;

public interface CommandBuildContext
extends HolderLookup.Provider {
    public static CommandBuildContext simple(final HolderLookup.Provider access, final FeatureFlagSet enabledFeatures) {
        return new CommandBuildContext(){

            @Override
            public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
                return access.listRegistryKeys();
            }

            public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                return access.lookup(key).map(lookup -> lookup.filterFeatures(enabledFeatures));
            }

            @Override
            public FeatureFlagSet enabledFeatures() {
                return enabledFeatures;
            }
        };
    }

    public FeatureFlagSet enabledFeatures();
}

