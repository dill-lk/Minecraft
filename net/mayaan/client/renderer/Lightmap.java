/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer;

import com.maayanlabs.blaze3d.buffers.GpuBuffer;
import com.maayanlabs.blaze3d.buffers.Std140Builder;
import com.maayanlabs.blaze3d.buffers.Std140SizeCalculator;
import com.maayanlabs.blaze3d.systems.CommandEncoder;
import com.maayanlabs.blaze3d.systems.GpuDevice;
import com.maayanlabs.blaze3d.systems.RenderPass;
import com.maayanlabs.blaze3d.systems.RenderSystem;
import com.maayanlabs.blaze3d.textures.GpuTexture;
import com.maayanlabs.blaze3d.textures.GpuTextureView;
import com.maayanlabs.blaze3d.textures.TextureFormat;
import java.util.OptionalInt;
import net.mayaan.client.renderer.MappableRingBuffer;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.renderer.state.LightmapRenderState;
import net.mayaan.util.Mth;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.level.dimension.DimensionType;

public class Lightmap
implements AutoCloseable {
    public static final int TEXTURE_SIZE = 16;
    private static final int LIGHTMAP_UBO_SIZE = new Std140SizeCalculator().putFloat().putFloat().putFloat().putFloat().putFloat().putFloat().putVec3().putVec3().putVec3().putVec3().get();
    private final GpuTexture texture;
    private final GpuTextureView textureView;
    private final MappableRingBuffer ubo;

    public Lightmap() {
        GpuDevice device = RenderSystem.getDevice();
        this.texture = device.createTexture("Lightmap", 13, TextureFormat.RGBA8, 16, 16, 1, 1);
        this.textureView = device.createTextureView(this.texture);
        device.createCommandEncoder().clearColorTexture(this.texture, -1);
        this.ubo = new MappableRingBuffer(() -> "Lightmap UBO", 130, LIGHTMAP_UBO_SIZE);
    }

    public GpuTextureView getTextureView() {
        return this.textureView;
    }

    @Override
    public void close() {
        this.texture.close();
        this.textureView.close();
        this.ubo.close();
    }

    public void render(LightmapRenderState renderState) {
        if (!renderState.needsUpdate) {
            return;
        }
        ProfilerFiller profiler = Profiler.get();
        profiler.push("lightmap");
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
        try (GpuBuffer.MappedView view = commandEncoder.mapBuffer(this.ubo.currentBuffer(), false, true);){
            Std140Builder.intoBuffer(view.data()).putFloat(renderState.skyFactor).putFloat(renderState.blockFactor).putFloat(renderState.nightVisionEffectIntensity).putFloat(renderState.darknessEffectScale).putFloat(renderState.bossOverlayWorldDarkening).putFloat(renderState.brightness).putVec3(renderState.blockLightTint).putVec3(renderState.skyLightColor).putVec3(renderState.ambientColor).putVec3(renderState.nightVisionColor);
        }
        try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.textureView, OptionalInt.empty());){
            renderPass.setPipeline(RenderPipelines.LIGHTMAP);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("LightmapInfo", this.ubo.currentBuffer());
            renderPass.draw(0, 3);
        }
        this.ubo.rotate();
        profiler.pop();
    }

    public static float getBrightness(DimensionType dimensionType, int level) {
        float v = (float)level / 15.0f;
        float curvedV = v / (4.0f - 3.0f * v);
        return Mth.lerp(dimensionType.ambientLight(), curvedV, 1.0f);
    }
}

