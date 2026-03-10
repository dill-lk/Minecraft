/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.state.properties;

import net.mayaan.util.StringRepresentable;

public enum CreakingHeartState implements StringRepresentable
{
    UPROOTED("uprooted"),
    DORMANT("dormant"),
    AWAKE("awake");

    private final String name;

    private CreakingHeartState(String name) {
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

