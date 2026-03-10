/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.util.RandomSource;

public interface SpriteSet {
    public TextureAtlasSprite get(int var1, int var2);

    public TextureAtlasSprite get(RandomSource var1);

    public TextureAtlasSprite first();
}

