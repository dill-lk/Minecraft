/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package net.mayaan.client.gui.render.pip;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import net.mayaan.client.gui.render.pip.PictureInPictureRenderer;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.gui.pip.GuiProfilerChartRenderState;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.profiling.ResultField;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class GuiProfilerChartRenderer
extends PictureInPictureRenderer<GuiProfilerChartRenderState> {
    public GuiProfilerChartRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public Class<GuiProfilerChartRenderState> getRenderStateClass() {
        return GuiProfilerChartRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiProfilerChartRenderState chartState, PoseStack poseStack) {
        double totalPercentage = 0.0;
        poseStack.translate(0.0f, -5.0f, 0.0f);
        Matrix4f pose = poseStack.last().pose();
        for (ResultField result : chartState.chartData()) {
            int j;
            int steps = Mth.floor(result.percentage / 4.0) + 1;
            VertexConsumer buffer = this.bufferSource.getBuffer(RenderTypes.debugTriangleFan());
            int color = ARGB.opaque(result.getColor());
            int shadeColor = ARGB.multiply(color, -8355712);
            buffer.addVertex((Matrix4fc)pose, 0.0f, 0.0f, 0.0f).setColor(color);
            for (j = steps; j >= 0; --j) {
                float dir = (float)((totalPercentage + result.percentage * (double)j / (double)steps) * 6.2831854820251465 / 100.0);
                float xx = Mth.sin(dir) * 105.0f;
                float yy = Mth.cos(dir) * 105.0f * 0.5f;
                buffer.addVertex((Matrix4fc)pose, xx, yy, 0.0f).setColor(color);
            }
            buffer = this.bufferSource.getBuffer(RenderTypes.debugQuads());
            for (j = steps; j > 0; --j) {
                float dir0 = (float)((totalPercentage + result.percentage * (double)j / (double)steps) * 6.2831854820251465 / 100.0);
                float x0 = Mth.sin(dir0) * 105.0f;
                float y0 = Mth.cos(dir0) * 105.0f * 0.5f;
                float dir1 = (float)((totalPercentage + result.percentage * (double)(j - 1) / (double)steps) * 6.2831854820251465 / 100.0);
                float x1 = Mth.sin(dir1) * 105.0f;
                float y1 = Mth.cos(dir1) * 105.0f * 0.5f;
                if ((y0 + y1) / 2.0f < 0.0f) continue;
                buffer.addVertex((Matrix4fc)pose, x0, y0, 0.0f).setColor(shadeColor);
                buffer.addVertex((Matrix4fc)pose, x0, y0 + 10.0f, 0.0f).setColor(shadeColor);
                buffer.addVertex((Matrix4fc)pose, x1, y1 + 10.0f, 0.0f).setColor(shadeColor);
                buffer.addVertex((Matrix4fc)pose, x1, y1, 0.0f).setColor(shadeColor);
            }
            totalPercentage += result.percentage;
        }
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return (float)height / 2.0f;
    }

    @Override
    protected String getTextureLabel() {
        return "profiler chart";
    }
}

