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
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.state.VaultRenderState;
import net.mayaan.client.renderer.entity.ItemEntityRenderer;
import net.mayaan.client.renderer.entity.state.ItemClusterRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.entity.vault.VaultBlockEntity;
import net.mayaan.world.level.block.entity.vault.VaultClientData;
import net.mayaan.world.phys.Vec3;
import org.joml.Quaternionfc;
import org.jspecify.annotations.Nullable;

public class VaultRenderer
implements BlockEntityRenderer<VaultBlockEntity, VaultRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final RandomSource random = RandomSource.create();

    public VaultRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public VaultRenderState createRenderState() {
        return new VaultRenderState();
    }

    @Override
    public void extractRenderState(VaultBlockEntity blockEntity, VaultRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        ItemStack displayItem = blockEntity.getSharedData().getDisplayItem();
        if (!VaultBlockEntity.Client.shouldDisplayActiveEffects(blockEntity.getSharedData()) || displayItem.isEmpty() || blockEntity.getLevel() == null) {
            return;
        }
        state.displayItem = new ItemClusterRenderState();
        this.itemModelResolver.updateForTopItem(state.displayItem.item, displayItem, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        state.displayItem.count = ItemClusterRenderState.getRenderedAmount(displayItem.getCount());
        state.displayItem.seed = ItemClusterRenderState.getSeedForItemStack(displayItem);
        VaultClientData clientData = blockEntity.getClientData();
        state.spin = Mth.rotLerp(partialTicks, clientData.previousSpin(), clientData.currentSpin());
    }

    @Override
    public void submit(VaultRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.displayItem == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.4f, 0.5f);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.spin));
        ItemEntityRenderer.renderMultipleFromCount(poseStack, submitNodeCollector, state.lightCoords, state.displayItem, this.random);
        poseStack.popPose();
    }
}

