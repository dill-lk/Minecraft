/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 */
package net.mayaan.client.gui.font;

import com.maayanlabs.blaze3d.pipeline.RenderPipeline;
import com.maayanlabs.blaze3d.textures.GpuTextureView;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.font.ActiveArea;
import net.mayaan.client.renderer.rendertype.RenderType;
import org.joml.Matrix4f;

public interface TextRenderable {
    public void render(Matrix4f var1, VertexConsumer var2, int var3, boolean var4);

    public RenderType renderType(Font.DisplayMode var1);

    public GpuTextureView textureView();

    public RenderPipeline guiPipeline();

    public float left();

    public float top();

    public float right();

    public float bottom();

    public static interface Styled
    extends TextRenderable,
    ActiveArea {
        @Override
        default public float activeLeft() {
            return this.left();
        }

        @Override
        default public float activeTop() {
            return this.top();
        }

        @Override
        default public float activeRight() {
            return this.right();
        }

        @Override
        default public float activeBottom() {
            return this.bottom();
        }
    }
}

