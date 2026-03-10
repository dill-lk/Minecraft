/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2IntFunction
 *  java.lang.MatchException
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import com.maayanlabs.math.Transformation;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.Map;
import java.util.function.Consumer;
import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.BrightnessCombiner;
import net.mayaan.client.renderer.blockentity.state.BedRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.core.Direction;
import net.mayaan.util.Unit;
import net.mayaan.util.Util;
import net.mayaan.world.level.block.BedBlock;
import net.mayaan.world.level.block.ChestBlock;
import net.mayaan.world.level.block.DoubleBlockCombiner;
import net.mayaan.world.level.block.entity.BedBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.properties.BedPart;
import net.mayaan.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class BedRenderer
implements BlockEntityRenderer<BedBlockEntity, BedRenderState> {
    private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, BedRenderer::createModelTransform);
    private final SpriteGetter sprites;
    private final Model.Simple headModel;
    private final Model.Simple footModel;

    public BedRenderer(BlockEntityRendererProvider.Context context) {
        this(context.sprites(), context.entityModelSet());
    }

    public BedRenderer(SpecialModelRenderer.BakingContext context) {
        this(context.sprites(), context.entityModelSet());
    }

    public BedRenderer(SpriteGetter sprites, EntityModelSet entityModelSet) {
        this.sprites = sprites;
        this.headModel = new Model.Simple(entityModelSet.bakeLayer(ModelLayers.BED_HEAD), RenderTypes::entitySolid);
        this.footModel = new Model.Simple(entityModelSet.bakeLayer(ModelLayers.BED_FOOT), RenderTypes::entitySolid);
    }

    public static LayerDefinition createHeadLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 6.0f), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(50, 6).addBox(0.0f, 6.0f, 0.0f, 3.0f, 3.0f, 3.0f), PartPose.rotation(1.5707964f, 0.0f, 1.5707964f));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(50, 18).addBox(-16.0f, 6.0f, 0.0f, 3.0f, 3.0f, 3.0f), PartPose.rotation(1.5707964f, 0.0f, (float)Math.PI));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition createFootLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 22).addBox(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 6.0f), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(50, 0).addBox(0.0f, 6.0f, -16.0f, 3.0f, 3.0f, 3.0f), PartPose.rotation(1.5707964f, 0.0f, 0.0f));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(50, 12).addBox(-16.0f, 6.0f, -16.0f, 3.0f, 3.0f, 3.0f), PartPose.rotation(1.5707964f, 0.0f, 4.712389f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public BedRenderState createRenderState() {
        return new BedRenderState();
    }

    @Override
    public void extractRenderState(BedBlockEntity blockEntity, BedRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.color = blockEntity.getColor();
        state.facing = (Direction)blockEntity.getBlockState().getValue(BedBlock.FACING);
        state.part = blockEntity.getBlockState().getValue(BedBlock.PART);
        if (blockEntity.getLevel() != null) {
            DoubleBlockCombiner.NeighborCombineResult<BedBlockEntity> combineResult = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, blockEntity.getBlockState(), blockEntity.getLevel(), blockEntity.getBlockPos(), (levelAccessor, blockPos) -> false);
            state.lightCoords = ((Int2IntFunction)combineResult.apply(new BrightnessCombiner())).get(state.lightCoords);
        }
    }

    @Override
    public void submit(BedRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        SpriteId sprite = Sheets.getBedSprite(state.color);
        poseStack.pushPose();
        poseStack.mulPose(BedRenderer.modelTransform(state.facing));
        this.submitPiece(state.part, sprite, poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.breakProgress, 0);
        poseStack.popPose();
    }

    public void submitPiece(BedPart part, SpriteId sprite, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor) {
        submitNodeCollector.submitModel(this.getPieceModel(part), Unit.INSTANCE, poseStack, sprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, this.sprites.get(sprite), outlineColor, breakProgress);
    }

    private Model.Simple getPieceModel(BedPart part) {
        return switch (part) {
            default -> throw new MatchException(null, null);
            case BedPart.HEAD -> this.headModel;
            case BedPart.FOOT -> this.footModel;
        };
    }

    private static Transformation createModelTransform(Direction direction) {
        return new Transformation((Matrix4fc)new Matrix4f().translation(0.0f, 0.5625f, 0.0f).rotate((Quaternionfc)Axis.XP.rotationDegrees(90.0f)).rotateAround((Quaternionfc)Axis.ZP.rotationDegrees(180.0f + direction.toYRot()), 0.5f, 0.5f, 0.5f));
    }

    public static Transformation modelTransform(Direction direction) {
        return TRANSFORMATIONS.get(direction);
    }

    public void getExtents(BedPart part, Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.getPieceModel(part).root().getExtentsForGui(poseStack, output);
    }
}

