/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.MapRenderer;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.block.BlockModelResolver;
import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.ItemFrameRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.decoration.GlowItemFrame;
import net.mayaan.world.entity.decoration.HangingEntity;
import net.mayaan.world.entity.decoration.ItemFrame;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.saveddata.maps.MapId;
import net.mayaan.world.level.saveddata.maps.MapItemSavedData;
import net.mayaan.world.phys.Vec3;
import org.joml.Quaternionfc;

public class ItemFrameRenderer<T extends ItemFrame>
extends EntityRenderer<T, ItemFrameRenderState> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    public static final int GLOW_FRAME_BRIGHTNESS = 5;
    public static final int BRIGHT_MAP_LIGHT_ADJUSTMENT = 30;
    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;
    private final MapRenderer mapRenderer;

    public ItemFrameRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockModelResolver = context.getBlockModelResolver();
        this.itemModelResolver = context.getItemModelResolver();
        this.mapRenderer = context.getMapRenderer();
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        if (entity.is((GlowItemFrame)((Object)EntityType.GLOW_ITEM_FRAME))) {
            return Math.max(5, super.getBlockLightLevel(entity, blockPos));
        }
        return super.getBlockLightLevel(entity, blockPos);
    }

    @Override
    public void submit(ItemFrameRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float yRot;
        float xRot;
        super.submit(state, poseStack, submitNodeCollector, camera);
        poseStack.pushPose();
        Direction direction = state.direction;
        Vec3 renderOffset = this.getRenderOffset(state);
        poseStack.translate(-renderOffset.x(), -renderOffset.y(), -renderOffset.z());
        double offs = 0.46875;
        poseStack.translate((double)direction.getStepX() * 0.46875, (double)direction.getStepY() * 0.46875, (double)direction.getStepZ() * 0.46875);
        if (direction.getAxis().isHorizontal()) {
            xRot = 0.0f;
            yRot = 180.0f - direction.toYRot();
        } else {
            xRot = -90 * direction.getAxisDirection().getStep();
            yRot = 180.0f;
        }
        poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot));
        if (!state.frameModel.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.5f, -0.5f, -0.5f);
            state.frameModel.submitWithZOffset(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
        if (state.isInvisible) {
            poseStack.translate(0.0f, 0.0f, 0.5f);
        } else {
            poseStack.translate(0.0f, 0.0f, 0.4375f);
        }
        if (state.mapId != null) {
            int rotation = state.rotation % 4 * 2;
            poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees((float)rotation * 360.0f / 8.0f));
            poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0f));
            float s = 0.0078125f;
            poseStack.scale(0.0078125f, 0.0078125f, 0.0078125f);
            poseStack.translate(-64.0f, -64.0f, 0.0f);
            poseStack.translate(0.0f, 0.0f, -1.0f);
            int lightCoords = this.getLightCoords(state.isGlowFrame, 15728850, state.lightCoords);
            this.mapRenderer.render(state.mapRenderState, poseStack, submitNodeCollector, true, lightCoords);
        } else if (!state.item.isEmpty()) {
            poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees((float)state.rotation * 360.0f / 8.0f));
            int lightVal = this.getLightCoords(state.isGlowFrame, 0xF000F0, state.lightCoords);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            state.item.submit(poseStack, submitNodeCollector, lightVal, OverlayTexture.NO_OVERLAY, state.outlineColor);
        }
        poseStack.popPose();
    }

    private int getLightCoords(boolean isGlowFrame, int glowLightCoords, int originalLightCoords) {
        return isGlowFrame ? glowLightCoords : originalLightCoords;
    }

    @Override
    public Vec3 getRenderOffset(ItemFrameRenderState state) {
        return new Vec3((float)state.direction.getStepX() * 0.3f, -0.25, (float)state.direction.getStepZ() * 0.3f);
    }

    @Override
    protected boolean shouldShowName(T entity, double distanceToCameraSq) {
        return Mayaan.renderNames() && this.entityRenderDispatcher.crosshairPickEntity == entity && ((ItemFrame)entity).getItem().getCustomName() != null;
    }

    @Override
    protected Component getNameTag(T entity) {
        return ((ItemFrame)entity).getItem().getHoverName();
    }

    @Override
    public ItemFrameRenderState createRenderState() {
        return new ItemFrameRenderState();
    }

    @Override
    public void extractRenderState(T entity, ItemFrameRenderState state, float partialTicks) {
        MapItemSavedData mapData;
        MapId framedMapId;
        super.extractRenderState(entity, state, partialTicks);
        state.direction = ((HangingEntity)((Object)entity)).getDirection();
        ItemStack itemStack = ((ItemFrame)((Object)entity)).getItem();
        this.itemModelResolver.updateForNonLiving(state.item, itemStack, ItemDisplayContext.FIXED, (Entity)((Object)entity));
        state.rotation = ((ItemFrame)((Object)entity)).getRotation();
        state.isGlowFrame = entity.is((GlowItemFrame)((Object)EntityType.GLOW_ITEM_FRAME));
        state.mapId = null;
        if (!itemStack.isEmpty() && (framedMapId = ((ItemFrame)((Object)entity)).getFramedMapId(itemStack)) != null && (mapData = ((Entity)((Object)entity)).level().getMapData(framedMapId)) != null) {
            this.mapRenderer.extractRenderState(framedMapId, mapData, state.mapRenderState);
            state.mapId = framedMapId;
        }
        if (!state.isInvisible) {
            this.blockModelResolver.updateForItemFrame(state.frameModel, state.isGlowFrame, state.mapId != null);
        } else {
            state.frameModel.clear();
        }
    }
}

