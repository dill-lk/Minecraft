/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.contents.objects;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.chat.contents.objects.AtlasSprite;
import net.mayaan.network.chat.contents.objects.ObjectInfo;
import net.mayaan.network.chat.contents.objects.PlayerSprite;
import net.mayaan.util.ExtraCodecs;

public class ObjectInfos {
    private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ObjectInfo>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
    public static final MapCodec<ObjectInfo> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, ObjectInfo::codec, "object");

    static {
        ID_MAPPER.put("atlas", AtlasSprite.MAP_CODEC);
        ID_MAPPER.put("player", PlayerSprite.MAP_CODEC);
    }
}

