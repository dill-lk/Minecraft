/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import java.util.Objects;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.illager.IllagerModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.IllagerRenderer;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.state.IllagerRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.illager.Vindicator;

public class VindicatorRenderer
extends IllagerRenderer<Vindicator, IllagerRenderState> {
    private static final Identifier VINDICATOR = Identifier.withDefaultNamespace("textures/entity/illager/vindicator.png");

    public VindicatorRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5f);
        this.addLayer(new ItemInHandLayer<IllagerRenderState, IllagerModel<IllagerRenderState>>(this, (RenderLayerParent)this){
            {
                Objects.requireNonNull(this$0);
                super(renderer);
            }

            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllagerRenderState state, float yRot, float xRot) {
                if (state.isAggressive) {
                    super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                }
            }
        });
    }

    @Override
    public Identifier getTextureLocation(IllagerRenderState state) {
        return VINDICATOR;
    }

    @Override
    public IllagerRenderState createRenderState() {
        return new IllagerRenderState();
    }
}

