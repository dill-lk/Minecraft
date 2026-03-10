/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  org.joml.Vector4f
 *  org.joml.Vector4fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state.level;

import com.maayanlabs.blaze3d.buffers.GpuBufferSlice;
import com.maayanlabs.blaze3d.systems.RenderPass;
import com.maayanlabs.blaze3d.systems.RenderSystem;
import com.maayanlabs.blaze3d.vertex.BufferBuilder;
import com.maayanlabs.blaze3d.vertex.ByteBufferBuilder;
import com.maayanlabs.blaze3d.vertex.DefaultVertexFormat;
import com.maayanlabs.blaze3d.vertex.MeshData;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import com.maayanlabs.blaze3d.vertex.VertexFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.mayaan.client.particle.SingleQuadParticle;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.feature.ParticleFeatureRenderer;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.state.level.ParticleGroupRenderState;
import net.mayaan.client.renderer.texture.AbstractTexture;
import net.mayaan.client.renderer.texture.TextureManager;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;

public class QuadParticleRenderState
implements ParticleGroupRenderState,
SubmitNodeCollector.ParticleGroupRenderer {
    private static final int INITIAL_PARTICLE_CAPACITY = 1024;
    private static final int FLOATS_PER_PARTICLE = 12;
    private static final int INTS_PER_PARTICLE = 2;
    private final Map<SingleQuadParticle.Layer, Storage> particles = new HashMap<SingleQuadParticle.Layer, Storage>();
    private int particleCount;

    public void add(SingleQuadParticle.Layer layer, float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
        this.particles.computeIfAbsent(layer, ignored -> new Storage()).add(x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords);
        ++this.particleCount;
    }

    @Override
    public void clear() {
        this.particles.values().forEach(Storage::clear);
        this.particleCount = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.particleCount == 0;
    }

    @Override
    public @Nullable PreparedBuffers prepare(ParticleFeatureRenderer.ParticleBufferCache cachedBuffer, boolean translucent) {
        if (this.isEmpty()) {
            return null;
        }
        int vertexCount = this.particleCount * 4;
        try (ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(vertexCount * DefaultVertexFormat.PARTICLE.getVertexSize());){
            BufferBuilder bufferBuilder = new BufferBuilder(builder, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
            HashMap<SingleQuadParticle.Layer, PreparedLayer> preparedLayers = new HashMap<SingleQuadParticle.Layer, PreparedLayer>();
            int offset = 0;
            for (Map.Entry<SingleQuadParticle.Layer, Storage> entry : this.particles.entrySet()) {
                if (entry.getKey().translucent() != translucent) continue;
                entry.getValue().forEachParticle((x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords) -> this.renderRotatedQuad(bufferBuilder, x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords));
                if (entry.getValue().count() > 0) {
                    preparedLayers.put(entry.getKey(), new PreparedLayer(offset, entry.getValue().count() * 6));
                }
                offset += entry.getValue().count() * 4;
            }
            MeshData mesh = bufferBuilder.build();
            if (mesh != null) {
                cachedBuffer.write(mesh.vertexBuffer());
                RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS).getBuffer(mesh.drawState().indexCount());
                GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
                PreparedBuffers preparedBuffers = new PreparedBuffers(mesh.drawState().indexCount(), dynamicTransforms, preparedLayers);
                return preparedBuffers;
            }
            PreparedBuffers preparedBuffers = null;
            return preparedBuffers;
        }
    }

    @Override
    public void render(PreparedBuffers preparedBuffers, ParticleFeatureRenderer.ParticleBufferCache bufferCache, RenderPass renderPass, TextureManager textureManager) {
        RenderSystem.AutoStorageIndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        renderPass.setVertexBuffer(0, bufferCache.get());
        renderPass.setIndexBuffer(indexBuffer.getBuffer(preparedBuffers.indexCount), indexBuffer.type());
        renderPass.setUniform("DynamicTransforms", preparedBuffers.dynamicTransforms);
        for (Map.Entry<SingleQuadParticle.Layer, PreparedLayer> entry : preparedBuffers.layers.entrySet()) {
            renderPass.setPipeline(entry.getKey().pipeline());
            AbstractTexture texture = textureManager.getTexture(entry.getKey().textureAtlasLocation());
            renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
            renderPass.drawIndexed(entry.getValue().vertexOffset, 0, entry.getValue().indexCount, 1);
        }
    }

    protected void renderRotatedQuad(VertexConsumer builder, float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
        Quaternionf rotation = new Quaternionf(xRot, yRot, zRot, wRot);
        this.renderVertex(builder, rotation, x, y, z, 1.0f, -1.0f, scale, u1, v1, color, lightCoords);
        this.renderVertex(builder, rotation, x, y, z, 1.0f, 1.0f, scale, u1, v0, color, lightCoords);
        this.renderVertex(builder, rotation, x, y, z, -1.0f, 1.0f, scale, u0, v0, color, lightCoords);
        this.renderVertex(builder, rotation, x, y, z, -1.0f, -1.0f, scale, u0, v1, color, lightCoords);
    }

    private void renderVertex(VertexConsumer builder, Quaternionf rotation, float x, float y, float z, float nx, float ny, float scale, float u, float v, int color, int lightCoords) {
        Vector3f scratch = new Vector3f(nx, ny, 0.0f).rotate((Quaternionfc)rotation).mul(scale).add(x, y, z);
        builder.addVertex(scratch.x(), scratch.y(), scratch.z()).setUv(u, v).setColor(color).setLight(lightCoords);
    }

    @Override
    public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (this.particleCount > 0) {
            submitNodeCollector.submitParticleGroup(this);
        }
    }

    private static class Storage {
        private int capacity = 1024;
        private float[] floatValues = new float[12288];
        private int[] intValues = new int[2048];
        private int currentParticleIndex;

        private Storage() {
        }

        public void add(float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightCoords) {
            if (this.currentParticleIndex >= this.capacity) {
                this.grow();
            }
            int index = this.currentParticleIndex * 12;
            this.floatValues[index++] = x;
            this.floatValues[index++] = y;
            this.floatValues[index++] = z;
            this.floatValues[index++] = xRot;
            this.floatValues[index++] = yRot;
            this.floatValues[index++] = zRot;
            this.floatValues[index++] = wRot;
            this.floatValues[index++] = scale;
            this.floatValues[index++] = u0;
            this.floatValues[index++] = u1;
            this.floatValues[index++] = v0;
            this.floatValues[index] = v1;
            index = this.currentParticleIndex * 2;
            this.intValues[index++] = color;
            this.intValues[index] = lightCoords;
            ++this.currentParticleIndex;
        }

        public void forEachParticle(ParticleConsumer consumer) {
            for (int particleIndex = 0; particleIndex < this.currentParticleIndex; ++particleIndex) {
                int floatIndex = particleIndex * 12;
                int intIndex = particleIndex * 2;
                consumer.consume(this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex], this.intValues[intIndex++], this.intValues[intIndex]);
            }
        }

        public void clear() {
            this.currentParticleIndex = 0;
        }

        private void grow() {
            this.capacity *= 2;
            this.floatValues = Arrays.copyOf(this.floatValues, this.capacity * 12);
            this.intValues = Arrays.copyOf(this.intValues, this.capacity * 2);
        }

        public int count() {
            return this.currentParticleIndex;
        }
    }

    @FunctionalInterface
    public static interface ParticleConsumer {
        public void consume(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, int var13, int var14);
    }

    public record PreparedLayer(int vertexOffset, int indexCount) {
    }

    public record PreparedBuffers(int indexCount, GpuBufferSlice dynamicTransforms, Map<SingleQuadParticle.Layer, PreparedLayer> layers) {
    }
}

