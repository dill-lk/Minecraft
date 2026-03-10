/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.monster.piglin.AdultPiglinModel;
import net.mayaan.client.model.monster.piglin.BabyPiglinModel;
import net.mayaan.client.model.monster.piglin.PiglinModel;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.HumanoidMobRenderer;
import net.mayaan.client.renderer.entity.layers.CustomHeadLayer;
import net.mayaan.client.renderer.entity.layers.HumanoidArmorLayer;
import net.mayaan.client.renderer.entity.state.PiglinRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.monster.piglin.AbstractPiglin;
import net.mayaan.world.item.CrossbowItem;

public class PiglinRenderer
extends HumanoidMobRenderer<AbstractPiglin, PiglinRenderState, PiglinModel> {
    private static final Identifier PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin.png");
    private static final Identifier PIGLIN_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin_baby.png");
    private static final Identifier PIGLIN_BRUTE_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin_brute.png");
    public static final CustomHeadLayer.Transforms PIGLIN_CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(0.0f, 0.0f, 1.0019531f);

    public PiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
        super(context, new AdultPiglinModel(context.bakeLayer(body)), new BabyPiglinModel(context.bakeLayer(babyBody)), 0.5f, PIGLIN_CUSTOM_HEAD_TRANSFORMS);
        this.addLayer(new HumanoidArmorLayer<PiglinRenderState, PiglinModel, PiglinModel>(this, ArmorModelSet.bake(armorSet, context.getModelSet(), AdultPiglinModel::new), ArmorModelSet.bake(babyArmorSet, context.getModelSet(), BabyPiglinModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTextureLocation(PiglinRenderState state) {
        return state.isBrute ? PIGLIN_BRUTE_LOCATION : (state.isBaby ? PIGLIN_BABY_LOCATION : PIGLIN_LOCATION);
    }

    @Override
    public PiglinRenderState createRenderState() {
        return new PiglinRenderState();
    }

    @Override
    public void extractRenderState(AbstractPiglin entity, PiglinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isBrute = entity.is(EntityType.PIGLIN_BRUTE);
        state.armPose = entity.getArmPose();
        state.maxCrossbowChageDuration = CrossbowItem.getChargeDuration(entity.getUseItem(), entity);
        state.isConverting = entity.isConverting();
    }

    @Override
    protected boolean isShaking(PiglinRenderState state) {
        return super.isShaking(state) || state.isConverting;
    }
}

