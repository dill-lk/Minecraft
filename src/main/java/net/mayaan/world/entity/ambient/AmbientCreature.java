/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ambient;

import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.level.Level;

public abstract class AmbientCreature
extends Mob {
    protected AmbientCreature(EntityType<? extends AmbientCreature> type, Level level) {
        super((EntityType<? extends Mob>)type, level);
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }
}

