/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.state.properties;

import net.mayaan.util.StringRepresentable;

public enum SculkSensorPhase implements StringRepresentable
{
    INACTIVE("inactive"),
    ACTIVE("active"),
    COOLDOWN("cooldown");

    private final String name;

    private SculkSensorPhase(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

