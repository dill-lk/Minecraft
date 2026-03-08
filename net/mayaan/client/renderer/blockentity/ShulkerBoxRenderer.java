/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Transformation;
import java.util.Map;
import java.util.function.Consumer;
import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.core.Direction;
import net.mayaan.util.Util;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.block.ShulkerBoxBlock;
import net.mayaan.world.level.block.entity.ShulkerBoxBlockEntity;
import net.mayaan.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class ShulkerBoxRenderer
implements BlockEntityRenderer<ShulkerBoxBlockEntity, ShulkerBoxRenderState> {
    private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, ShulkerBoxRenderer::createModelTransform);
    private final SpriteGetter sprites;
    private final ShulkerBoxModel model;

    public ShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
        this(context.entityModelSet(), context.sprites());
    }

    public ShulkerBoxRenderer(SpecialModelRenderer.BakingContext context) {
        this(context.entityModelSet(), context.sprites());
    }

    public ShulkerBoxRenderer(EntityModelSet context, SpriteGetter sprites) {
        this.sprites = sprites;
        this.model = new ShulkerBoxModel(context.bakeLayer(ModelLayers.SHULKER_BOX));
    }

    @Override
    public ShulkerBoxRenderState createRenderState() {
        return new ShulkerBoxRenderState();
    }

    @Override
    public void extractRenderState(ShulkerBoxBlockEntity blockEntity, ShulkerBoxRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = blockEntity.getBlockState().getValueOrElse(ShulkerBoxBlock.FACING, Direction.UP);
        state.color = blockEntity.getColor();
        state.progress = blockEntity.getProgress(partialTicks);
    }

    @Override
    public void submit(ShulkerBoxRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        DyeColor color = state.color;
        SpriteId sprite = color == null ? Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION : Sheets.getShulkerBoxSprite(color);
        this.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.direction, state.progress, state.breakProgress, sprite, 0);
    }

    private void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Direction direction, float progress,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress, SpriteId sprite, int outlineColor) {
        poseStack.pushPose();
        poseStack.mulPose(ShulkerBoxRenderer.modelTransform(direction));
        this.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, progress, breakProgress, sprite, outlineColor);
        poseStack.popPose();
    }

    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, float progress,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress, SpriteId sprite, int outlineColor) {
        this.model.setupAnim(Float.valueOf(progress));
        submitNodeCollector.submitModel(this.model, Float.valueOf(progress), poseStack, sprite.renderType(this.model::renderType), lightCoords, overlayCoords, -1, this.sprites.get(sprite), outlineColor, breakProgress);
    }

    private static Transformation createModelTransform(Direction direction) {
        float scale = 0.9995f;
        return new Transformation((Matrix4fc)new Matrix4f().translation(0.5f, 0.5f, 0.5f).scale(0.9995f, 0.9995f, 0.9995f).rotate((Quaternionfc)direction.getRotation()).scale(1.0f, -1.0f, -1.0f).translate(0.0f, -1.0f, 0.0f));
    }

    public static Transformation modelTransform(Direction direction) {
        return TRANSFORMATIONS.get(direction);
    }

    public void getExtents(float progress, Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(Float.valueOf(progress));
        this.model.root().getExtentsForGui(poseStack, output);
    }

    private static class ShulkerBoxModel
    extends Model<Float> {
        private final ModelPart lid;

        public ShulkerBoxModel(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            this.lid = root.getChild("lid");
        }

        @Override
        public void setupAnim(Float progress) {
            super.setupAnim(progress);
            this.lid.setPos(0.0f, 24.0f - progress.floatValue() * 0.5f * 16.0f, 0.0f);
            this.lid.yRot = 270.0f * progress.floatValue() * ((float)Math.PI / 180);
        }
    }
}

