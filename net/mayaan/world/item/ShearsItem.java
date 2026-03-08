/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.List;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderSet;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.Tool;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.GrowingPlantHeadBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;

public class ShearsItem
extends Item {
    public ShearsItem(Item.Properties properties) {
        super(properties);
    }

    public static Tool createToolProperties() {
        HolderGetter<Block> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(List.of(Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0f), Tool.Rule.overrideSpeed(registrationLookup.getOrThrow(BlockTags.LEAVES), 15.0f), Tool.Rule.overrideSpeed(registrationLookup.getOrThrow(BlockTags.WOOL), 5.0f), Tool.Rule.overrideSpeed(HolderSet.direct(Blocks.VINE.builtInRegistryHolder(), Blocks.GLOW_LICHEN.builtInRegistryHolder()), 2.0f)), 1.0f, 1, true);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        Tool tool = itemStack.get(DataComponents.TOOL);
        if (tool == null) {
            return false;
        }
        if (!level.isClientSide() && !state.is(BlockTags.FIRE) && tool.damagePerBlock() > 0) {
            itemStack.hurtAndBreak(tool.damagePerBlock(), miner, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        GrowingPlantHeadBlock plantBlock;
        BlockPos pos;
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos = context.getClickedPos());
        Block block = state.getBlock();
        if (block instanceof GrowingPlantHeadBlock && !(plantBlock = (GrowingPlantHeadBlock)block).isMaxAge(state)) {
            Player player = context.getPlayer();
            ItemStack itemInHand = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemInHand);
            }
            level.playSound((Entity)player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0f, 1.0f);
            BlockState newState = plantBlock.getMaxAgeState(state);
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(context.getPlayer(), newState));
            if (player != null) {
                itemInHand.hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}

