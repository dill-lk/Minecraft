/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.gui.Font;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.MapRenderer;
import net.mayaan.client.renderer.PlayerSkinRenderCache;
import net.mayaan.client.renderer.block.BlockModelResolver;
import net.mayaan.client.renderer.entity.EntityRenderDispatcher;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.texture.TextureAtlas;
import net.mayaan.client.resources.model.EquipmentAssetManager;
import net.mayaan.client.resources.model.sprite.AtlasManager;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.data.AtlasIds;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.world.entity.Entity;

@FunctionalInterface
public interface EntityRendererProvider<T extends Entity> {
    public EntityRenderer<T, ?> create(Context var1);

    public static class Context {
        private final EntityRenderDispatcher entityRenderDispatcher;
        private final BlockModelResolver blockModelResolver;
        private final ItemModelResolver itemModelResolver;
        private final MapRenderer mapRenderer;
        private final ResourceManager resourceManager;
        private final EntityModelSet modelSet;
        private final EquipmentAssetManager equipmentAssets;
        private final Font font;
        private final EquipmentLayerRenderer equipmentRenderer;
        private final AtlasManager atlasManager;
        private final PlayerSkinRenderCache playerSkinRenderCache;

        public Context(EntityRenderDispatcher entityRenderDispatcher, BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, MapRenderer mapRenderer, ResourceManager resourceManager, EntityModelSet modelSet, EquipmentAssetManager equipmentAssets, AtlasManager atlasManager, Font font, PlayerSkinRenderCache playerSkinRenderCache) {
            this.entityRenderDispatcher = entityRenderDispatcher;
            this.blockModelResolver = blockModelResolver;
            this.itemModelResolver = itemModelResolver;
            this.mapRenderer = mapRenderer;
            this.resourceManager = resourceManager;
            this.modelSet = modelSet;
            this.equipmentAssets = equipmentAssets;
            this.font = font;
            this.atlasManager = atlasManager;
            this.playerSkinRenderCache = playerSkinRenderCache;
            this.equipmentRenderer = new EquipmentLayerRenderer(equipmentAssets, atlasManager.getAtlasOrThrow(AtlasIds.ARMOR_TRIMS));
        }

        public EntityRenderDispatcher getEntityRenderDispatcher() {
            return this.entityRenderDispatcher;
        }

        public BlockModelResolver getBlockModelResolver() {
            return this.blockModelResolver;
        }

        public ItemModelResolver getItemModelResolver() {
            return this.itemModelResolver;
        }

        public MapRenderer getMapRenderer() {
            return this.mapRenderer;
        }

        public ResourceManager getResourceManager() {
            return this.resourceManager;
        }

        public EntityModelSet getModelSet() {
            return this.modelSet;
        }

        public EquipmentAssetManager getEquipmentAssets() {
            return this.equipmentAssets;
        }

        public EquipmentLayerRenderer getEquipmentRenderer() {
            return this.equipmentRenderer;
        }

        public SpriteGetter getSprites() {
            return this.atlasManager;
        }

        public TextureAtlas getAtlas(Identifier sheet) {
            return this.atlasManager.getAtlasOrThrow(sheet);
        }

        public ModelPart bakeLayer(ModelLayerLocation id) {
            return this.modelSet.bakeLayer(id);
        }

        public Font getFont() {
            return this.font;
        }

        public PlayerSkinRenderCache getPlayerSkinRenderCache() {
            return this.playerSkinRenderCache;
        }
    }
}

