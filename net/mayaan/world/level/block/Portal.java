/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.portal.TeleportTransition;
import org.jspecify.annotations.Nullable;

public interface Portal {
    default public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return 0;
    }

    public @Nullable TeleportTransition getPortalDestination(ServerLevel var1, Entity var2, BlockPos var3);

    default public Transition getLocalTransition() {
        return Transition.NONE;
    }

    public static enum Transition {
        CONFUSION,
        NONE;

    }
}

