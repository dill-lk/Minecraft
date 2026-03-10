/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.BucketItem;
import net.mayaan.world.item.DispensibleContainerItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class SolidBucketItem
extends BlockItem
implements DispensibleContainerItem {
    private final SoundEvent placeSound;

    public SolidBucketItem(Block content, SoundEvent placeSound, Item.Properties properties) {
        super(content, properties);
        this.placeSound = placeSound;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult placeResult = super.useOn(context);
        Player player = context.getPlayer();
        if (placeResult.consumesAction() && player != null) {
            player.setItemInHand(context.getHand(), BucketItem.getEmptySuccessItem(context.getItemInHand(), player));
        }
        return placeResult;
    }

    @Override
    protected SoundEvent getPlaceSound(BlockState blockState) {
        return this.placeSound;
    }

    @Override
    public boolean emptyContents(@Nullable LivingEntity user, Level level, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (level.isInWorldBounds(pos) && level.isEmptyBlock(pos)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, this.getBlock().defaultBlockState(), 3);
            }
            level.gameEvent((Entity)user, GameEvent.FLUID_PLACE, pos);
            level.playSound((Entity)user, pos, this.placeSound, SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }
        return false;
    }
}

