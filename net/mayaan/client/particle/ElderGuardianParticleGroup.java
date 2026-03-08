/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.particle;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import java.util.List;
import net.mayaan.client.Camera;
import net.mayaan.client.model.Model;
import net.mayaan.client.particle.ElderGuardianParticle;
import net.mayaan.client.particle.ParticleEngine;
import net.mayaan.client.particle.ParticleGroup;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.state.level.ParticleGroupRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.Unit;
import org.joml.Quaternionfc;

public class ElderGuardianParticleGroup
extends ParticleGroup<ElderGuardianParticle> {
    public ElderGuardianParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
        return new State(this.particles.stream().map(particle -> ElderGuardianParticleRenderState.fromParticle(particle, camera, partialTickTime)).toList());
    }

    private record State(List<ElderGuardianParticleRenderState> states) implements ParticleGroupRenderState
    {
        @Override
        public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
            for (ElderGuardianParticleRenderState state : this.states) {
                submitNodeCollector.submitModel(state.model, Unit.INSTANCE, state.poseStack, state.renderType, 0xF000F0, OverlayTexture.NO_OVERLAY, state.color, null, 0, null);
            }
        }
    }

    private record ElderGuardianParticleRenderState(Model<Unit> model, PoseStack poseStack, RenderType renderType, int color) {
        public static ElderGuardianParticleRenderState fromParticle(ElderGuardianParticle particle, Camera camera, float partialTickTime) {
            float ageScale = ((float)particle.age + partialTickTime) / (float)particle.lifetime;
            float alpha = 0.05f + 0.5f * Mth.sin(ageScale * (float)Math.PI);
            int color = ARGB.colorFromFloat(alpha, 1.0f, 1.0f, 1.0f);
            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.mulPose((Quaternionfc)camera.rotation());
            poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(60.0f - 150.0f * ageScale));
            float scale = 0.42553192f;
            poseStack.scale(0.42553192f, -0.42553192f, -0.42553192f);
            poseStack.translate(0.0f, -0.56f, 3.5f);
            return new ElderGuardianParticleRenderState(particle.model, poseStack, particle.renderType, color);
        }
    }
}

