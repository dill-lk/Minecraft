/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.mayaan.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.mayaan.client.model.animal.llama.BabyLlamaModel;
import net.mayaan.client.model.animal.llama.LlamaModel;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.LlamaDecorLayer;
import net.mayaan.client.renderer.entity.state.LlamaRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.equine.Llama;

public class LlamaRenderer
extends AgeableMobRenderer<Llama, LlamaRenderState, LlamaModel> {
    private static final Map<Llama.Variant, Identifier> TEXTURES = Maps.newEnumMap(Map.of(Llama.Variant.CREAMY, Identifier.withDefaultNamespace("textures/entity/llama/llama_creamy.png"), Llama.Variant.WHITE, Identifier.withDefaultNamespace("textures/entity/llama/llama_white.png"), Llama.Variant.BROWN, Identifier.withDefaultNamespace("textures/entity/llama/llama_brown.png"), Llama.Variant.GRAY, Identifier.withDefaultNamespace("textures/entity/llama/llama_gray.png")));
    private static final Map<Llama.Variant, Identifier> BABY_TEXTURES = Maps.newEnumMap(Map.of(Llama.Variant.CREAMY, Identifier.withDefaultNamespace("textures/entity/llama/llama_creamy_baby.png"), Llama.Variant.WHITE, Identifier.withDefaultNamespace("textures/entity/llama/llama_white_baby.png"), Llama.Variant.BROWN, Identifier.withDefaultNamespace("textures/entity/llama/llama_brown_baby.png"), Llama.Variant.GRAY, Identifier.withDefaultNamespace("textures/entity/llama/llama_gray_baby.png")));

    public LlamaRenderer(EntityRendererProvider.Context context, ModelLayerLocation model, ModelLayerLocation babyModel) {
        super(context, new LlamaModel(context.bakeLayer(model)), new BabyLlamaModel(context.bakeLayer(babyModel)), 0.7f);
        this.addLayer(new LlamaDecorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTextureLocation(LlamaRenderState state) {
        Map<Llama.Variant, Identifier> textures = state.isBaby ? BABY_TEXTURES : TEXTURES;
        return textures.get(state.variant);
    }

    @Override
    public LlamaRenderState createRenderState() {
        return new LlamaRenderState();
    }

    @Override
    public void extractRenderState(Llama entity, LlamaRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.variant = entity.getVariant();
        state.hasChest = !entity.isBaby() && entity.hasChest();
        state.bodyItem = entity.getBodyArmorItem();
        state.isTraderLlama = entity.isTraderLlama();
    }
}

