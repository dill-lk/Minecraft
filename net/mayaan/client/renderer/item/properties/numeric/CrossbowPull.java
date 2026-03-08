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
import net.mayaan.client.renderer.item.properties.numeric.UseDuration;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.CrossbowItem;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class CrossbowPull
implements RangeSelectItemModelProperty {
    public static final MapCodec<CrossbowPull> MAP_CODEC = MapCodec.unit((Object)new CrossbowPull());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity entity;
        LivingEntity livingEntity = entity = owner == null ? null : owner.asLivingEntity();
        if (entity == null) {
            return 0.0f;
        }
        if (CrossbowItem.isCharged(itemStack)) {
            return 0.0f;
        }
        int chargeDuration = CrossbowItem.getChargeDuration(itemStack, entity);
        return (float)UseDuration.useDuration(itemStack, entity) / (float)chargeDuration;
    }

    public MapCodec<CrossbowPull> type() {
        return MAP_CODEC;
    }
}

