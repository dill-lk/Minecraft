/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.OptionalDispenseItemBehavior;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.Shearable;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.BeehiveBlock;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.level.block.entity.BeehiveBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.AABB;

public class ShearsDispenseItemBehavior
extends OptionalDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack dispensed) {
        ServerLevel level = source.level();
        if (!level.isClientSide()) {
            BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
            this.setSuccess(ShearsDispenseItemBehavior.tryShearBeehive(level, dispensed, pos) || ShearsDispenseItemBehavior.tryShearEntity(level, pos, dispensed));
            if (this.isSuccess()) {
                dispensed.hurtAndBreak(1, level, null, item -> {});
            }
        }
        return dispensed;
    }

    private static boolean tryShearBeehive(ServerLevel level, ItemStack tool, BlockPos pos) {
        int honeyLevel;
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.BEEHIVES, s -> s.hasProperty(BeehiveBlock.HONEY_LEVEL) && s.getBlock() instanceof BeehiveBlock) && (honeyLevel = state.getValue(BeehiveBlock.HONEY_LEVEL).intValue()) >= 5) {
            level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0f, 1.0f);
            BeehiveBlock.dropHoneycomb(level, tool, state, level.getBlockEntity(pos), null, pos);
            ((BeehiveBlock)state.getBlock()).releaseBeesAndResetHoneyLevel(level, state, pos, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
            level.gameEvent(null, GameEvent.SHEAR, pos);
            return true;
        }
        return false;
    }

    private static boolean tryShearEntity(ServerLevel level, BlockPos pos, ItemStack tool) {
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
        for (Entity entity : entities) {
            Shearable shearable;
            if (entity.shearOffAllLeashConnections(null)) {
                return true;
            }
            if (!(entity instanceof Shearable) || !(shearable = (Shearable)((Object)entity)).readyForShearing()) continue;
            shearable.shear(level, SoundSource.BLOCKS, tool);
            level.gameEvent(null, GameEvent.SHEAR, pos);
            return true;
        }
        return false;
    }
}

