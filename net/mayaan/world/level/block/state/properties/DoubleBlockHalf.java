/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.state.properties;

import net.mayaan.core.Direction;
import net.mayaan.util.StringRepresentable;

public enum DoubleBlockHalf implements StringRepresentable
{
    UPPER(Direction.DOWN),
    LOWER(Direction.UP);

    private final Direction directionToOther;

    private DoubleBlockHalf(Direction directionToOther) {
        this.directionToOther = directionToOther;
    }

    public Direction getDirectionToOther() {
        return this.directionToOther;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return this == UPPER ? "upper" : "lower";
    }

    public DoubleBlockHalf getOtherHalf() {
        return this == UPPER ? LOWER : UPPER;
    }
}

