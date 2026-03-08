/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.tags.StructureTags;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.EyeOfEnder;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.ClipContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.EndPortalFrameBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.pattern.BlockPattern;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class EnderEyeItem
extends Item {
    public EnderEyeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos;
        Level level = context.getLevel();
        BlockState targetState = level.getBlockState(pos = context.getClickedPos());
        if (!targetState.is(Blocks.END_PORTAL_FRAME) || targetState.getValue(EndPortalFrameBlock.HAS_EYE).booleanValue()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        BlockState newState = (BlockState)targetState.setValue(EndPortalFrameBlock.HAS_EYE, true);
        Block.pushEntitiesUp(targetState, newState, level, pos);
        level.setBlock(pos, newState, 2);
        level.updateNeighbourForOutputSignal(pos, Blocks.END_PORTAL_FRAME);
        context.getItemInHand().shrink(1);
        level.levelEvent(1503, pos, 0);
        BlockPattern.BlockPatternMatch match = EndPortalFrameBlock.getOrCreatePortalShape().find(level, pos);
        if (match != null) {
            BlockPos blockPos = match.getFrontTopLeft().offset(-3, 0, -3);
            for (int x = 0; x < 3; ++x) {
                for (int z = 0; z < 3; ++z) {
                    BlockPos portalBlockPos = blockPos.offset(x, 0, z);
                    level.destroyBlock(portalBlockPos, true, null);
                    level.setBlock(portalBlockPos, Blocks.END_PORTAL.defaultBlockState(), 2);
                }
            }
            level.globalLevelEvent(1038, blockPos.offset(1, 0, 1), 0);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 0;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult hitResult = EnderEyeItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.BLOCK && level.getBlockState(hitResult.getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
            return InteractionResult.PASS;
        }
        player.startUsingItem(hand);
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            BlockPos nearestMapFeature = serverLevel.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, player.blockPosition(), 100, false);
            if (nearestMapFeature == null) {
                return InteractionResult.CONSUME;
            }
            EyeOfEnder eyeOfEnder = new EyeOfEnder(level, player.getX(), player.getY(0.5), player.getZ());
            eyeOfEnder.setItem(itemStack);
            eyeOfEnder.signalTo(Vec3.atLowerCornerOf(nearestMapFeature));
            level.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeOfEnder.position(), GameEvent.Context.of(player));
            level.addFreshEntity(eyeOfEnder);
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                CriteriaTriggers.USED_ENDER_EYE.trigger(serverPlayer, nearestMapFeature);
            }
            float pitch = Mth.lerp(level.getRandom().nextFloat(), 0.33f, 0.5f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0f, pitch);
            itemStack.consume(1, player);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResult.SUCCESS_SERVER;
    }
}

