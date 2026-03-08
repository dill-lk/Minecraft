/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class MapRenderState {
    public @Nullable Identifier texture;
    public final List<MapDecorationRenderState> decorations = new ArrayList<MapDecorationRenderState>();

    public static class MapDecorationRenderState {
        public @Nullable TextureAtlasSprite atlasSprite;
        public byte x;
        public byte y;
        public byte rot;
        public boolean renderOnFrame;
        public @Nullable Component name;
    }
}

