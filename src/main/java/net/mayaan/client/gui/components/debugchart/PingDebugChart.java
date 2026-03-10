/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components.debugchart;

import java.util.Locale;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.debugchart.AbstractDebugChart;
import net.mayaan.util.debugchart.SampleStorage;

public class PingDebugChart
extends AbstractDebugChart {
    private static final int CHART_TOP_VALUE = 500;

    public PingDebugChart(Font font, SampleStorage sampleStorage) {
        super(font, sampleStorage);
    }

    @Override
    protected void renderAdditionalLinesAndLabels(GuiGraphics graphics, int left, int width, int bottom) {
        this.drawStringWithShade(graphics, "500 ms", left + 1, bottom - 60 + 1);
    }

    @Override
    protected String toDisplayString(double millis) {
        return String.format(Locale.ROOT, "%d ms", (int)Math.round(millis));
    }

    @Override
    protected int getSampleHeight(double millis) {
        return (int)Math.round(millis * 60.0 / 500.0);
    }

    @Override
    protected int getSampleColor(long millis) {
        return this.getSampleColor(millis, 0.0, -16711936, 250.0, -256, 500.0, -65536);
    }
}

