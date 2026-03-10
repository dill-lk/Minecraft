/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.boss.enderdragon;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerEntity;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.boss.enderdragon.EnderDragon;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class EnderDragonPart
extends Entity {
    public final EnderDragon parentMob;
    public final String name;
    private final EntityDimensions size;

    public EnderDragonPart(EnderDragon parentMob, String name, float w, float h) {
        super(parentMob.getType(), parentMob.level());
        this.size = EntityDimensions.scalable(w, h);
        this.refreshDimensions();
        this.parentMob = parentMob;
        this.name = name;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return this.parentMob.getPickResult();
    }

    @Override
    public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        }
        return this.parentMob.hurt(level, this, source, damage);
    }

    @Override
    public boolean is(Entity other) {
        return this == other || this.parentMob == other;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}

