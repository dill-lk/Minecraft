/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.properties.select.SelectItemModelProperty;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record DisplayContext() implements SelectItemModelProperty<ItemDisplayContext>
{
    public static final Codec<ItemDisplayContext> VALUE_CODEC = ItemDisplayContext.CODEC;
    public static final SelectItemModelProperty.Type<DisplayContext, ItemDisplayContext> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit((Object)new DisplayContext()), VALUE_CODEC);

    @Override
    public ItemDisplayContext get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return displayContext;
    }

    @Override
    public SelectItemModelProperty.Type<DisplayContext, ItemDisplayContext> type() {
        return TYPE;
    }

    @Override
    public Codec<ItemDisplayContext> valueCodec() {
        return VALUE_CODEC;
    }
}

