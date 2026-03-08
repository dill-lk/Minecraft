/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.decoration;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.decoration.ItemFrame;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;

public class GlowItemFrame
extends ItemFrame {
    public GlowItemFrame(EntityType<? extends ItemFrame> type, Level level) {
        super(type, level);
    }

    public GlowItemFrame(Level level, BlockPos pos, Direction direction) {
        super(EntityType.GLOW_ITEM_FRAME, level, pos, direction);
    }

    @Override
    public SoundEvent getRemoveItemSound() {
        return SoundEvents.GLOW_ITEM_FRAME_REMOVE_ITEM;
    }

    @Override
    public SoundEvent getBreakSound() {
        return SoundEvents.GLOW_ITEM_FRAME_BREAK;
    }

    @Override
    public SoundEvent getPlaceSound() {
        return SoundEvents.GLOW_ITEM_FRAME_PLACE;
    }

    @Override
    public SoundEvent getAddItemSound() {
        return SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM;
    }

    @Override
    public SoundEvent getRotateItemSound() {
        return SoundEvents.GLOW_ITEM_FRAME_ROTATE_ITEM;
    }

    @Override
    protected ItemStack getFrameItemStack() {
        return new ItemStack(Items.GLOW_ITEM_FRAME);
    }
}

