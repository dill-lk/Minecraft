/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.HumanoidModel;
import net.mayaan.client.model.monster.zombie.ZombieModel;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.HumanoidMobRenderer;
import net.mayaan.client.renderer.entity.layers.HumanoidArmorLayer;
import net.mayaan.client.renderer.entity.state.ZombieRenderState;
import net.mayaan.core.component.DataComponents;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.monster.zombie.Zombie;
import net.mayaan.world.item.SwingAnimationType;
import net.mayaan.world.item.component.SwingAnimation;

public abstract class AbstractZombieRenderer<T extends Zombie, S extends ZombieRenderState, M extends ZombieModel<S>>
extends HumanoidMobRenderer<T, S, M> {
    private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");
    private static final Identifier BABY_ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie_baby.png");

    protected AbstractZombieRenderer(EntityRendererProvider.Context context, M model, M babyModel, ArmorModelSet<M> armorSet, ArmorModelSet<M> babyArmorSet) {
        super(context, model, babyModel, 0.5f);
        this.addLayer(new HumanoidArmorLayer(this, armorSet, babyArmorSet, context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTextureLocation(S state) {
        return ((ZombieRenderState)state).isBaby ? BABY_ZOMBIE_LOCATION : ZOMBIE_LOCATION;
    }

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ((ZombieRenderState)state).isAggressive = ((Mob)entity).isAggressive();
        ((ZombieRenderState)state).isConverting = ((Zombie)entity).isUnderWaterConverting();
    }

    @Override
    protected boolean isShaking(S state) {
        return super.isShaking(state) || ((ZombieRenderState)state).isConverting;
    }

    @Override
    protected HumanoidModel.ArmPose getArmPose(T mob, HumanoidArm arm) {
        SwingAnimation otherAnim = ((LivingEntity)mob).getItemHeldByArm(arm.getOpposite()).get(DataComponents.SWING_ANIMATION);
        if (otherAnim != null && otherAnim.type() == SwingAnimationType.STAB) {
            return HumanoidModel.ArmPose.SPEAR;
        }
        return super.getArmPose(mob, arm);
    }
}

