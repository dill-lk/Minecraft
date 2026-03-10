/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity;

import net.mayaan.world.entity.ConversionType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.scores.PlayerTeam;
import org.jspecify.annotations.Nullable;

public record ConversionParams(ConversionType type, boolean keepEquipment, boolean preserveCanPickUpLoot, @Nullable PlayerTeam team) {
    public static ConversionParams single(Mob mob, boolean keepEquipment, boolean preserveCanPickUpLoot) {
        return new ConversionParams(ConversionType.SINGLE, keepEquipment, preserveCanPickUpLoot, mob.getTeam());
    }

    @FunctionalInterface
    public static interface AfterConversion<T extends Mob> {
        public void finalizeConversion(T var1);
    }
}

