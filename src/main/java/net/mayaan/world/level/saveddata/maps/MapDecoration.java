/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.saveddata.maps;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.Identifier;
import net.mayaan.world.level.saveddata.maps.MapDecorationType;

public record MapDecoration(Holder<MapDecorationType> type, byte x, byte y, byte rot, Optional<Component> name) {
    public static final StreamCodec<RegistryFriendlyByteBuf, MapDecoration> STREAM_CODEC = StreamCodec.composite(MapDecorationType.STREAM_CODEC, MapDecoration::type, ByteBufCodecs.BYTE, MapDecoration::x, ByteBufCodecs.BYTE, MapDecoration::y, ByteBufCodecs.BYTE, MapDecoration::rot, ComponentSerialization.OPTIONAL_STREAM_CODEC, MapDecoration::name, MapDecoration::new);

    public MapDecoration {
        rot = (byte)(rot & 0xF);
    }

    public Identifier getSpriteLocation() {
        return this.type.value().assetId();
    }

    public boolean renderOnFrame() {
        return this.type.value().showOnItemFrame();
    }
}

