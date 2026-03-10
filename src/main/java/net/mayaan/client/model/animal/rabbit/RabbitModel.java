/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.animal.rabbit;

import net.mayaan.client.animation.AnimationDefinition;
import net.mayaan.client.animation.KeyframeAnimation;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.entity.state.RabbitRenderState;

public abstract class RabbitModel
extends EntityModel<RabbitRenderState> {
    protected static final String FRONT_LEGS = "frontlegs";
    protected static final String BACK_LEGS = "backlegs";
    protected static final String LEFT_HAUNCH = "left_haunch";
    protected static final String RIGHT_HAUNCH = "right_haunch";
    private final KeyframeAnimation hopAnimation;
    private final KeyframeAnimation idleHeadTiltAnimation;
    private final ModelPart head;

    public RabbitModel(ModelPart root, AnimationDefinition hop, AnimationDefinition idleHeadTilt) {
        super(root);
        this.head = root.getChild("body").getChild("head");
        this.hopAnimation = hop.bake(root);
        this.idleHeadTiltAnimation = idleHeadTilt.bake(root);
    }

    @Override
    public void setupAnim(RabbitRenderState state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * ((float)Math.PI / 180);
        this.head.xRot = state.xRot * ((float)Math.PI / 180);
        this.hopAnimation.apply(state.hopAnimationState, state.ageInTicks);
        this.idleHeadTiltAnimation.apply(state.idleHeadTiltAnimationState, state.ageInTicks);
    }
}

