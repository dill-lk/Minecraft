/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model.monster.zombie;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractZombieModel<S extends ZombieRenderState>
extends HumanoidModel<S> {
    protected AbstractZombieModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        if (((ZombieRenderState)state).isBaby && ((ArmedEntityRenderState)state).getMainHandItemStack() != ItemStack.EMPTY) {
            this.rightArm.xRot = 0.0f;
            this.leftArm.xRot = 0.0f;
            return;
        }
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, ((ZombieRenderState)state).isAggressive, state);
    }
}

