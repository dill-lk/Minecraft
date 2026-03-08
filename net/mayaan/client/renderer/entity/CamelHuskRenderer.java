/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.camel.AdultCamelModel;
import net.mayaan.client.model.animal.camel.CamelModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.CamelRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.CamelRenderState;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.camel.Camel;

public class CamelHuskRenderer
extends MobRenderer<Camel, CamelRenderState, CamelModel> {
    private static final Identifier CAMEL_HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel_husk.png");

    public CamelHuskRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultCamelModel(context.bakeLayer(ModelLayers.CAMEL)), 0.7f);
        this.addLayer(CamelRenderer.createCamelSaddleLayer(context, this, EquipmentClientInfo.LayerType.CAMEL_HUSK_SADDLE, ModelLayers.CAMEL_HUSK_SADDLE));
    }

    @Override
    public Identifier getTextureLocation(CamelRenderState state) {
        return CAMEL_HUSK_LOCATION;
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
}

