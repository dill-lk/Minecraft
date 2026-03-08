/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.sounds.SoundEvents;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemUseAnimation;
import net.mayaan.world.item.ItemUtils;
import net.mayaan.world.level.Level;

public class SpyglassItem
extends Item {
    public static final int USE_DURATION = 1200;
    public static final float ZOOM_FOV_MODIFIER = 0.1f;

    public SpyglassItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 1200;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.SPYGLASS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.playSound(SoundEvents.SPYGLASS_USE, 1.0f, 1.0f);
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        this.stopUsing(entity);
        return itemStack;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        this.stopUsing(entity);
        return true;
    }

    private void stopUsing(LivingEntity entity) {
        entity.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0f, 1.0f);
    }
}

