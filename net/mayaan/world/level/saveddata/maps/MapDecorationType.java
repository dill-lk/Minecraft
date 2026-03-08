/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.saveddata.maps;

import com.mojang.serialization.Codec;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.Identifier;

public record MapDecorationType(Identifier assetId, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) {
    public static final int NO_MAP_COLOR = -1;
    public static final Codec<Holder<MapDecorationType>> CODEC = BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MapDecorationType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MAP_DECORATION_TYPE);

    public boolean hasMapColor() {
        return this.mapColor != -1;
    }
}

