/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseFireBlock;
import net.mayaan.world.level.block.CampfireBlock;
import net.mayaan.world.level.block.CandleBlock;
import net.mayaan.world.level.block.CandleCakeBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.gameevent.GameEvent;

public class FlintAndSteelItem
extends Item {
    public FlintAndSteelItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos;
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos = context.getClickedPos());
        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            level.playSound((Entity)player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
            level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.LIT, true), 11);
            level.gameEvent((Entity)player, GameEvent.BLOCK_CHANGE, pos);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }
        BlockPos relativePos = pos.relative(context.getClickedFace());
        if (BaseFireBlock.canBePlacedAt(level, relativePos, context.getHorizontalDirection())) {
            level.playSound((Entity)player, relativePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
            BlockState fireState = BaseFireBlock.getState(level, relativePos);
            level.setBlock(relativePos, fireState, 11);
            level.gameEvent((Entity)player, GameEvent.BLOCK_PLACE, pos);
            ItemStack itemStack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, relativePos, itemStack);
                itemStack.hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}

