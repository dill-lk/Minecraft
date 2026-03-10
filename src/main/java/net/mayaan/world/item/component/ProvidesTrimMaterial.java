/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item.component;

import com.mojang.serialization.Codec;
import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.equipment.trim.TrimMaterial;

public record ProvidesTrimMaterial(Holder<TrimMaterial> material) {
    public static final Codec<ProvidesTrimMaterial> CODEC = TrimMaterial.CODEC.xmap(ProvidesTrimMaterial::new, ProvidesTrimMaterial::material);
    public static final StreamCodec<RegistryFriendlyByteBuf, ProvidesTrimMaterial> STREAM_CODEC = TrimMaterial.STREAM_CODEC.map(ProvidesTrimMaterial::new, ProvidesTrimMaterial::material);
}

