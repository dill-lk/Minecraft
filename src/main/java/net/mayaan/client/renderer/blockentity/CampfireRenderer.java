/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.state.CampfireRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.Direction;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.level.block.CampfireBlock;
import net.mayaan.world.level.block.entity.CampfireBlockEntity;
import net.mayaan.world.phys.Vec3;
import org.joml.Quaternionfc;
import org.jspecify.annotations.Nullable;

public class CampfireRenderer
implements BlockEntityRenderer<CampfireBlockEntity, CampfireRenderState> {
    private static final float SIZE = 0.375f;
    private final ItemModelResolver itemModelResolver;

    public CampfireRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CampfireRenderState createRenderState() {
        return new CampfireRenderState();
    }

    @Override
    public void extractRenderState(CampfireBlockEntity blockEntity, CampfireRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.facing = blockEntity.getBlockState().getValue(CampfireBlock.FACING);
        int seed = (int)blockEntity.getBlockPos().asLong();
        state.items = new ArrayList<ItemStackRenderState>();
        for (int slot = 0; slot < blockEntity.getItems().size(); ++slot) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemState, blockEntity.getItems().get(slot), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + slot);
            state.items.add(itemState);
        }
    }

    @Override
    public void submit(CampfireRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        Direction facing = state.facing;
        List<ItemStackRenderState> items = state.items;
        for (int slot = 0; slot < items.size(); ++slot) {
            ItemStackRenderState itemState = items.get(slot);
            if (itemState.isEmpty()) continue;
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.44921875f, 0.5f);
            Direction direction = Direction.from2DDataValue((slot + facing.get2DDataValue()) % 4);
            float angle = -direction.toYRot();
            poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(angle));
            poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0f));
            poseStack.translate(-0.3125f, -0.3125f, 0.0f);
            poseStack.scale(0.375f, 0.375f, 0.375f);
            itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}

