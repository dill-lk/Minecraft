/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.renderer.entity.ArrowRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.TippableArrowRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.arrow.Arrow;

public class TippableArrowRenderer
extends ArrowRenderer<Arrow, TippableArrowRenderState> {
    public static final Identifier NORMAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
    public static final Identifier TIPPED_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow_tipped.png");

    public TippableArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(TippableArrowRenderState state) {
        return state.isTipped ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
    }

    @Override
    public TippableArrowRenderState createRenderState() {
        return new TippableArrowRenderState();
    }

    @Override
    public void extractRenderState(Arrow entity, TippableArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isTipped = entity.getColor() > 0;
    }
}

