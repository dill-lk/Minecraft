/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.animal.fish.PufferfishBigModel;
import net.mayaan.client.model.animal.fish.PufferfishMidModel;
import net.mayaan.client.model.animal.fish.PufferfishSmallModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.entity.state.PufferfishRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.animal.fish.Pufferfish;

public class PufferfishRenderer
extends MobRenderer<Pufferfish, PufferfishRenderState, EntityModel<EntityRenderState>> {
    private static final Identifier PUFFER_LOCATION = Identifier.withDefaultNamespace("textures/entity/fish/pufferfish.png");
    private final EntityModel<EntityRenderState> small;
    private final EntityModel<EntityRenderState> mid;
    private final EntityModel<EntityRenderState> big = this.getModel();

    public PufferfishRenderer(EntityRendererProvider.Context context) {
        super(context, new PufferfishBigModel(context.bakeLayer(ModelLayers.PUFFERFISH_BIG)), 0.2f);
        this.mid = new PufferfishMidModel(context.bakeLayer(ModelLayers.PUFFERFISH_MEDIUM));
        this.small = new PufferfishSmallModel(context.bakeLayer(ModelLayers.PUFFERFISH_SMALL));
    }

    @Override
    public Identifier getTextureLocation(PufferfishRenderState state) {
        return PUFFER_LOCATION;
    }

    @Override
    public PufferfishRenderState createRenderState() {
        return new PufferfishRenderState();
    }

    @Override
    protected float getShadowRadius(PufferfishRenderState state) {
        return 0.1f + 0.1f * (float)state.puffState;
    }

    @Override
    public void submit(PufferfishRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = switch (state.puffState) {
            case 0 -> this.small;
            case 1 -> this.mid;
            default -> this.big;
        };
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public void extractRenderState(Pufferfish entity, PufferfishRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.puffState = entity.getPuffState();
    }

    @Override
    protected void setupRotations(PufferfishRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        poseStack.translate(0.0f, Mth.cos(state.ageInTicks * 0.05f) * 0.08f, 0.0f);
        super.setupRotations(state, poseStack, bodyRot, entityScale);
    }
}

