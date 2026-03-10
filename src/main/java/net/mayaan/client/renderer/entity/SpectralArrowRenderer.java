/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.renderer.entity.ArrowRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.ArrowRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.arrow.SpectralArrow;

public class SpectralArrowRenderer
extends ArrowRenderer<SpectralArrow, ArrowRenderState> {
    public static final Identifier SPECTRAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow_spectral.png");

    public SpectralArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return SPECTRAL_ARROW_LOCATION;
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }
}

