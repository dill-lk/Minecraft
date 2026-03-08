/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.List;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.tags.FluidTags;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.AreaEffectCloud;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.boss.enderdragon.EnderDragon;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemUtils;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.alchemy.PotionContents;
import net.mayaan.world.item.alchemy.Potions;
import net.mayaan.world.level.ClipContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;

public class BottleItem
extends Item {
    public BottleItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        List<AreaEffectCloud> clouds = level.getEntitiesOfClass(AreaEffectCloud.class, player.getBoundingBox().inflate(2.0), input -> input.isAlive() && input.getOwner() instanceof EnderDragon);
        ItemStack itemStack = player.getItemInHand(hand);
        if (!clouds.isEmpty()) {
            AreaEffectCloud cloud = clouds.get(0);
            cloud.setRadius(cloud.getRadius() - 0.5f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
            level.gameEvent((Entity)player, GameEvent.FLUID_PICKUP, player.position());
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, cloud);
            }
            return InteractionResult.SUCCESS.heldItemTransformedTo(this.turnBottleIntoItem(itemStack, player, new ItemStack(Items.DRAGON_BREATH)));
        }
        BlockHitResult hitResult = BottleItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            if (!level.mayInteract(player, pos)) {
                return InteractionResult.PASS;
            }
            if (level.getFluidState(pos).is(FluidTags.WATER)) {
                level.playSound((Entity)player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0f, 1.0f);
                level.gameEvent((Entity)player, GameEvent.FLUID_PICKUP, pos);
                return InteractionResult.SUCCESS.heldItemTransformedTo(this.turnBottleIntoItem(itemStack, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
            }
        }
        return InteractionResult.PASS;
    }

    protected ItemStack turnBottleIntoItem(ItemStack itemStack, Player player, ItemStack itemStackToTurnInto) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(itemStack, player, itemStackToTurnInto);
    }
}

