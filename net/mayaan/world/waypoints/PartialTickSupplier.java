/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.waypoints;

import net.mayaan.world.entity.Entity;

@FunctionalInterface
public interface PartialTickSupplier {
    public float apply(Entity var1);
}

