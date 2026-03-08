/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 */
package net.mayaan.client.gui.font;

import com.maayanlabs.blaze3d.pipeline.RenderPipeline;
import net.mayaan.client.gui.Font;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.resources.Identifier;

public record GlyphRenderTypes(RenderType normal, RenderType seeThrough, RenderType polygonOffset, RenderPipeline guiPipeline) {
    public static GlyphRenderTypes createForIntensityTexture(Identifier name) {
        return new GlyphRenderTypes(RenderTypes.textIntensity(name), RenderTypes.textIntensitySeeThrough(name), RenderTypes.textIntensityPolygonOffset(name), RenderPipelines.GUI_TEXT_INTENSITY);
    }

    public static GlyphRenderTypes createForColorTexture(Identifier name) {
        return new GlyphRenderTypes(RenderTypes.text(name), RenderTypes.textSeeThrough(name), RenderTypes.textPolygonOffset(name), RenderPipelines.GUI_TEXT);
    }

    public RenderType select(Font.DisplayMode mode) {
        return switch (mode) {
            default -> throw new MatchException(null, null);
            case Font.DisplayMode.NORMAL -> this.normal;
            case Font.DisplayMode.SEE_THROUGH -> this.seeThrough;
            case Font.DisplayMode.POLYGON_OFFSET -> this.polygonOffset;
        };
    }
}

