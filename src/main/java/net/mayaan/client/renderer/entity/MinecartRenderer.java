/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.renderer.entity.AbstractMinecartRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.MinecartRenderState;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;

public class MinecartRenderer
extends AbstractMinecartRenderer<AbstractMinecart, MinecartRenderState> {
    public MinecartRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
        super(context, model);
    }

    @Override
    public MinecartRenderState createRenderState() {
        return new MinecartRenderState();
    }
}

