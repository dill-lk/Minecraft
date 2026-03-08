/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.client.renderer.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.mayaan.client.renderer.texture.atlas.SpriteSource;
import net.mayaan.client.renderer.texture.atlas.sources.DirectoryLister;
import net.mayaan.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.mayaan.client.renderer.texture.atlas.sources.SingleFile;
import net.mayaan.client.renderer.texture.atlas.sources.SourceFilter;
import net.mayaan.client.renderer.texture.atlas.sources.Unstitcher;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ExtraCodecs;

public class SpriteSources {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpriteSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
    public static final Codec<SpriteSource> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(SpriteSource::codec, c -> c);
    public static final Codec<List<SpriteSource>> FILE_CODEC = CODEC.listOf().fieldOf("sources").codec();

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.withDefaultNamespace("single"), SingleFile.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("directory"), DirectoryLister.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("filter"), SourceFilter.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("unstitch"), Unstitcher.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("paletted_permutations"), PalettedPermutations.MAP_CODEC);
    }
}

