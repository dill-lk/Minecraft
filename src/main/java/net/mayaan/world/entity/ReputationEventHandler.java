/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity;

import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.ai.village.ReputationEventType;

public interface ReputationEventHandler {
    public void onReputationEventFrom(ReputationEventType var1, Entity var2);
}

