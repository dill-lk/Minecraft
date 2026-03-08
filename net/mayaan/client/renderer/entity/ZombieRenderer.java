/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.zombie.BabyZombieModel;
import net.mayaan.client.model.monster.zombie.ZombieModel;
import net.mayaan.client.renderer.entity.AbstractZombieRenderer;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.ZombieRenderState;
import net.mayaan.world.entity.monster.zombie.Zombie;

public class ZombieRenderer
extends AbstractZombieRenderer<Zombie, ZombieRenderState, ZombieModel<ZombieRenderState>> {
    public ZombieRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_ARMOR, ModelLayers.ZOMBIE_BABY_ARMOR);
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    public ZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
        super(context, new ZombieModel(context.bakeLayer(body)), new BabyZombieModel(context.bakeLayer(babyBody)), ArmorModelSet.bake(armorSet, context.getModelSet(), ZombieModel::new), ArmorModelSet.bake(babyArmorSet, context.getModelSet(), BabyZombieModel::new));
    }
}

