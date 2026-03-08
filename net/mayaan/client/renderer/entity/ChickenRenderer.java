/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.mayaan.client.renderer.entity;

import com.google.common.collect.Maps;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.mayaan.client.model.AdultAndBabyModelPair;
import net.mayaan.client.model.animal.chicken.AdultChickenModel;
import net.mayaan.client.model.animal.chicken.BabyChickenModel;
import net.mayaan.client.model.animal.chicken.ChickenModel;
import net.mayaan.client.model.animal.chicken.ColdChickenModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.ChickenRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.MissingTextureAtlasSprite;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.animal.chicken.Chicken;
import net.mayaan.world.entity.animal.chicken.ChickenVariant;

public class ChickenRenderer
extends MobRenderer<Chicken, ChickenRenderState, ChickenModel> {
    private final Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> models;

    public ChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3f);
        this.models = ChickenRenderer.bakeModels(context);
    }

    private static Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(ChickenVariant.ModelType.NORMAL, new AdultAndBabyModelPair<BabyChickenModel>((BabyChickenModel)((Object)new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN))), new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY))), ChickenVariant.ModelType.COLD, new AdultAndBabyModelPair<BabyChickenModel>((BabyChickenModel)((Object)new ColdChickenModel(context.bakeLayer(ModelLayers.COLD_CHICKEN))), new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY)))));
    }

    @Override
    public void submit(ChickenRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.variant == null) {
            return;
        }
        this.model = this.models.get(state.variant.modelAndTexture().model()).getModel(state.isBaby);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public Identifier getTextureLocation(ChickenRenderState state) {
        return state.variant == null ? MissingTextureAtlasSprite.getLocation() : (state.isBaby ? state.variant.babyTexture().texturePath() : state.variant.modelAndTexture().asset().texturePath());
    }

    @Override
    public ChickenRenderState createRenderState() {
        return new ChickenRenderState();
    }

    @Override
    public void extractRenderState(Chicken entity, ChickenRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        state.flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        state.variant = entity.getVariant().value();
    }
}

