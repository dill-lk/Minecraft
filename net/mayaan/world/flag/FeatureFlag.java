/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.flag;

import net.mayaan.world.flag.FeatureFlagUniverse;

public class FeatureFlag {
    final FeatureFlagUniverse universe;
    final long mask;

    FeatureFlag(FeatureFlagUniverse universe, int bit) {
        this.universe = universe;
        this.mask = 1L << bit;
    }
}

