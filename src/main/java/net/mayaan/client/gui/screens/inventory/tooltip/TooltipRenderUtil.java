/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.inventory.tooltip;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class TooltipRenderUtil {
    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("tooltip/background");
    private static final Identifier FRAME_SPRITE = Identifier.withDefaultNamespace("tooltip/frame");
    public static final int MOUSE_OFFSET = 12;
    private static final int PADDING = 3;
    public static final int PADDING_LEFT = 3;
    public static final int PADDING_RIGHT = 3;
    public static final int PADDING_TOP = 3;
    public static final int PADDING_BOTTOM = 3;
    private static final int MARGIN = 9;

    public static void renderTooltipBackground(GuiGraphics graphics, int x, int y, int w, int h, @Nullable Identifier style) {
        int x0 = x - 3 - 9;
        int y0 = y - 3 - 9;
        int paddedWidth = w + 3 + 3 + 18;
        int paddedHeight = h + 3 + 3 + 18;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TooltipRenderUtil.getBackgroundSprite(style), x0, y0, paddedWidth, paddedHeight);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TooltipRenderUtil.getFrameSprite(style), x0, y0, paddedWidth, paddedHeight);
    }

    private static Identifier getBackgroundSprite(@Nullable Identifier style) {
        if (style == null) {
            return BACKGROUND_SPRITE;
        }
        return style.withPath(path -> "tooltip/" + path + "_background");
    }

    private static Identifier getFrameSprite(@Nullable Identifier style) {
        if (style == null) {
            return FRAME_SPRITE;
        }
        return style.withPath(path -> "tooltip/" + path + "_frame");
    }
}

