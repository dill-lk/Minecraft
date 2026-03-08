/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity;

import net.mayaan.world.entity.Entity;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface EntityProcessor {
    public static final EntityProcessor NOP = input -> input;

    public @Nullable Entity process(Entity var1);
}

