/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.HumanoidModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.zombie.GiantZombieModel;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.HumanoidMobRenderer;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.HumanoidArmorLayer;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.state.ZombieRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.Giant;

public class GiantMobRenderer
extends MobRenderer<Giant, ZombieRenderState, HumanoidModel<ZombieRenderState>> {
    private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");

    public GiantMobRenderer(EntityRendererProvider.Context context, float scale) {
        super(context, new GiantZombieModel(context.bakeLayer(ModelLayers.GIANT)), 0.5f * scale);
        this.addLayer(new ItemInHandLayer<ZombieRenderState, HumanoidModel<ZombieRenderState>>(this));
        this.addLayer(new HumanoidArmorLayer<ZombieRenderState, HumanoidModel<ZombieRenderState>, GiantZombieModel>(this, ArmorModelSet.bake(ModelLayers.GIANT_ARMOR, context.getModelSet(), GiantZombieModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return ZOMBIE_LOCATION;
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public void extractRenderState(Giant entity, ZombieRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
    }
}

