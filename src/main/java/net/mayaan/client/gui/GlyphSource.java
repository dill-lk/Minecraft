/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui;

import net.mayaan.client.gui.font.glyphs.BakedGlyph;
import net.mayaan.util.RandomSource;

public interface GlyphSource {
    public BakedGlyph getGlyph(int var1);

    public BakedGlyph getRandomGlyph(RandomSource var1, int var2);
}

