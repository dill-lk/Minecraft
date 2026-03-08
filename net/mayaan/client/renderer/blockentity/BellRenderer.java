/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.bell.BellModel;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.state.BellRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.world.level.block.entity.BellBlockEntity;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BellRenderer
implements BlockEntityRenderer<BellBlockEntity, BellRenderState> {
    public static final SpriteId BELL_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.defaultNamespaceApply("bell/bell_body");
    private final SpriteGetter sprites;
    private final BellModel model;

    public BellRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new BellModel(context.bakeLayer(ModelLayers.BELL));
    }

    @Override
    public BellRenderState createRenderState() {
        return new BellRenderState();
    }

    @Override
    public void extractRenderState(BellBlockEntity blockEntity, BellRenderState state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.ticks = (float)blockEntity.ticks + partialTicks;
        state.shakeDirection = blockEntity.shaking ? blockEntity.clickDirection : null;
    }

    @Override
    public void submit(BellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        BellModel.State modelState = new BellModel.State(state.ticks, state.shakeDirection);
        this.model.setupAnim(modelState);
        RenderType renderType = BELL_TEXTURE.renderType(RenderTypes::entitySolid);
        submitNodeCollector.submitModel(this.model, modelState, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, this.sprites.get(BELL_TEXTURE), 0, state.breakProgress);
    }
}

