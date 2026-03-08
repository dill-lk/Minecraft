/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.HumanoidModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.zombie.BabyDrownedModel;
import net.mayaan.client.model.monster.zombie.DrownedModel;
import net.mayaan.client.renderer.entity.AbstractZombieRenderer;
import net.mayaan.client.renderer.entity.ArmorModelSet;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.DrownedOuterLayer;
import net.mayaan.client.renderer.entity.state.ZombieRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.entity.monster.zombie.Drowned;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import org.joml.Quaternionfc;

public class DrownedRenderer
extends AbstractZombieRenderer<Drowned, ZombieRenderState, DrownedModel> {
    private static final Identifier DROWNED_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned.png");
    private static final Identifier BABY_DROWNED_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_baby.png");

    public DrownedRenderer(EntityRendererProvider.Context context) {
        super(context, new DrownedModel(context.bakeLayer(ModelLayers.DROWNED)), new BabyDrownedModel(context.bakeLayer(ModelLayers.DROWNED_BABY)), ArmorModelSet.bake(ModelLayers.DROWNED_ARMOR, context.getModelSet(), DrownedModel::new), ArmorModelSet.bake(ModelLayers.DROWNED_BABY_ARMOR, context.getModelSet(), BabyDrownedModel::new));
        this.addLayer(new DrownedOuterLayer(this, context.getModelSet()));
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return state.isBaby ? BABY_DROWNED_LOCATION : DROWNED_LOCATION;
    }

    @Override
    protected void setupRotations(ZombieRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        float swimAmount = state.swimAmount;
        if (swimAmount > 0.0f) {
            float targetRotationX = -10.0f - state.xRot;
            float rotationX = Mth.lerp(swimAmount, 0.0f, targetRotationX);
            poseStack.rotateAround((Quaternionfc)Axis.XP.rotationDegrees(rotationX), 0.0f, state.boundingBoxHeight / 2.0f / entityScale, 0.0f);
        }
    }

    @Override
    protected HumanoidModel.ArmPose getArmPose(Drowned mob, HumanoidArm arm) {
        ItemStack item = mob.getItemHeldByArm(arm);
        if (mob.getMainArm() == arm && mob.isAggressive() && item.is(Items.TRIDENT)) {
            return HumanoidModel.ArmPose.THROW_TRIDENT;
        }
        return super.getArmPose(mob, arm);
    }
}

