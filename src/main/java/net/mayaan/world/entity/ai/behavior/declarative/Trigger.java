/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior.declarative;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.LivingEntity;

public interface Trigger<E extends LivingEntity> {
    public boolean trigger(ServerLevel var1, E var2, long var3);
}

