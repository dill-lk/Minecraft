/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.monster.piglin.AdultZombifiedPiglinModel;
import net.mayaan.client.model.monster.piglin.BabyZombifiedPiglinModel;
import net.mayaan.client.model.monster.piglin.ZombifiedPiglinModel;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.HumanoidMobRenderer;
import net.mayaan.client.renderer.entity.PiglinRenderer;
import net.mayaan.client.renderer.entity.layers.HumanoidArmorLayer;
import net.mayaan.client.renderer.entity.state.ZombifiedPiglinRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.zombie.ZombifiedPiglin;

public class ZombifiedPiglinRenderer
extends HumanoidMobRenderer<ZombifiedPiglin, ZombifiedPiglinRenderState, ZombifiedPiglinModel> {
    private static final Identifier ZOMBIFIED_PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/zombified_piglin.png");
    private static final Identifier BABY_ZOMBIFIED_PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/zombified_piglin_baby.png");

    public ZombifiedPiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
        super(context, new AdultZombifiedPiglinModel(context.bakeLayer(body)), new BabyZombifiedPiglinModel(context.bakeLayer(babyBody)), 0.5f, PiglinRenderer.PIGLIN_CUSTOM_HEAD_TRANSFORMS);
        this.addLayer(new HumanoidArmorLayer<ZombifiedPiglinRenderState, ZombifiedPiglinModel, ZombifiedPiglinModel>(this, ArmorModelSet.bake(armorSet, context.getModelSet(), AdultZombifiedPiglinModel::new), ArmorModelSet.bake(babyArmorSet, context.getModelSet(), BabyZombifiedPiglinModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTextureLocation(ZombifiedPiglinRenderState state) {
        return state.isBaby ? BABY_ZOMBIFIED_PIGLIN_LOCATION : ZOMBIFIED_PIGLIN_LOCATION;
    }

    @Override
    public ZombifiedPiglinRenderState createRenderState() {
        return new ZombifiedPiglinRenderState();
    }

    @Override
    public void extractRenderState(ZombifiedPiglin entity, ZombifiedPiglinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isAggressive = entity.isAggressive();
    }
}

