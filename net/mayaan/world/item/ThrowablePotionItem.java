/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.Direction;
import net.mayaan.core.Position;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.PotionItem;
import net.mayaan.world.item.ProjectileItem;
import net.mayaan.world.level.Level;

public abstract class ThrowablePotionItem
extends PotionItem
implements ProjectileItem {
    public static final float PROJECTILE_SHOOT_POWER = 0.5f;

    public ThrowablePotionItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Projectile.spawnProjectileFromRotation(this::createPotion, serverLevel, itemStack, player, -20.0f, 0.5f, 1.0f);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    protected abstract AbstractThrownPotion createPotion(ServerLevel var1, LivingEntity var2, ItemStack var3);

    protected abstract AbstractThrownPotion createPotion(Level var1, Position var2, ItemStack var3);

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return this.createPotion(level, position, itemStack);
    }

    @Override
    public ProjectileItem.DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder().uncertainty(ProjectileItem.DispenseConfig.DEFAULT.uncertainty() * 0.5f).power(ProjectileItem.DispenseConfig.DEFAULT.power() * 1.25f).build();
    }
}

