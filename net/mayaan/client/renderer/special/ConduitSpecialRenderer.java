/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.joml.Vector3fc
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.ConduitRenderer;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.special.NoDataSpecialModelRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

public class ConduitSpecialRenderer
implements NoDataSpecialModelRenderer {
    private final SpriteGetter sprites;
    private final ModelPart model;

    public ConduitSpecialRenderer(SpriteGetter sprites, ModelPart model) {
        this.sprites = sprites;
        this.model = model;
    }

    @Override
    public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.submitModelPart(this.model, poseStack, ConduitRenderer.SHELL_TEXTURE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(ConduitRenderer.SHELL_TEXTURE), false, false, -1, null, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit((Object)new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public ConduitSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new ConduitSpecialRenderer(context.sprites(), context.entityModelSet().bakeLayer(ModelLayers.CONDUIT_SHELL));
        }
    }
}

