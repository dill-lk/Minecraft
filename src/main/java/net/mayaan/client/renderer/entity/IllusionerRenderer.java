/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import java.util.Objects;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.illager.IllagerModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.IllagerRenderer;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.state.IllusionerRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.monster.illager.Illusioner;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;

public class IllusionerRenderer
extends IllagerRenderer<Illusioner, IllusionerRenderState> {
    private static final Identifier ILLUSIONER = Identifier.withDefaultNamespace("textures/entity/illager/illusioner.png");

    public IllusionerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel(context.bakeLayer(ModelLayers.ILLUSIONER)), 0.5f);
        this.addLayer(new ItemInHandLayer<IllusionerRenderState, IllagerModel<IllusionerRenderState>>(this, (RenderLayerParent)this){
            {
                Objects.requireNonNull(this$0);
                super(renderer);
            }

            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllusionerRenderState state, float yRot, float xRot) {
                if (state.isCastingSpell || state.isAggressive) {
                    super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                }
            }
        });
        ((IllagerModel)this.model).getHat().visible = true;
    }

    @Override
    public Identifier getTextureLocation(IllusionerRenderState state) {
        return ILLUSIONER;
    }

    @Override
    public IllusionerRenderState createRenderState() {
        return new IllusionerRenderState();
    }

    @Override
    public void extractRenderState(Illusioner entity, IllusionerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        Vec3[] illusionOffsets = entity.getIllusionOffsets(partialTicks);
        state.illusionOffsets = Arrays.copyOf(illusionOffsets, illusionOffsets.length);
        state.isCastingSpell = entity.isCastingSpell();
    }

    @Override
    public void submit(IllusionerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.isInvisible) {
            Vec3[] offsets = state.illusionOffsets;
            for (int i = 0; i < offsets.length; ++i) {
                poseStack.pushPose();
                poseStack.translate(offsets[i].x + (double)Mth.cos((float)i + state.ageInTicks * 0.5f) * 0.025, offsets[i].y + (double)Mth.cos((float)i + state.ageInTicks * 0.75f) * 0.0125, offsets[i].z + (double)Mth.cos((float)i + state.ageInTicks * 0.7f) * 0.025);
                super.submit(state, poseStack, submitNodeCollector, camera);
                poseStack.popPose();
            }
        } else {
            super.submit(state, poseStack, submitNodeCollector, camera);
        }
    }

    @Override
    protected boolean isBodyVisible(IllusionerRenderState state) {
        return true;
    }

    @Override
    protected AABB getBoundingBoxForCulling(Illusioner entity) {
        return super.getBoundingBoxForCulling(entity).inflate(3.0, 0.0, 3.0);
    }
}

