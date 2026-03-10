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
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record ContextEntityType() implements SelectItemModelProperty<ResourceKey<EntityType<?>>>
{
    public static final Codec<ResourceKey<EntityType<?>>> VALUE_CODEC = ResourceKey.codec(Registries.ENTITY_TYPE);
    public static final SelectItemModelProperty.Type<ContextEntityType, ResourceKey<EntityType<?>>> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit((Object)new ContextEntityType()), VALUE_CODEC);

    @Override
    public @Nullable ResourceKey<EntityType<?>> get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return owner == null ? null : (ResourceKey)owner.typeHolder().unwrapKey().orElse(null);
    }

    @Override
    public SelectItemModelProperty.Type<ContextEntityType, ResourceKey<EntityType<?>>> type() {
        return TYPE;
    }

    @Override
    public Codec<ResourceKey<EntityType<?>>> valueCodec() {
        return VALUE_CODEC;
    }
}

