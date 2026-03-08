/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.font.glyphs;

import com.maayanlabs.blaze3d.font.GlyphInfo;
import net.mayaan.client.gui.font.TextRenderable;
import net.mayaan.network.chat.Style;
import org.jspecify.annotations.Nullable;

public interface BakedGlyph {
    public GlyphInfo info();

    public @Nullable TextRenderable.Styled createGlyph(float var1, float var2, int var3, int var4, Style var5, float var6, float var7);
}

