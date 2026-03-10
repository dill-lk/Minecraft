/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package net.mayaan.world.effect;

import java.util.function.ToIntFunction;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.monster.Silverfish;
import net.mayaan.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

class InfestedMobEffect
extends MobEffect {
    private final float chanceToSpawn;
    private final ToIntFunction<RandomSource> spawnedCount;

    protected InfestedMobEffect(MobEffectCategory category, int color, float chanceToSpawn, ToIntFunction<RandomSource> spawnedCount) {
        super(category, color, ParticleTypes.INFESTED);
        this.chanceToSpawn = chanceToSpawn;
        this.spawnedCount = spawnedCount;
    }

    @Override
    public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {
        if (mob.getRandom().nextFloat() <= this.chanceToSpawn) {
            int count = this.spawnedCount.applyAsInt(mob.getRandom());
            for (int i = 0; i < count; ++i) {
                this.spawnSilverfish(level, mob, mob.getX(), mob.getY() + (double)mob.getBbHeight() / 2.0, mob.getZ());
            }
        }
    }

    private void spawnSilverfish(ServerLevel level, LivingEntity mob, double x, double y, double z) {
        Silverfish silverfish = EntityType.SILVERFISH.create(level, EntitySpawnReason.TRIGGERED);
        if (silverfish == null) {
            return;
        }
        RandomSource random = mob.getRandom();
        float angle = 1.5707964f;
        float randomAngle = Mth.randomBetween(random, -1.5707964f, 1.5707964f);
        Vector3f viewDirection = mob.getLookAngle().toVector3f().mul(0.3f).mul(1.0f, 1.5f, 1.0f).rotateY(randomAngle);
        silverfish.snapTo(x, y, z, level.getRandom().nextFloat() * 360.0f, 0.0f);
        silverfish.setDeltaMovement(new Vec3((Vector3fc)viewDirection));
        level.addFreshEntity(silverfish);
        silverfish.playSound(SoundEvents.SILVERFISH_HURT);
    }
}

