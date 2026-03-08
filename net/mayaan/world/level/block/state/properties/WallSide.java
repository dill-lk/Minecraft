/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.state.properties;

import net.mayaan.util.StringRepresentable;

public enum WallSide implements StringRepresentable
{
    NONE("none"),
    LOW("low"),
    TALL("tall");

    private final String name;

    private WallSide(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

