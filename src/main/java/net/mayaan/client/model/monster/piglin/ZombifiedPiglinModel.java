/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.monster.piglin;

import net.mayaan.client.model.AnimationUtils;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.monster.piglin.AbstractPiglinModel;
import net.mayaan.client.renderer.entity.state.ZombifiedPiglinRenderState;

public abstract class ZombifiedPiglinModel
extends AbstractPiglinModel<ZombifiedPiglinRenderState> {
    public ZombifiedPiglinModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(ZombifiedPiglinRenderState state) {
        super.setupAnim(state);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, state.isAggressive, state);
    }
}

