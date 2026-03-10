/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.SignApplicator;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.entity.SignBlockEntity;

public class GlowInkSacItem
extends Item
implements SignApplicator {
    public GlowInkSacItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player) {
        if (sign.updateText(text -> text.setHasGlowingText(true), isFrontText)) {
            level.playSound(null, sign.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }
        return false;
    }
}

