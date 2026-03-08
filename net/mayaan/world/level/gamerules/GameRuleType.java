/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.gamerules;

import net.mayaan.util.StringRepresentable;

public enum GameRuleType implements StringRepresentable
{
    INT("integer"),
    BOOL("boolean");

    private final String name;

    private GameRuleType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

