/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.ghast.HappyGhastHarnessModel;
import net.mayaan.client.model.animal.ghast.HappyGhastModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.RopesLayer;
import net.mayaan.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.mayaan.client.renderer.entity.state.HappyGhastRenderState;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.animal.happyghast.HappyGhast;
import net.mayaan.world.phys.AABB;

public class HappyGhastRenderer
extends AgeableMobRenderer<HappyGhast, HappyGhastRenderState, HappyGhastModel> {
    private static final Identifier GHAST_LOCATION = Identifier.withDefaultNamespace("textures/entity/ghast/happy_ghast.png");
    private static final Identifier GHAST_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/ghast/happy_ghast_baby.png");
    private static final Identifier GHAST_ROPES = Identifier.withDefaultNamespace("textures/entity/ghast/happy_ghast_ropes.png");

    public HappyGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new HappyGhastModel(context.bakeLayer(ModelLayers.HAPPY_GHAST)), new HappyGhastModel(context.bakeLayer(ModelLayers.HAPPY_GHAST_BABY)), 2.0f);
        this.addLayer(new SimpleEquipmentLayer<HappyGhastRenderState, HappyGhastModel, HappyGhastHarnessModel>(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HAPPY_GHAST_BODY, state -> state.bodyItem, new HappyGhastHarnessModel(context.bakeLayer(ModelLayers.HAPPY_GHAST_HARNESS)), new HappyGhastHarnessModel(context.bakeLayer(ModelLayers.HAPPY_GHAST_BABY_HARNESS))));
        this.addLayer(new RopesLayer<HappyGhastModel>(this, context.getModelSet(), GHAST_ROPES));
    }

    @Override
    public Identifier getTextureLocation(HappyGhastRenderState state) {
        if (state.isBaby) {
            return GHAST_BABY_LOCATION;
        }
        return GHAST_LOCATION;
    }

    @Override
    public HappyGhastRenderState createRenderState() {
        return new HappyGhastRenderState();
    }

    @Override
    protected AABB getBoundingBoxForCulling(HappyGhast entity) {
        AABB aabb = super.getBoundingBoxForCulling(entity);
        float height = entity.getBbHeight();
        return aabb.setMinY(aabb.minY - (double)(height / 2.0f));
    }

    @Override
    public void extractRenderState(HappyGhast entity, HappyGhastRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.bodyItem = entity.getItemBySlot(EquipmentSlot.BODY).copy();
        state.isRidden = entity.isVehicle();
        state.isLeashHolder = entity.isLeashHolder();
    }
}

