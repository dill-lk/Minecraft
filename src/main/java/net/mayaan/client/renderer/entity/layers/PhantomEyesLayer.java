/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import net.mayaan.client.model.monster.phantom.PhantomModel;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.EyesLayer;
import net.mayaan.client.renderer.entity.state.PhantomRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.resources.Identifier;

public class PhantomEyesLayer
extends EyesLayer<PhantomRenderState, PhantomModel> {
    private static final RenderType PHANTOM_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/phantom/phantom_eyes.png"));

    public PhantomEyesLayer(RenderLayerParent<PhantomRenderState, PhantomModel> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return PHANTOM_EYES;
    }
}

