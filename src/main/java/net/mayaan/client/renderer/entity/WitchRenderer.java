/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.witch.WitchModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.WitchItemLayer;
import net.mayaan.client.renderer.entity.state.HoldingEntityRenderState;
import net.mayaan.client.renderer.entity.state.WitchRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.Witch;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;

public class WitchRenderer
extends MobRenderer<Witch, WitchRenderState, WitchModel> {
    private static final Identifier WITCH_LOCATION = Identifier.withDefaultNamespace("textures/entity/witch/witch.png");

    public WitchRenderer(EntityRendererProvider.Context context) {
        super(context, new WitchModel(context.bakeLayer(ModelLayers.WITCH)), 0.5f);
        this.addLayer(new WitchItemLayer(this));
    }

    @Override
    public Identifier getTextureLocation(WitchRenderState state) {
        return WITCH_LOCATION;
    }

    @Override
    public WitchRenderState createRenderState() {
        return new WitchRenderState();
    }

    @Override
    public void extractRenderState(Witch entity, WitchRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);
        state.entityId = entity.getId();
        ItemStack mainHandItem = entity.getMainHandItem();
        state.isHoldingItem = !mainHandItem.isEmpty();
        state.isHoldingPotion = mainHandItem.is(Items.POTION);
    }
}

