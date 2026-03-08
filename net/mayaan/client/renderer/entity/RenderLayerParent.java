/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.EntityModel;
import net.mayaan.client.renderer.entity.state.EntityRenderState;

public interface RenderLayerParent<S extends EntityRenderState, M extends EntityModel<? super S>> {
    public M getModel();
}

