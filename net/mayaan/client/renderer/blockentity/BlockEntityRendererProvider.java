/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity;

import net.mayaan.client.gui.Font;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.PlayerSkinRenderCache;
import net.mayaan.client.renderer.block.BlockModelResolver;
import net.mayaan.client.renderer.block.BlockRenderDispatcher;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.entity.EntityRenderDispatcher;
import net.mayaan.client.renderer.entity.ItemRenderer;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.world.level.block.entity.BlockEntity;

@FunctionalInterface
public interface BlockEntityRendererProvider<T extends BlockEntity, S extends BlockEntityRenderState> {
    public BlockEntityRenderer<T, S> create(Context var1);

    public record Context(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockRenderDispatcher blockRenderDispatcher, BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, ItemRenderer itemRenderer, EntityRenderDispatcher entityRenderer, EntityModelSet entityModelSet, Font font, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache) {
        public ModelPart bakeLayer(ModelLayerLocation id) {
            return this.entityModelSet.bakeLayer(id);
        }
    }
}

