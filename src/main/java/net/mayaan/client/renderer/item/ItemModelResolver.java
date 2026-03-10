/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.ClientItem;
import net.mayaan.client.renderer.item.ItemModel;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.resources.model.ModelManager;
import net.mayaan.core.component.DataComponents;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class ItemModelResolver {
    private final ModelManager modelManager;

    public ItemModelResolver(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    private ClientItem.Properties getItemProperties(Identifier modelId) {
        return this.modelManager.getItemProperties(modelId);
    }

    private ItemModel getItemModel(Identifier modelId) {
        return this.modelManager.getItemModel(modelId);
    }

    public void updateForLiving(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, LivingEntity entity) {
        this.updateForTopItem(output, item, displayContext, entity.level(), entity, entity.getId() + displayContext.ordinal());
    }

    public void updateForNonLiving(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Entity entity) {
        this.updateForTopItem(output, item, displayContext, entity.level(), entity, entity.getId());
    }

    public void updateForTopItem(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, @Nullable Level level, @Nullable ItemOwner owner, int seed) {
        output.clear();
        if (!item.isEmpty()) {
            output.displayContext = displayContext;
            this.appendItemLayers(output, item, displayContext, level, owner, seed);
        }
    }

    public void appendItemLayers(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, @Nullable Level level, @Nullable ItemOwner owner, int seed) {
        ClientLevel clientLevel;
        Identifier modelId = item.get(DataComponents.ITEM_MODEL);
        if (modelId == null) {
            return;
        }
        output.setOversizedInGui(this.getItemProperties(modelId).oversizedInGui());
        this.getItemModel(modelId).update(output, item, this, displayContext, level instanceof ClientLevel ? (clientLevel = (ClientLevel)level) : null, owner, seed);
    }

    public boolean shouldPlaySwapAnimation(ItemStack stack) {
        Identifier modelId = stack.get(DataComponents.ITEM_MODEL);
        if (modelId == null) {
            return true;
        }
        return this.getItemProperties(modelId).handAnimationOnSwap();
    }

    public float swapAnimationScale(ItemStack stack) {
        Identifier modelId = stack.get(DataComponents.ITEM_MODEL);
        if (modelId == null) {
            return 1.0f;
        }
        return this.getItemProperties(modelId).swapAnimationScale();
    }
}

