/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface RangeSelectItemModelProperty {
    public float get(ItemStack var1, @Nullable ClientLevel var2, @Nullable ItemOwner var3, int var4);

    public MapCodec<? extends RangeSelectItemModelProperty> type();
}

