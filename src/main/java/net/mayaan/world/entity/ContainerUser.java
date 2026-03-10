/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.level.block.entity.ContainerOpenersCounter;

public interface ContainerUser {
    public boolean hasContainerOpen(ContainerOpenersCounter var1, BlockPos var2);

    public double getContainerInteractionRange();

    default public LivingEntity getLivingEntity() {
        if (this instanceof LivingEntity) {
            return (LivingEntity)((Object)this);
        }
        throw new IllegalStateException("A container user must be a LivingEntity");
    }
}

