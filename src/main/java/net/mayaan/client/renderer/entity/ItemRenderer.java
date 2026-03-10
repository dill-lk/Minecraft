/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.QuadInstance;
import com.maayanlabs.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import com.maayanlabs.blaze3d.vertex.VertexMultiConsumer;
import com.maayanlabs.math.MatrixUtil;
import java.util.List;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.rendertype.OutputTarget;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.resources.model.geometry.BakedQuad;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;

public class ItemRenderer {
    public static final Identifier ENCHANTED_GLINT_ARMOR = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_armor.png");
    public static final Identifier ENCHANTED_GLINT_ITEM = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_item.png");
    public static final float SPECIAL_FOIL_UI_SCALE = 0.5f;
    public static final float SPECIAL_FOIL_FIRST_PERSON_SCALE = 0.75f;
    public static final float SPECIAL_FOIL_TEXTURE_SCALE = 0.0078125f;
    public static final int NO_TINT = -1;

    public static void renderItem(ItemDisplayContext type, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int overlayCoords, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType) {
        PoseStack.Pose pose = poseStack.last();
        PoseStack.Pose foilDecalPose = foilType == ItemStackRenderState.FoilType.SPECIAL ? ItemRenderer.computeFoilDecalPose(type, pose) : null;
        QuadInstance quadInstance = new QuadInstance();
        quadInstance.setLightCoords(lightCoords);
        quadInstance.setOverlayCoords(overlayCoords);
        for (BakedQuad quad : quads) {
            RenderType renderType = quad.spriteInfo().itemRenderType();
            quadInstance.setColor(ItemRenderer.getLayerColorSafe(tintLayers, quad));
            if (foilType != ItemStackRenderState.FoilType.NONE) {
                VertexConsumer foilBuffer = ItemRenderer.getFoilBuffer(bufferSource, renderType, foilDecalPose);
                foilBuffer.putBakedQuad(pose, quad, quadInstance);
            }
            bufferSource.getBuffer(renderType).putBakedQuad(pose, quad, quadInstance);
        }
    }

    private static VertexConsumer getFoilBuffer(MultiBufferSource bufferSource, RenderType renderType, @Nullable PoseStack.Pose foilDecalPose) {
        VertexConsumer foilBuffer = bufferSource.getBuffer(ItemRenderer.getFoilRenderType(renderType, true));
        if (foilDecalPose != null) {
            foilBuffer = new SheetedDecalTextureGenerator(foilBuffer, foilDecalPose, 0.0078125f);
        }
        return foilBuffer;
    }

    private static PoseStack.Pose computeFoilDecalPose(ItemDisplayContext type, PoseStack.Pose pose) {
        PoseStack.Pose foilDecalPose = pose.copy();
        if (type == ItemDisplayContext.GUI) {
            MatrixUtil.mulComponentWise(foilDecalPose.pose(), 0.5f);
        } else if (type.firstPerson()) {
            MatrixUtil.mulComponentWise(foilDecalPose.pose(), 0.75f);
        }
        return foilDecalPose;
    }

    public static VertexConsumer getFoilBuffer(MultiBufferSource bufferSource, RenderType renderType, boolean sheeted, boolean hasFoil) {
        if (hasFoil) {
            return VertexMultiConsumer.create(bufferSource.getBuffer(ItemRenderer.getFoilRenderType(renderType, sheeted)), bufferSource.getBuffer(renderType));
        }
        return bufferSource.getBuffer(renderType);
    }

    private static RenderType getFoilRenderType(RenderType baseRenderType, boolean sheeted) {
        if (ItemRenderer.useTransparentGlint(baseRenderType)) {
            return RenderTypes.glintTranslucent();
        }
        return sheeted ? RenderTypes.glint() : RenderTypes.entityGlint();
    }

    public static List<RenderType> getFoilRenderTypes(RenderType baseRenderType, boolean sheeted, boolean hasFoil) {
        if (hasFoil) {
            return List.of(baseRenderType, ItemRenderer.getFoilRenderType(baseRenderType, sheeted));
        }
        return List.of(baseRenderType);
    }

    private static boolean useTransparentGlint(RenderType renderType) {
        return Mayaan.useShaderTransparency() && renderType.outputTarget() == OutputTarget.ITEM_ENTITY_TARGET;
    }

    private static int getLayerColorSafe(int[] layers, int layer) {
        if (layer < 0 || layer >= layers.length) {
            return -1;
        }
        return layers[layer];
    }

    private static int getLayerColorSafe(int[] tintLayers, BakedQuad quad) {
        if (quad.isTinted()) {
            return ItemRenderer.getLayerColorSafe(tintLayers, quad.tintIndex());
        }
        return -1;
    }
}

