/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.font;

import net.mayaan.client.gui.GlyphSource;
import net.mayaan.client.gui.font.glyphs.BakedGlyph;
import net.mayaan.util.RandomSource;

public record SingleSpriteSource(BakedGlyph glyph) implements GlyphSource
{
    @Override
    public BakedGlyph getGlyph(int codepoint) {
        return this.glyph;
    }

    @Override
    public BakedGlyph getRandomGlyph(RandomSource random, int width) {
        return this.glyph;
    }
}

