/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world;

import com.mojang.serialization.Codec;
import net.mayaan.advancements.criterion.ItemPredicate;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public record LockCode(ItemPredicate predicate) {
    public static final LockCode NO_LOCK = new LockCode(ItemPredicate.Builder.item().build());
    public static final Codec<LockCode> CODEC = ItemPredicate.CODEC.xmap(LockCode::new, LockCode::predicate);
    public static final String TAG_LOCK = "lock";

    public boolean unlocksWith(ItemStack itemStack) {
        return this.predicate.test(itemStack);
    }

    public void addToTag(ValueOutput parent) {
        if (this != NO_LOCK) {
            parent.store(TAG_LOCK, CODEC, this);
        }
    }

    public boolean canUnlock(Player player) {
        return player.isSpectator() || this.unlocksWith(player.getMainHandItem());
    }

    public static LockCode fromTag(ValueInput parent) {
        return parent.read(TAG_LOCK, CODEC).orElse(NO_LOCK);
    }
}

