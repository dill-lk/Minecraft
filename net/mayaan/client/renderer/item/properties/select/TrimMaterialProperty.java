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
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.equipment.trim.ArmorTrim;
import net.mayaan.world.item.equipment.trim.TrimMaterial;
import org.jspecify.annotations.Nullable;

public record TrimMaterialProperty() implements SelectItemModelProperty<ResourceKey<TrimMaterial>>
{
    public static final Codec<ResourceKey<TrimMaterial>> VALUE_CODEC = ResourceKey.codec(Registries.TRIM_MATERIAL);
    public static final SelectItemModelProperty.Type<TrimMaterialProperty, ResourceKey<TrimMaterial>> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit((Object)new TrimMaterialProperty()), VALUE_CODEC);

    @Override
    public @Nullable ResourceKey<TrimMaterial> get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        ArmorTrim trim = itemStack.get(DataComponents.TRIM);
        if (trim == null) {
            return null;
        }
        return trim.material().unwrapKey().orElse(null);
    }

    @Override
    public SelectItemModelProperty.Type<TrimMaterialProperty, ResourceKey<TrimMaterial>> type() {
        return TYPE;
    }

    @Override
    public Codec<ResourceKey<TrimMaterial>> valueCodec() {
        return VALUE_CODEC;
    }
}

