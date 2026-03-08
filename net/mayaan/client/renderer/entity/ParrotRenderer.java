/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.parrot.ParrotModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.ParrotRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.animal.parrot.Parrot;

public class ParrotRenderer
extends MobRenderer<Parrot, ParrotRenderState, ParrotModel> {
    private static final Identifier RED_BLUE = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_red_blue.png");
    private static final Identifier BLUE = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_blue.png");
    private static final Identifier GREEN = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_green.png");
    private static final Identifier YELLOW_BLUE = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_yellow_blue.png");
    private static final Identifier GREY = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_grey.png");

    public ParrotRenderer(EntityRendererProvider.Context context) {
        super(context, new ParrotModel(context.bakeLayer(ModelLayers.PARROT)), 0.3f);
    }

    @Override
    public Identifier getTextureLocation(ParrotRenderState state) {
        return ParrotRenderer.getVariantTexture(state.variant);
    }

    @Override
    public ParrotRenderState createRenderState() {
        return new ParrotRenderState();
    }

    @Override
    public void extractRenderState(Parrot entity, ParrotRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.variant = entity.getVariant();
        float flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        float flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        state.flapAngle = (Mth.sin(flap) + 1.0f) * flapSpeed;
        state.pose = ParrotModel.getPose(entity);
    }

    public static Identifier getVariantTexture(Parrot.Variant variant) {
        return switch (variant) {
            default -> throw new MatchException(null, null);
            case Parrot.Variant.RED_BLUE -> RED_BLUE;
            case Parrot.Variant.BLUE -> BLUE;
            case Parrot.Variant.GREEN -> GREEN;
            case Parrot.Variant.YELLOW_BLUE -> YELLOW_BLUE;
            case Parrot.Variant.GRAY -> GREY;
        };
    }
}

