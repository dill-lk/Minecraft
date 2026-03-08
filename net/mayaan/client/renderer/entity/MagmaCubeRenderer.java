/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.slime.MagmaCubeModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.SlimeRenderState;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.monster.MagmaCube;

public class MagmaCubeRenderer
extends MobRenderer<MagmaCube, SlimeRenderState, MagmaCubeModel> {
    private static final Identifier MAGMACUBE_LOCATION = Identifier.withDefaultNamespace("textures/entity/slime/magmacube.png");

    public MagmaCubeRenderer(EntityRendererProvider.Context context) {
        super(context, new MagmaCubeModel(context.bakeLayer(ModelLayers.MAGMA_CUBE)), 0.25f);
    }

    @Override
    protected int getBlockLightLevel(MagmaCube entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTextureLocation(SlimeRenderState state) {
        return MAGMACUBE_LOCATION;
    }

    @Override
    public SlimeRenderState createRenderState() {
        return new SlimeRenderState();
    }

    @Override
    public void extractRenderState(MagmaCube entity, SlimeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.squish = Mth.lerp(partialTicks, entity.oSquish, entity.squish);
        state.size = entity.getSize();
    }

    @Override
    protected float getShadowRadius(SlimeRenderState state) {
        return (float)state.size * 0.25f;
    }

    @Override
    protected void scale(SlimeRenderState state, PoseStack poseStack) {
        int size = state.size;
        float ss = state.squish / ((float)size * 0.5f + 1.0f);
        float w = 1.0f / (ss + 1.0f);
        poseStack.scale(w * (float)size, 1.0f / w * (float)size, w * (float)size);
    }
}

