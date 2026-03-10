/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal;

import java.util.Optional;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.component.DataComponents;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemUtils;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.component.CustomData;
import net.mayaan.world.level.Level;

public interface Bucketable {
    public boolean fromBucket();

    public void setFromBucket(boolean var1);

    public void saveToBucketTag(ItemStack var1);

    public void loadFromBucketTag(CompoundTag var1);

    public ItemStack getBucketItemStack();

    public SoundEvent getPickupSound();

    @Deprecated
    public static void saveDefaultDataToBucketTag(Mob entity, ItemStack bucket) {
        bucket.copyFrom(DataComponents.CUSTOM_NAME, entity);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
            if (entity.isNoAi()) {
                tag.putBoolean("NoAI", entity.isNoAi());
            }
            if (entity.isSilent()) {
                tag.putBoolean("Silent", entity.isSilent());
            }
            if (entity.isNoGravity()) {
                tag.putBoolean("NoGravity", entity.isNoGravity());
            }
            if (entity.hasGlowingTag()) {
                tag.putBoolean("Glowing", entity.hasGlowingTag());
            }
            if (entity.isInvulnerable()) {
                tag.putBoolean("Invulnerable", entity.isInvulnerable());
            }
            tag.putFloat("Health", entity.getHealth());
        });
    }

    @Deprecated
    public static void loadDefaultDataFromBucketTag(Mob entity, CompoundTag tag) {
        tag.getBoolean("NoAI").ifPresent(entity::setNoAi);
        tag.getBoolean("Silent").ifPresent(entity::setSilent);
        tag.getBoolean("NoGravity").ifPresent(entity::setNoGravity);
        tag.getBoolean("Glowing").ifPresent(entity::setGlowingTag);
        tag.getBoolean("Invulnerable").ifPresent(entity::setInvulnerable);
        tag.getFloat("Health").ifPresent(entity::setHealth);
    }

    public static <T extends LivingEntity> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T pickupEntity) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == Items.WATER_BUCKET && pickupEntity.isAlive()) {
            pickupEntity.playSound(((Bucketable)((Object)pickupEntity)).getPickupSound(), 1.0f, 1.0f);
            ItemStack bucket = ((Bucketable)((Object)pickupEntity)).getBucketItemStack();
            ((Bucketable)((Object)pickupEntity)).saveToBucketTag(bucket);
            ItemStack result = ItemUtils.createFilledResult(itemStack, player, bucket, false);
            player.setItemInHand(hand, result);
            Level level = pickupEntity.level();
            if (!level.isClientSide()) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucket);
            }
            pickupEntity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        }
        return Optional.empty();
    }
}

