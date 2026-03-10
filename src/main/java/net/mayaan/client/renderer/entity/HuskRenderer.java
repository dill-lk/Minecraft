/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.ZombieRenderer;
import net.mayaan.client.renderer.entity.state.ZombieRenderState;
import net.mayaan.resources.Identifier;

public class HuskRenderer
extends ZombieRenderer {
    private static final Identifier HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/husk.png");
    private static final Identifier BABY_HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/husk_baby.png");

    public HuskRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.HUSK, ModelLayers.HUSK_BABY, ModelLayers.HUSK_ARMOR, ModelLayers.HUSK_BABY_ARMOR);
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return state.isBaby ? BABY_HUSK_LOCATION : HUSK_LOCATION;
    }
}

