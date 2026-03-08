/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.monster.spider;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.Difficulty;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.monster.spider.Spider;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CaveSpider
extends Spider {
    public CaveSpider(EntityType<? extends CaveSpider> type, Level level) {
        super((EntityType<? extends Spider>)type, level);
    }

    public static AttributeSupplier.Builder createCaveSpider() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 12.0);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            if (target instanceof LivingEntity) {
                int poisonTime = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    poisonTime = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    poisonTime = 15;
                }
                if (poisonTime > 0) {
                    ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0), this);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        return groupData;
    }

    @Override
    public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
        if (vehicle.getBbWidth() <= this.getBbWidth()) {
            return new Vec3(0.0, 0.21875 * (double)this.getScale(), 0.0);
        }
        return super.getVehicleAttachmentPoint(vehicle);
    }
}

