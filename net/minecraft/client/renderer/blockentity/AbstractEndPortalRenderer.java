/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Set;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public abstract class AbstractEndPortalRenderer<T extends TheEndPortalBlockEntity, S extends EndPortalRenderState>
implements BlockEntityRenderer<T, S> {
    public static final Identifier END_SKY_LOCATION = Identifier.withDefaultNamespace("textures/environment/end_sky.png");
    public static final Identifier END_PORTAL_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_portal/end_portal.png");

    @Override
    public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        ((EndPortalRenderState)state).facesToShow.clear();
        for (Direction direction : Direction.values()) {
            if (!((TheEndPortalBlockEntity)blockEntity).shouldRenderFace(direction)) continue;
            ((EndPortalRenderState)state).facesToShow.add(direction);
        }
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        submitNodeCollector.submitCustomGeometry(poseStack, this.renderType(), (pose1, buffer) -> this.renderCube(state.facesToShow, pose1.pose(), buffer));
    }

    private void renderCube(Set<Direction> facesToShow, Matrix4f pose, VertexConsumer builder) {
        float offsetDown = this.getOffsetDown();
        float offsetUp = this.getOffsetUp();
        AbstractEndPortalRenderer.renderFace(facesToShow, pose, builder, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, Direction.SOUTH);
        AbstractEndPortalRenderer.renderFace(facesToShow, pose, builder, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Direction.NORTH);
        AbstractEndPortalRenderer.renderFace(facesToShow, pose, builder, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.EAST);
        AbstractEndPortalRenderer.renderFace(facesToShow, pose, builder, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.WEST);
        AbstractEndPortalRenderer.renderFace(facesToShow, pose, builder, 0.0f, 1.0f, offsetDown, offsetDown, 0.0f, 0.0f, 1.0f, 1.0f, Direction.DOWN);
        AbstractEndPortalRenderer.renderFace(facesToShow, pose, builder, 0.0f, 1.0f, offsetUp, offsetUp, 1.0f, 1.0f, 0.0f, 0.0f, Direction.UP);
    }

    private static void renderFace(Set<Direction> facesToShow, Matrix4f pose, VertexConsumer builder, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction face) {
        if (facesToShow.contains(face)) {
            builder.addVertex((Matrix4fc)pose, x1, y1, z1);
            builder.addVertex((Matrix4fc)pose, x2, y1, z2);
            builder.addVertex((Matrix4fc)pose, x2, y2, z3);
            builder.addVertex((Matrix4fc)pose, x1, y2, z4);
        }
    }

    protected float getOffsetUp() {
        return 0.75f;
    }

    protected float getOffsetDown() {
        return 0.375f;
    }

    protected RenderType renderType() {
        return RenderTypes.endPortal();
    }
}

