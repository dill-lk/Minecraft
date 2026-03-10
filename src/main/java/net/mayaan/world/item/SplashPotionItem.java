/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.Position;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.mayaan.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ThrowablePotionItem;
import net.mayaan.world.level.Level;

public class SplashPotionItem
extends ThrowablePotionItem {
    public SplashPotionItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
        return super.use(level, player, hand);
    }

    @Override
    protected AbstractThrownPotion createPotion(ServerLevel level, LivingEntity owner, ItemStack itemStack) {
        return new ThrownSplashPotion(level, owner, itemStack);
    }

    @Override
    protected AbstractThrownPotion createPotion(Level level, Position position, ItemStack itemStack) {
        return new ThrownSplashPotion(level, position.x(), position.y(), position.z(), itemStack);
    }
}

