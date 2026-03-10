/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.HumanoidModel;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.player.PlayerEarsModel;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.AvatarRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;

public class Deadmau5EarsLayer
extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final HumanoidModel<AvatarRenderState> model;

    public Deadmau5EarsLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new PlayerEarsModel(modelSet.bakeLayer(ModelLayers.PLAYER_EARS));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!state.showExtraEars || state.isInvisible) {
            return;
        }
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entitySolid(state.skin.body().texturePath()), lightCoords, overlayCoords, state.outlineColor, null);
    }
}

