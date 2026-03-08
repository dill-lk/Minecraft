/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.world.Container;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.phys.AABB;

public interface Hopper
extends Container {
    public static final AABB SUCK_AABB = Block.column(16.0, 11.0, 32.0).toAabbs().get(0);

    default public AABB getSuckAabb() {
        return SUCK_AABB;
    }

    public double getLevelX();

    public double getLevelY();

    public double getLevelZ();

    public boolean isGridAligned();
}

