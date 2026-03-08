/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jspecify.annotations.Nullable;

public interface SubmitNodeCollector
extends OrderedSubmitNodeCollector {
    public OrderedSubmitNodeCollector order(int var1);

    public static interface ParticleGroupRenderer {
        public boolean isEmpty();

        public  @Nullable QuadParticleRenderState.PreparedBuffers prepare(ParticleFeatureRenderer.ParticleBufferCache var1, boolean var2);

        public void render(QuadParticleRenderState.PreparedBuffers var1, ParticleFeatureRenderer.ParticleBufferCache var2, RenderPass var3, TextureManager var4);
    }

    public static interface CustomGeometryRenderer {
        public void render(PoseStack.Pose var1, VertexConsumer var2);
    }
}

