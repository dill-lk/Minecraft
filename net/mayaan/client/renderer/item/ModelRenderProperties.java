/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.item;

import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.resources.model.ModelBaker;
import net.mayaan.client.resources.model.ResolvedModel;
import net.mayaan.client.resources.model.cuboid.ItemTransforms;
import net.mayaan.client.resources.model.sprite.Material;
import net.mayaan.client.resources.model.sprite.TextureSlots;
import net.mayaan.world.item.ItemDisplayContext;

public record ModelRenderProperties(boolean usesBlockLight, Material.Baked particleMaterial, ItemTransforms transforms) {
    public static ModelRenderProperties fromResolvedModel(ModelBaker baker, ResolvedModel resolvedModel, TextureSlots textureSlots) {
        Material.Baked particleSprite = resolvedModel.resolveParticleMaterial(textureSlots, baker);
        return new ModelRenderProperties(resolvedModel.getTopGuiLight().lightLikeBlock(), particleSprite, resolvedModel.getTopTransforms());
    }

    public void applyToLayer(ItemStackRenderState.LayerRenderState layer, ItemDisplayContext displayContext) {
        layer.setUsesBlockLight(this.usesBlockLight);
        layer.setParticleMaterial(this.particleMaterial);
        layer.setItemTransform(this.transforms.getTransform(displayContext));
    }
}

