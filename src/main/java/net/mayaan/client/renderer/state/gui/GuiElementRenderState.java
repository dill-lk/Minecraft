/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state.gui;

import com.maayanlabs.blaze3d.pipeline.RenderPipeline;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.gui.render.TextureSetup;
import net.mayaan.client.renderer.state.gui.ScreenArea;
import org.jspecify.annotations.Nullable;

public interface GuiElementRenderState
extends ScreenArea {
    public void buildVertices(VertexConsumer var1);

    public RenderPipeline pipeline();

    public TextureSetup textureSetup();

    public @Nullable ScreenRectangle scissorArea();
}

