/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.camel.AdultCamelModel;
import net.mayaan.client.model.animal.camel.BabyCamelModel;
import net.mayaan.client.model.animal.camel.CamelModel;
import net.mayaan.client.model.animal.camel.CamelSaddleModel;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.mayaan.client.renderer.entity.state.CamelRenderState;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.animal.camel.Camel;

public class CamelRenderer
extends AgeableMobRenderer<Camel, CamelRenderState, CamelModel> {
    private static final Identifier CAMEL_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel.png");
    private static final Identifier CAMEL_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel_baby.png");

    public CamelRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultCamelModel(context.bakeLayer(ModelLayers.CAMEL)), new BabyCamelModel(context.bakeLayer(ModelLayers.CAMEL_BABY)), 0.7f);
        this.addLayer(CamelRenderer.createCamelSaddleLayer(context, this, EquipmentClientInfo.LayerType.CAMEL_SADDLE, ModelLayers.CAMEL_SADDLE));
    }

    protected static SimpleEquipmentLayer<CamelRenderState, CamelModel, CamelSaddleModel> createCamelSaddleLayer(EntityRendererProvider.Context context, MobRenderer<Camel, CamelRenderState, CamelModel> renderer, EquipmentClientInfo.LayerType saddleLayerType, ModelLayerLocation saddleModelLayer) {
        return new SimpleEquipmentLayer(renderer, context.getEquipmentRenderer(), saddleLayerType, state -> state.saddle, new CamelSaddleModel(context.bakeLayer(saddleModelLayer)), null);
    }

    @Override
    public Identifier getTextureLocation(CamelRenderState state) {
        return state.isBaby ? CAMEL_BABY_LOCATION : CAMEL_LOCATION;
    }

    @Override
    public CamelRenderState createRenderState() {
        return new CamelRenderState();
    }

    @Override
    public void extractRenderState(Camel entity, CamelRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        CamelRenderer.extractAdditionalState(entity, state, partialTicks);
    }

    static void extractAdditionalState(Camel entity, CamelRenderState state, float partialTicks) {
        state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
        state.isRidden = entity.isVehicle();
        state.jumpCooldown = CamelRenderer.getJumpCooldown(entity, partialTicks);
        state.sitAnimationState.copyFrom(entity.sitAnimationState);
        state.sitPoseAnimationState.copyFrom(entity.sitPoseAnimationState);
        state.sitUpAnimationState.copyFrom(entity.sitUpAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.dashAnimationState.copyFrom(entity.dashAnimationState);
    }

    static float getJumpCooldown(Camel camel, float partialTicks) {
        return Math.max((float)camel.getJumpCooldown() - partialTicks, 0.0f);
    }
}

