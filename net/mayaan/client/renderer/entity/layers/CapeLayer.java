/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.HumanoidModel;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.player.PlayerCapeModel;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.AvatarRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.resources.model.EquipmentAssetManager;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.core.component.DataComponents;
import net.mayaan.world.entity.player.PlayerSkin;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.equipment.Equippable;

public class CapeLayer
extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final HumanoidModel<AvatarRenderState> model;
    private final EquipmentAssetManager equipmentAssets;

    public CapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet, EquipmentAssetManager equipmentAssets) {
        super(renderer);
        this.model = new PlayerCapeModel(modelSet.bakeLayer(ModelLayers.PLAYER_CAPE));
        this.equipmentAssets = equipmentAssets;
    }

    private boolean hasLayer(ItemStack itemStack, EquipmentClientInfo.LayerType layerType) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.assetId().isEmpty()) {
            return false;
        }
        EquipmentClientInfo equipmentClientInfo = this.equipmentAssets.get(equippable.assetId().get());
        return !equipmentClientInfo.getLayers(layerType).isEmpty();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (state.isInvisible || !state.showCape) {
            return;
        }
        PlayerSkin skin = state.skin;
        if (skin.cape() == null) {
            return;
        }
        if (this.hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) {
            return;
        }
        poseStack.pushPose();
        if (this.hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID)) {
            poseStack.translate(0.0f, -0.053125f, 0.06875f);
        }
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entitySolid(skin.cape().texturePath()), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
    }
}

