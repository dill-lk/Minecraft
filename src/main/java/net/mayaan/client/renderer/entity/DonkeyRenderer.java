/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.equine.BabyDonkeyModel;
import net.mayaan.client.model.animal.equine.DonkeyModel;
import net.mayaan.client.model.animal.equine.EquineSaddleModel;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AbstractHorseRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.mayaan.client.renderer.entity.state.DonkeyRenderState;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.equine.AbstractChestedHorse;

public class DonkeyRenderer<T extends AbstractChestedHorse>
extends AbstractHorseRenderer<T, DonkeyRenderState, DonkeyModel> {
    private final Identifier adultTexture;
    private final Identifier babyTexture;

    public DonkeyRenderer(EntityRendererProvider.Context context, EquipmentClientInfo.LayerType saddleLayer, ModelLayerLocation saddleModel, Type adult, Type baby) {
        super(context, new DonkeyModel(context.bakeLayer(adult.model)), new BabyDonkeyModel(context.bakeLayer(baby.model)));
        this.adultTexture = adult.texture;
        this.babyTexture = baby.texture;
        this.addLayer(new SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, Object>(this, context.getEquipmentRenderer(), saddleLayer, state -> state.saddle, new EquineSaddleModel(context.bakeLayer(saddleModel)), null));
    }

    @Override
    public Identifier getTextureLocation(DonkeyRenderState state) {
        return state.isBaby ? this.babyTexture : this.adultTexture;
    }

    @Override
    public DonkeyRenderState createRenderState() {
        return new DonkeyRenderState();
    }

    @Override
    public void extractRenderState(T entity, DonkeyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.hasChest = ((AbstractChestedHorse)entity).hasChest();
    }

    public static enum Type {
        DONKEY(Identifier.withDefaultNamespace("textures/entity/horse/donkey.png"), ModelLayers.DONKEY),
        DONKEY_BABY(Identifier.withDefaultNamespace("textures/entity/horse/donkey_baby.png"), ModelLayers.DONKEY_BABY),
        MULE(Identifier.withDefaultNamespace("textures/entity/horse/mule.png"), ModelLayers.MULE),
        MULE_BABY(Identifier.withDefaultNamespace("textures/entity/horse/mule_baby.png"), ModelLayers.MULE_BABY);

        private final Identifier texture;
        private final ModelLayerLocation model;

        private Type(Identifier texture, ModelLayerLocation model) {
            this.texture = texture;
            this.model = model;
        }
    }
}

