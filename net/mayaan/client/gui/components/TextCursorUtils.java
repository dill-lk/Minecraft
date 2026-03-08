/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;

public class TextCursorUtils {
    public static final int CURSOR_INSERT_WIDTH = 1;
    private static final String CURSOR_APPEND_CHARACTER = "_";
    private static final int CURSOR_BLINK_INTERVAL_MS = 300;

    public static void drawInsertCursor(GuiGraphics graphics, int x, int y, int color, int lineHeight) {
        graphics.fill(x, y - 1, x + 1, y + lineHeight, color);
    }

    public static void drawAppendCursor(GuiGraphics graphics, Font font, int x, int y, int color, boolean shadow) {
        graphics.drawString(font, CURSOR_APPEND_CHARACTER, x, y, color, shadow);
    }

    public static boolean isCursorVisible(long timeInMs) {
        return timeInMs / 300L % 2L == 0L;
    }
}

