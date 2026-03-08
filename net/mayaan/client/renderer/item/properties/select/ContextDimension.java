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
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public record ContextDimension() implements SelectItemModelProperty<ResourceKey<Level>>
{
    public static final Codec<ResourceKey<Level>> VALUE_CODEC = ResourceKey.codec(Registries.DIMENSION);
    public static final SelectItemModelProperty.Type<ContextDimension, ResourceKey<Level>> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit((Object)new ContextDimension()), VALUE_CODEC);

    @Override
    public @Nullable ResourceKey<Level> get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return level != null ? level.dimension() : null;
    }

    @Override
    public SelectItemModelProperty.Type<ContextDimension, ResourceKey<Level>> type() {
        return TYPE;
    }

    @Override
    public Codec<ResourceKey<Level>> valueCodec() {
        return VALUE_CODEC;
    }
}

