/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.function.Function;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Unit;

public abstract class Model<S> {
    protected final ModelPart root;
    protected final Function<Identifier, RenderType> renderType;
    private final List<ModelPart> allParts;

    public Model(ModelPart root, Function<Identifier, RenderType> renderType) {
        this.root = root;
        this.renderType = renderType;
        this.allParts = root.getAllParts();
    }

    public final RenderType renderType(Identifier texture) {
        return this.renderType.apply(texture);
    }

    public final void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords, int color) {
        this.root().render(poseStack, buffer, lightCoords, overlayCoords, color);
    }

    public final void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords) {
        this.renderToBuffer(poseStack, buffer, lightCoords, overlayCoords, -1);
    }

    public final ModelPart root() {
        return this.root;
    }

    public final List<ModelPart> allParts() {
        return this.allParts;
    }

    public void setupAnim(S state) {
        this.resetPose();
    }

    public final void resetPose() {
        for (ModelPart part : this.allParts) {
            part.resetPose();
        }
    }

    public static class Simple
    extends Model<Unit> {
        public Simple(ModelPart root, Function<Identifier, RenderType> renderType) {
            super(root, renderType);
        }

        @Override
        public void setupAnim(Unit state) {
        }
    }
}

