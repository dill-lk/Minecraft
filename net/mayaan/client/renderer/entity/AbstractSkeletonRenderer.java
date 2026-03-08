/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.HumanoidModel;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.monster.skeleton.SkeletonModel;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.HumanoidMobRenderer;
import net.mayaan.client.renderer.entity.layers.HumanoidArmorLayer;
import net.mayaan.client.renderer.entity.state.SkeletonRenderState;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.monster.skeleton.AbstractSkeleton;
import net.mayaan.world.item.Items;

public abstract class AbstractSkeletonRenderer<T extends AbstractSkeleton, S extends SkeletonRenderState>
extends HumanoidMobRenderer<T, S, SkeletonModel<S>> {
    public AbstractSkeletonRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ArmorModelSet<ModelLayerLocation> armorSet) {
        this(context, armorSet, new SkeletonModel(context.bakeLayer(body)));
    }

    public AbstractSkeletonRenderer(EntityRendererProvider.Context context, ArmorModelSet<ModelLayerLocation> armorSet, SkeletonModel<S> bodyModel) {
        super(context, bodyModel, 0.5f);
        this.addLayer(new HumanoidArmorLayer(this, ArmorModelSet.bake(armorSet, context.getModelSet(), SkeletonModel::new), context.getEquipmentRenderer()));
    }

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ((SkeletonRenderState)state).isAggressive = ((Mob)entity).isAggressive();
        ((SkeletonRenderState)state).isShaking = ((AbstractSkeleton)entity).isShaking();
        ((SkeletonRenderState)state).isHoldingBow = ((LivingEntity)entity).getMainHandItem().is(Items.BOW);
    }

    @Override
    protected boolean isShaking(S state) {
        return ((SkeletonRenderState)state).isShaking;
    }

    @Override
    protected HumanoidModel.ArmPose getArmPose(T mob, HumanoidArm arm) {
        if (((Mob)mob).getMainArm() == arm && ((Mob)mob).isAggressive() && ((LivingEntity)mob).getMainHandItem().is(Items.BOW)) {
            return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
        return super.getArmPose(mob, arm);
    }
}

