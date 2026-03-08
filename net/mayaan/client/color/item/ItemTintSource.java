/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.color.item;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface ItemTintSource {
    public int calculate(ItemStack var1, @Nullable ClientLevel var2, @Nullable LivingEntity var3);

    public MapCodec<? extends ItemTintSource> type();
}

