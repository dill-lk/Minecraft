/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.FishingHook;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gameevent.GameEvent;

public class FishingRodItem
extends Item {
    public FishingRodItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.fishing != null) {
            if (!level.isClientSide()) {
                int dmg = player.fishing.retrieve(itemStack);
                itemStack.hurtAndBreak(dmg, (LivingEntity)player, hand.asEquipmentSlot());
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
            itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                int lureSpeed = (int)(EnchantmentHelper.getFishingTimeReduction(serverLevel, itemStack, player) * 20.0f);
                int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, itemStack, player);
                Projectile.spawnProjectile(new FishingHook(player, level, luck, lureSpeed), serverLevel, itemStack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
        }
        return InteractionResult.SUCCESS;
    }
}

