/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface BlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> {
    public S createRenderState();

    default public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);
    }

    public void submit(S var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4);

    default public boolean shouldRenderOffScreen() {
        return false;
    }

    default public int getViewDistance() {
        return 64;
    }

    default public boolean shouldRender(T blockEntity, Vec3 cameraPosition) {
        return Vec3.atCenterOf(((BlockEntity)blockEntity).getBlockPos()).closerThan(cameraPosition, this.getViewDistance());
    }
}

