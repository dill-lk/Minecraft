/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.phys;

import net.mayaan.world.entity.Entity;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class EntityHitResult
extends HitResult {
    private final Entity entity;

    public EntityHitResult(Entity entity) {
        this(entity, entity.position());
    }

    public EntityHitResult(Entity entity, Vec3 location) {
        super(location);
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HitResult.Type getType() {
        return HitResult.Type.ENTITY;
    }
}

