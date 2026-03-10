/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record IsUsingItem() implements ConditionalItemModelProperty
{
    public static final MapCodec<IsUsingItem> MAP_CODEC = MapCodec.unit((Object)new IsUsingItem());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        if (owner == null) {
            return false;
        }
        return owner.isUsingItem() && owner.getUseItem() == itemStack;
    }

    public MapCodec<IsUsingItem> type() {
        return MAP_CODEC;
    }
}

