/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.GuardianRenderer;
import net.mayaan.client.renderer.entity.state.GuardianRenderState;
import net.mayaan.resources.Identifier;

public class ElderGuardianRenderer
extends GuardianRenderer {
    public static final Identifier GUARDIAN_ELDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/guardian/guardian_elder.png");

    public ElderGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, 1.2f, ModelLayers.ELDER_GUARDIAN);
    }

    @Override
    public Identifier getTextureLocation(GuardianRenderState state) {
        return GUARDIAN_ELDER_LOCATION;
    }
}

