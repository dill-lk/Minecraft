/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level;

import net.mayaan.world.level.biome.Biome;

@FunctionalInterface
public interface ColorResolver {
    public int getColor(Biome var1, double var2, double var4);
}

