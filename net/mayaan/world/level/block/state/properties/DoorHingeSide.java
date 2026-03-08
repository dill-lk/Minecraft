/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.state.properties;

import net.mayaan.util.StringRepresentable;

public enum DoorHingeSide implements StringRepresentable
{
    LEFT,
    RIGHT;


    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return this == LEFT ? "left" : "right";
    }
}

