/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.List;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Leashable;
import net.mayaan.world.entity.decoration.LeashFenceKnotEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.Vec3;

public class LeadItem
extends Item {
    public LeadItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos;
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos = context.getClickedPos());
        if (state.is(BlockTags.FENCES)) {
            Player player = context.getPlayer();
            if (!level.isClientSide() && player != null) {
                return LeadItem.bindPlayerMobs(player, level, pos);
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult bindPlayerMobs(Player player, Level level, BlockPos pos) {
        List<Leashable> entitiesToLeash = Leashable.leashableInArea(level, Vec3.atCenterOf(pos), l -> l.getLeashHolder() == player);
        if (entitiesToLeash.isEmpty()) {
            return InteractionResult.PASS;
        }
        Optional<LeashFenceKnotEntity> existingKnot = LeashFenceKnotEntity.getKnot(level, pos);
        LeashFenceKnotEntity activeKnot = existingKnot.orElseGet(() -> LeashFenceKnotEntity.createKnot(level, pos));
        boolean anyLeashed = false;
        for (Leashable leashable : entitiesToLeash) {
            if (!leashable.canHaveALeashAttachedTo(activeKnot)) continue;
            leashable.setLeashedTo(activeKnot, true);
            anyLeashed = true;
        }
        if (anyLeashed) {
            activeKnot.playPlacementSound();
            level.gameEvent(GameEvent.BLOCK_ATTACH, pos, GameEvent.Context.of(player));
            return InteractionResult.SUCCESS_SERVER;
        }
        if (existingKnot.isEmpty()) {
            activeKnot.discard();
        }
        return InteractionResult.PASS;
    }
}

