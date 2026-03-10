/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  org.joml.Vector3f
 */
package net.mayaan.client.renderer.feature;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.mayaan.SharedConstants;
import net.mayaan.client.model.Model;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.OutlineBufferSource;
import net.mayaan.client.renderer.SubmitNodeCollection;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.resources.model.ModelBakery;
import org.joml.Vector3f;

public class ModelFeatureRenderer {
    private final PoseStack poseStack = new PoseStack();

    public void renderSolid(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource) {
        Storage storage = nodeCollection.getModelSubmits();
        this.renderBatch(bufferSource, outlineBufferSource, storage.solidModelSubmits, crumblingBufferSource);
    }

    public void renderTranslucent(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource) {
        Storage storage = nodeCollection.getModelSubmits();
        storage.translucentModelSubmits.sort(Comparator.comparingDouble(submit -> -submit.position().lengthSquared()));
        this.renderTranslucents(bufferSource, outlineBufferSource, storage.translucentModelSubmits, crumblingBufferSource);
    }

    private void renderTranslucents(MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, List<SubmitNodeStorage.TranslucentModelSubmit<?>> submits, MultiBufferSource.BufferSource crumblingBufferSource) {
        for (SubmitNodeStorage.TranslucentModelSubmit<?> submit : submits) {
            this.renderModel(submit.modelSubmit(), submit.renderType(), bufferSource.getBuffer(submit.renderType()), outlineBufferSource, crumblingBufferSource);
        }
    }

    private void renderBatch(MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, Map<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> map, MultiBufferSource.BufferSource crumblingBufferSource) {
        Collection<Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>>> entries;
        if (SharedConstants.DEBUG_SHUFFLE_MODELS) {
            ArrayList shuffledCopy = new ArrayList(map.entrySet());
            Collections.shuffle(shuffledCopy);
            entries = shuffledCopy;
        } else {
            entries = map.entrySet();
        }
        for (Map.Entry entry : entries) {
            VertexConsumer buffer = bufferSource.getBuffer((RenderType)entry.getKey());
            for (SubmitNodeStorage.ModelSubmit submit : (List)entry.getValue()) {
                this.renderModel(submit, (RenderType)entry.getKey(), buffer, outlineBufferSource, crumblingBufferSource);
            }
        }
    }

    private <S> void renderModel(SubmitNodeStorage.ModelSubmit<S> submit, RenderType renderType, VertexConsumer buffer, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource) {
        this.poseStack.pushPose();
        this.poseStack.last().set(submit.pose());
        Model<S> model = submit.model();
        VertexConsumer wrappedBuffer = submit.sprite() == null ? buffer : submit.sprite().wrap(buffer);
        model.setupAnim(submit.state());
        model.renderToBuffer(this.poseStack, wrappedBuffer, submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
        if (submit.outlineColor() != 0 && (renderType.outline().isPresent() || renderType.isOutline())) {
            outlineBufferSource.setColor(submit.outlineColor());
            VertexConsumer outlineBuffer = outlineBufferSource.getBuffer(renderType);
            model.renderToBuffer(this.poseStack, submit.sprite() == null ? outlineBuffer : submit.sprite().wrap(outlineBuffer), submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
        }
        if (submit.crumblingOverlay() != null && renderType.affectsCrumbling()) {
            SheetedDecalTextureGenerator breakingBuffer = new SheetedDecalTextureGenerator(crumblingBufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(submit.crumblingOverlay().progress())), submit.crumblingOverlay().cameraPose(), 1.0f);
            model.renderToBuffer(this.poseStack, submit.sprite() == null ? breakingBuffer : submit.sprite().wrap(breakingBuffer), submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
        }
        this.poseStack.popPose();
    }

    public static class Storage {
        private final Map<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> solidModelSubmits = new HashMap();
        private final List<SubmitNodeStorage.TranslucentModelSubmit<?>> translucentModelSubmits = new ArrayList();
        private final Set<RenderType> usedModelSubmitBuckets = new ObjectOpenHashSet();

        public void add(RenderType renderType, SubmitNodeStorage.ModelSubmit<?> modelSubmit) {
            if (!renderType.hasBlending()) {
                this.solidModelSubmits.computeIfAbsent(renderType, ignored -> new ArrayList()).add(modelSubmit);
            } else {
                Vector3f position = modelSubmit.pose().pose().transformPosition(new Vector3f());
                this.translucentModelSubmits.add(new SubmitNodeStorage.TranslucentModelSubmit(modelSubmit, renderType, position));
            }
        }

        public void clear() {
            this.translucentModelSubmits.clear();
            for (Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> bucketEntry : this.solidModelSubmits.entrySet()) {
                List<SubmitNodeStorage.ModelSubmit<?>> bucket = bucketEntry.getValue();
                if (bucket.isEmpty()) continue;
                this.usedModelSubmitBuckets.add(bucketEntry.getKey());
                bucket.clear();
            }
        }

        public void endFrame() {
            this.solidModelSubmits.keySet().removeIf(renderType -> !this.usedModelSubmitBuckets.contains(renderType));
            this.usedModelSubmitBuckets.clear();
        }
    }

    public record CrumblingOverlay(int progress, PoseStack.Pose cameraPose) {
    }
}

