/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.entity;

import net.mayaan.world.entity.Entity;

public interface EntityInLevelCallback {
    public static final EntityInLevelCallback NULL = new EntityInLevelCallback(){

        @Override
        public void onMove() {
        }

        @Override
        public void onRemove(Entity.RemovalReason reason) {
        }
    };

    public void onMove();

    public void onRemove(Entity.RemovalReason var1);
}

