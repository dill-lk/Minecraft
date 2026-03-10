/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.world.entity.Mob;

@Deprecated
public abstract class AgeableMobRenderer<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
extends MobRenderer<T, S, M> {
    private final M adultModel;
    private final M babyModel;

    public AgeableMobRenderer(EntityRendererProvider.Context context, M adultModel, M babyModel, float shadow) {
        super(context, adultModel, shadow);
        this.adultModel = adultModel;
        this.babyModel = babyModel;
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = ((LivingEntityRenderState)state).isBaby ? this.babyModel : this.adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}

