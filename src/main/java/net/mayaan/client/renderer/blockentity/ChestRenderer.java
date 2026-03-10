/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2IntFunction
 *  java.lang.MatchException
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import com.maayanlabs.math.Transformation;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.Map;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.chest.ChestModel;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.BrightnessCombiner;
import net.mayaan.client.renderer.blockentity.state.ChestRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.core.Direction;
import net.mayaan.util.SpecialDates;
import net.mayaan.util.Util;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.ChestBlock;
import net.mayaan.world.level.block.CopperChestBlock;
import net.mayaan.world.level.block.DoubleBlockCombiner;
import net.mayaan.world.level.block.WeatheringCopper;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.EnderChestBlockEntity;
import net.mayaan.world.level.block.entity.LidBlockEntity;
import net.mayaan.world.level.block.entity.TrappedChestBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.ChestType;
import net.mayaan.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.jspecify.annotations.Nullable;

public class ChestRenderer<T extends BlockEntity>
implements BlockEntityRenderer<T, ChestRenderState> {
    private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, ChestRenderer::createModelTransformation);
    private final SpriteGetter sprites;
    private final ChestModel singleModel;
    private final ChestModel doubleLeftModel;
    private final ChestModel doubleRightModel;
    private final boolean xmasTextures;

    public ChestRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.xmasTextures = ChestRenderer.xmasTextures();
        this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.doubleLeftModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        this.doubleRightModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }

    public static boolean xmasTextures() {
        return SpecialDates.isExtendedChristmas();
    }

    @Override
    public ChestRenderState createRenderState() {
        return new ChestRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ChestRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        DoubleBlockCombiner.NeighborCombineResult<Object> combineResult;
        Block block;
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        boolean hasLevel = ((BlockEntity)blockEntity).getLevel() != null;
        BlockState blockState = hasLevel ? ((BlockEntity)blockEntity).getBlockState() : (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        state.type = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        state.facing = blockState.getValue(ChestBlock.FACING);
        state.material = ChestRenderer.getChestMaterial(blockEntity, this.xmasTextures);
        if (hasLevel && (block = blockState.getBlock()) instanceof ChestBlock) {
            ChestBlock chestBlock = (ChestBlock)block;
            combineResult = chestBlock.combine(blockState, ((BlockEntity)blockEntity).getLevel(), ((BlockEntity)blockEntity).getBlockPos(), true);
        } else {
            combineResult = DoubleBlockCombiner.Combiner::acceptNone;
        }
        state.open = combineResult.apply(ChestBlock.opennessCombiner((LidBlockEntity)blockEntity)).get(partialTicks);
        if (state.type != ChestType.SINGLE) {
            state.lightCoords = ((Int2IntFunction)combineResult.apply(new BrightnessCombiner())).applyAsInt(state.lightCoords);
        }
    }

    @Override
    public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));
        float open = state.open;
        open = 1.0f - open;
        open = 1.0f - open * open * open;
        SpriteId spriteId = Sheets.chooseSprite(state.material, state.type);
        RenderType renderType = spriteId.renderType(RenderTypes::entityCutoutCull);
        TextureAtlasSprite sprite = this.sprites.get(spriteId);
        ChestModel model = switch (state.type) {
            default -> throw new MatchException(null, null);
            case ChestType.SINGLE -> this.singleModel;
            case ChestType.LEFT -> this.doubleLeftModel;
            case ChestType.RIGHT -> this.doubleRightModel;
        };
        submitNodeCollector.submitModel(model, Float.valueOf(open), poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, 0, state.breakProgress);
        poseStack.popPose();
    }

    private static ChestRenderState.ChestMaterialType getChestMaterial(BlockEntity entity, boolean xmasTextures) {
        Block block = entity.getBlockState().getBlock();
        if (block instanceof CopperChestBlock) {
            CopperChestBlock copperChestBlock = (CopperChestBlock)block;
            return switch (copperChestBlock.getState()) {
                default -> throw new MatchException(null, null);
                case WeatheringCopper.WeatherState.UNAFFECTED -> ChestRenderState.ChestMaterialType.COPPER_UNAFFECTED;
                case WeatheringCopper.WeatherState.EXPOSED -> ChestRenderState.ChestMaterialType.COPPER_EXPOSED;
                case WeatheringCopper.WeatherState.WEATHERED -> ChestRenderState.ChestMaterialType.COPPER_WEATHERED;
                case WeatheringCopper.WeatherState.OXIDIZED -> ChestRenderState.ChestMaterialType.COPPER_OXIDIZED;
            };
        }
        if (entity instanceof EnderChestBlockEntity) {
            return ChestRenderState.ChestMaterialType.ENDER_CHEST;
        }
        if (xmasTextures) {
            return ChestRenderState.ChestMaterialType.CHRISTMAS;
        }
        if (entity instanceof TrappedChestBlockEntity) {
            return ChestRenderState.ChestMaterialType.TRAPPED;
        }
        return ChestRenderState.ChestMaterialType.REGULAR;
    }

    public static Transformation modelTransformation(Direction facing) {
        return TRANSFORMATIONS.get(facing);
    }

    private static Transformation createModelTransformation(Direction facing) {
        return new Transformation((Matrix4fc)new Matrix4f().rotationAround((Quaternionfc)Axis.YP.rotationDegrees(-facing.toYRot()), 0.5f, 0.0f, 0.5f));
    }
}

