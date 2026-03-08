/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.animal.llama.BabyLlamaModel;
import net.mayaan.client.model.animal.llama.LlamaModel;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.LlamaRenderState;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.core.component.DataComponents;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.equipment.EquipmentAsset;
import net.mayaan.world.item.equipment.EquipmentAssets;
import net.mayaan.world.item.equipment.Equippable;

public class LlamaDecorLayer
extends RenderLayer<LlamaRenderState, LlamaModel> {
    private final LlamaModel adultModel;
    private final LlamaModel babyModel;
    private final EquipmentLayerRenderer equipmentRenderer;

    public LlamaDecorLayer(RenderLayerParent<LlamaRenderState, LlamaModel> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer);
        this.equipmentRenderer = equipmentRenderer;
        this.adultModel = new LlamaModel(modelSet.bakeLayer(ModelLayers.LLAMA_DECOR));
        this.babyModel = new BabyLlamaModel(modelSet.bakeLayer(ModelLayers.LLAMA_BABY_DECOR));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LlamaRenderState state, float yRot, float xRot) {
        ItemStack itemStack = state.bodyItem;
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable != null && equippable.assetId().isPresent() && !state.isBaby) {
            this.renderEquipment(poseStack, submitNodeCollector, state, itemStack, equippable.assetId().get(), lightCoords);
        } else if (state.isTraderLlama) {
            this.renderEquipment(poseStack, submitNodeCollector, state, ItemStack.EMPTY, state.isBaby ? EquipmentAssets.TRADER_LLAMA_BABY : EquipmentAssets.TRADER_LLAMA, lightCoords);
        }
    }

    private void renderEquipment(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LlamaRenderState state, ItemStack itemStack, ResourceKey<EquipmentAsset> equipmentAssetId, int lightCoords) {
        LlamaModel model = state.isBaby ? this.babyModel : this.adultModel;
        this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.LLAMA_BODY, equipmentAssetId, model, state, itemStack, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
    }
}

