/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.contents.data;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.chat.contents.data.BlockDataSource;
import net.mayaan.network.chat.contents.data.DataSource;
import net.mayaan.network.chat.contents.data.EntityDataSource;
import net.mayaan.network.chat.contents.data.StorageDataSource;
import net.mayaan.util.ExtraCodecs;

public class DataSources {
    private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends DataSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
    public static final MapCodec<DataSource> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, DataSource::codec, "source");

    static {
        ID_MAPPER.put("entity", EntityDataSource.MAP_CODEC);
        ID_MAPPER.put("block", BlockDataSource.MAP_CODEC);
        ID_MAPPER.put("storage", StorageDataSource.MAP_CODEC);
    }
}

