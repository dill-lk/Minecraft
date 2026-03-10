/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.flag;

import java.util.Set;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.flag.FeatureFlagSet;

public interface FeatureElement {
    public static final Set<ResourceKey<? extends Registry<? extends FeatureElement>>> FILTERED_REGISTRIES = Set.of(Registries.ITEM, Registries.BLOCK, Registries.ENTITY_TYPE, Registries.GAME_RULE, Registries.MENU, Registries.POTION, Registries.MOB_EFFECT);

    public FeatureFlagSet requiredFeatures();

    default public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return this.requiredFeatures().isSubsetOf(enabledFeatures);
    }
}

