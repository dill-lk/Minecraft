/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Transformation;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.projectile.TridentModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.special.NoDataSpecialModelRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.world.item.ItemDisplayContext;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class TridentSpecialRenderer
implements NoDataSpecialModelRenderer {
    public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(null, null, (Vector3fc)new Vector3f(1.0f, -1.0f, -1.0f), null);
    private final TridentModel model;

    public TridentSpecialRenderer(TridentModel model) {
        this.model = model;
    }

    @Override
    public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.submitModelPart(this.model.root(), poseStack, this.model.renderType(TridentModel.TEXTURE), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit((Object)new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public TridentSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new TridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }
    }
}

