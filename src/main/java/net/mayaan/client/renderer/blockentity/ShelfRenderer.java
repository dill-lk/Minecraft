/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.HashCommon
 *  org.joml.Quaternionfc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.state.ShelfRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.NonNullList;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.ShelfBlock;
import net.mayaan.world.level.block.entity.ShelfBlockEntity;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;
import org.joml.Quaternionfc;
import org.jspecify.annotations.Nullable;

public class ShelfRenderer
implements BlockEntityRenderer<ShelfBlockEntity, ShelfRenderState> {
    private static final float ITEM_SIZE = 0.25f;
    private static final float ALIGN_ITEMS_TO_BOTTOM = -0.25f;
    private final ItemModelResolver itemModelResolver;

    public ShelfRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public ShelfRenderState createRenderState() {
        return new ShelfRenderState();
    }

    @Override
    public void extractRenderState(ShelfBlockEntity blockEntity, ShelfRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.alignToBottom = blockEntity.getAlignItemsToBottom();
        state.facing = blockEntity.getBlockState().getValue(ShelfBlock.FACING);
        NonNullList<ItemStack> items = blockEntity.getItems();
        int seed = HashCommon.long2int((long)blockEntity.getBlockPos().asLong());
        for (int slot = 0; slot < items.size(); ++slot) {
            ItemStack itemStack = items.get(slot);
            if (itemStack.isEmpty()) continue;
            ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.ON_SHELF, blockEntity.level(), blockEntity, seed + slot);
            state.items[slot] = itemStackRenderState;
        }
    }

    @Override
    public void submit(ShelfRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float yRot = state.facing.getAxis().isHorizontal() ? -state.facing.toYRot() : 180.0f;
        for (int slot = 0; slot < state.items.length; ++slot) {
            ItemStackRenderState itemStackRenderState = state.items[slot];
            if (itemStackRenderState == null) continue;
            this.submitItem(state, itemStackRenderState, poseStack, submitNodeCollector, slot, yRot);
        }
    }

    private void submitItem(ShelfRenderState state, ItemStackRenderState itemStackRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int slot, float yRot) {
        float itemSlotPosition = (float)(slot - 1) * 0.3125f;
        Vec3 itemOffset = new Vec3(itemSlotPosition, state.alignToBottom ? -0.25 : 0.0, -0.25);
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot));
        poseStack.translate(itemOffset);
        poseStack.scale(0.25f, 0.25f, 0.25f);
        AABB box = itemStackRenderState.getModelBoundingBox();
        double offsetY = -box.minY;
        if (!state.alignToBottom) {
            offsetY += -(box.maxY - box.minY) / 2.0;
        }
        poseStack.translate(0.0, offsetY, 0.0);
        itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}

