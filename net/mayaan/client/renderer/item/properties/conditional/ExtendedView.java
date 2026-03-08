/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.Mayaan;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record ExtendedView() implements ConditionalItemModelProperty
{
    public static final MapCodec<ExtendedView> MAP_CODEC = MapCodec.unit((Object)new ExtendedView());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.GUI && Mayaan.getInstance().hasShiftDown();
    }

    public MapCodec<ExtendedView> type() {
        return MAP_CODEC;
    }
}

