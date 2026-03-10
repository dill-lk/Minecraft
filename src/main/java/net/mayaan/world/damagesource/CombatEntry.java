/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.damagesource;

import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.damagesource.FallLocation;
import org.jspecify.annotations.Nullable;

public record CombatEntry(DamageSource source, float damage, @Nullable FallLocation fallLocation, float fallDistance) {
}

