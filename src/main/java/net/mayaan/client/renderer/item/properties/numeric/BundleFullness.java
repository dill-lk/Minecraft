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
import net.mayaan.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.item.BundleItem;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record BundleFullness() implements RangeSelectItemModelProperty
{
    public static final MapCodec<BundleFullness> MAP_CODEC = MapCodec.unit((Object)new BundleFullness());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return BundleItem.getFullnessDisplay(itemStack);
    }

    public MapCodec<BundleFullness> type() {
        return MAP_CODEC;
    }
}

