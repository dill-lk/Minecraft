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
import net.mayaan.client.model.object.bell.BellModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BellRenderer;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.special.NoDataSpecialModelRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

public class BellSpecialRenderer
implements NoDataSpecialModelRenderer {
    private static final BellModel.State STATE = new BellModel.State(0.0f, null);
    private final BellModel model;
    private final SpriteGetter sprites;

    public BellSpecialRenderer(SpriteGetter sprites, BellModel model) {
        this.sprites = sprites;
        this.model = model;
    }

    @Override
    public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        RenderType renderType = BellRenderer.BELL_TEXTURE.renderType(RenderTypes::entitySolid);
        submitNodeCollector.submitModel(this.model, STATE, poseStack, renderType, lightCoords, overlayCoords, -1, this.sprites.get(BellRenderer.BELL_TEXTURE), outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(STATE);
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit((Object)new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public BellSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new BellSpecialRenderer(context.sprites(), new BellModel(context.entityModelSet().bakeLayer(ModelLayers.BELL)));
        }
    }
}

