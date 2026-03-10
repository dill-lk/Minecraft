/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.nautilus.NautilusArmorModel;
import net.mayaan.client.model.animal.nautilus.NautilusModel;
import net.mayaan.client.model.animal.nautilus.NautilusSaddleModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.mayaan.client.renderer.entity.state.NautilusRenderState;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.animal.nautilus.AbstractNautilus;

public class NautilusRenderer<T extends AbstractNautilus>
extends AgeableMobRenderer<T, NautilusRenderState, NautilusModel> {
    private static final Identifier NAUTILUS_LOCATION = Identifier.withDefaultNamespace("textures/entity/nautilus/nautilus.png");
    private static final Identifier NAUTILUS_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/nautilus/nautilus_baby.png");

    public NautilusRenderer(EntityRendererProvider.Context context) {
        super(context, new NautilusModel(context.bakeLayer(ModelLayers.NAUTILUS)), new NautilusModel(context.bakeLayer(ModelLayers.NAUTILUS_BABY)), 0.7f);
        this.addLayer(new SimpleEquipmentLayer<NautilusRenderState, NautilusModel, Object>(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_BODY, state -> state.bodyArmorItem, new NautilusArmorModel(context.bakeLayer(ModelLayers.NAUTILUS_ARMOR)), null));
        this.addLayer(new SimpleEquipmentLayer<NautilusRenderState, NautilusModel, Object>(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_SADDLE, state -> state.saddle, new NautilusSaddleModel(context.bakeLayer(ModelLayers.NAUTILUS_SADDLE)), null));
    }

    @Override
    public Identifier getTextureLocation(NautilusRenderState state) {
        return state.isBaby ? NAUTILUS_BABY_LOCATION : NAUTILUS_LOCATION;
    }

    @Override
    public NautilusRenderState createRenderState() {
        return new NautilusRenderState();
    }

    @Override
    public void extractRenderState(T entity, NautilusRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.saddle = ((LivingEntity)entity).getItemBySlot(EquipmentSlot.SADDLE).copy();
        state.bodyArmorItem = ((Mob)entity).getBodyArmorItem().copy();
    }
}

