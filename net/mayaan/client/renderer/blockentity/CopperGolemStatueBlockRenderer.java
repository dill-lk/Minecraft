/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import com.maayanlabs.math.Transformation;
import java.util.HashMap;
import java.util.Map;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.statue.CopperGolemStatueModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.state.CopperGolemStatueRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.Direction;
import net.mayaan.util.Unit;
import net.mayaan.util.Util;
import net.mayaan.world.entity.animal.golem.CopperGolemOxidationLevels;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.CopperGolemStatueBlock;
import net.mayaan.world.level.block.WeatheringCopper;
import net.mayaan.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.jspecify.annotations.Nullable;

public class CopperGolemStatueBlockRenderer
implements BlockEntityRenderer<CopperGolemStatueBlockEntity, CopperGolemStatueRenderState> {
    private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, CopperGolemStatueBlockRenderer::createModelTransformation);
    private final Map<CopperGolemStatueBlock.Pose, CopperGolemStatueModel> models = new HashMap<CopperGolemStatueBlock.Pose, CopperGolemStatueModel>();

    public CopperGolemStatueBlockRenderer(BlockEntityRendererProvider.Context context) {
        EntityModelSet modelSet = context.entityModelSet();
        this.models.put(CopperGolemStatueBlock.Pose.STANDING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM)));
        this.models.put(CopperGolemStatueBlock.Pose.RUNNING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_RUNNING)));
        this.models.put(CopperGolemStatueBlock.Pose.SITTING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_SITTING)));
        this.models.put(CopperGolemStatueBlock.Pose.STAR, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_STAR)));
    }

    @Override
    public CopperGolemStatueRenderState createRenderState() {
        return new CopperGolemStatueRenderState();
    }

    @Override
    public void extractRenderState(CopperGolemStatueBlockEntity blockEntity, CopperGolemStatueRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        WeatheringCopper.WeatherState weatherState;
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.direction = blockState.getValue(CopperGolemStatueBlock.FACING);
        state.pose = blockState.getValue(CopperGolemStatueBlock.POSE);
        Block block = blockState.getBlock();
        if (block instanceof CopperGolemStatueBlock) {
            CopperGolemStatueBlock copperGolemStatueBlock = (CopperGolemStatueBlock)block;
            weatherState = copperGolemStatueBlock.getWeatheringState();
        } else {
            weatherState = WeatheringCopper.WeatherState.UNAFFECTED;
        }
        state.oxidationState = weatherState;
    }

    @Override
    public void submit(CopperGolemStatueRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(CopperGolemStatueBlockRenderer.modelTransformation(state.direction));
        CopperGolemStatueModel model = this.models.get(state.pose);
        RenderType renderType = RenderTypes.entityCutout(CopperGolemOxidationLevels.getOxidationLevel(state.oxidationState).texture());
        submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
        poseStack.popPose();
    }

    public static Transformation modelTransformation(Direction facing) {
        return TRANSFORMATIONS.get(facing);
    }

    private static Transformation createModelTransformation(Direction entityDirection) {
        return new Transformation((Matrix4fc)new Matrix4f().translation(0.5f, 0.0f, 0.5f).rotate((Quaternionfc)Axis.YP.rotationDegrees(-entityDirection.getOpposite().toYRot())));
    }
}

