/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.component.DataComponents;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.animal.Bucketable;
import net.mayaan.world.item.BucketItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.CustomData;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.material.Fluid;
import org.jspecify.annotations.Nullable;

public class MobBucketItem
extends BucketItem {
    private final EntityType<? extends Mob> type;
    private final SoundEvent emptySound;

    public MobBucketItem(EntityType<? extends Mob> type, Fluid content, SoundEvent emptySound, Item.Properties properties) {
        super(content, properties);
        this.type = type;
        this.emptySound = emptySound;
    }

    @Override
    public void checkExtraContent(@Nullable LivingEntity user, Level level, ItemStack itemStack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, itemStack, pos);
            level.gameEvent((Entity)user, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    protected void playEmptySound(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos) {
        level.playSound(user, pos, this.emptySound, SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    private void spawn(ServerLevel level, ItemStack itemStack, BlockPos spawnPos) {
        Mob mob = this.type.create(level, EntityType.createDefaultStackConfig(level, itemStack, null), spawnPos, EntitySpawnReason.BUCKET, true, false);
        if (mob instanceof Bucketable) {
            Bucketable bucketable = (Bucketable)((Object)mob);
            CustomData entityData = itemStack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
            bucketable.loadFromBucketTag(entityData.copyTag());
            bucketable.setFromBucket(true);
        }
        if (mob != null) {
            level.addFreshEntityWithPassengers(mob);
            mob.playAmbientSound();
        }
    }
}

