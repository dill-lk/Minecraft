/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.font;

import com.maayanlabs.blaze3d.font.GlyphBitmap;
import com.maayanlabs.blaze3d.font.GlyphInfo;
import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.gui.font.FontTexture;
import net.mayaan.client.gui.font.GlyphRenderTypes;
import net.mayaan.client.gui.font.glyphs.BakedSheetGlyph;
import net.mayaan.client.renderer.texture.TextureManager;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class GlyphStitcher
implements AutoCloseable {
    private final TextureManager textureManager;
    private final Identifier texturePrefix;
    private final List<FontTexture> textures = new ArrayList<FontTexture>();

    public GlyphStitcher(TextureManager textureManager, Identifier texturePrefix) {
        this.textureManager = textureManager;
        this.texturePrefix = texturePrefix;
    }

    public void reset() {
        int textureCount = this.textures.size();
        this.textures.clear();
        for (int i = 0; i < textureCount; ++i) {
            this.textureManager.release(this.textureName(i));
        }
    }

    @Override
    public void close() {
        this.reset();
    }

    public @Nullable BakedSheetGlyph stitch(GlyphInfo info, GlyphBitmap glyphBitmap) {
        for (FontTexture texture : this.textures) {
            BakedSheetGlyph glyph = texture.add(info, glyphBitmap);
            if (glyph == null) continue;
            return glyph;
        }
        int nextIndex = this.textures.size();
        Identifier name = this.textureName(nextIndex);
        boolean isColored = glyphBitmap.isColored();
        GlyphRenderTypes renderTypes = isColored ? GlyphRenderTypes.createForColorTexture(name) : GlyphRenderTypes.createForIntensityTexture(name);
        FontTexture texture = new FontTexture(name::toString, renderTypes, isColored);
        this.textures.add(texture);
        this.textureManager.register(name, texture);
        return texture.add(info, glyphBitmap);
    }

    private Identifier textureName(int index) {
        return this.texturePrefix.withSuffix("/" + index);
    }
}

