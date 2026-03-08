/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.font.glyphs;

import com.maayanlabs.blaze3d.font.GlyphInfo;
import com.maayanlabs.blaze3d.font.UnbakedGlyph;
import java.util.Objects;
import net.mayaan.client.gui.font.TextRenderable;
import net.mayaan.client.gui.font.glyphs.BakedGlyph;
import net.mayaan.network.chat.Style;
import org.jspecify.annotations.Nullable;

public class EmptyGlyph
implements UnbakedGlyph {
    private final GlyphInfo info;

    public EmptyGlyph(float advance) {
        this.info = GlyphInfo.simple(advance);
    }

    @Override
    public GlyphInfo info() {
        return this.info;
    }

    @Override
    public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
        return new BakedGlyph(this){
            final /* synthetic */ EmptyGlyph this$0;
            {
                EmptyGlyph emptyGlyph = this$0;
                Objects.requireNonNull(emptyGlyph);
                this.this$0 = emptyGlyph;
            }

            @Override
            public GlyphInfo info() {
                return this.this$0.info;
            }

            @Override
            public @Nullable TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
                return null;
            }
        };
    }
}

