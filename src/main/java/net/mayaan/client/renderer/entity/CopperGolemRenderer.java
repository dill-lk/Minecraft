/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import java.util.function.Function;
import net.mayaan.client.model.animal.golem.CopperGolemModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.block.BlockModelResolver;
import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.BlockDecorationLayer;
import net.mayaan.client.renderer.entity.layers.CustomHeadLayer;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.mayaan.client.renderer.entity.state.ArmedEntityRenderState;
import net.mayaan.client.renderer.entity.state.CopperGolemRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.core.component.DataComponents;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.golem.CopperGolem;
import net.mayaan.world.entity.animal.golem.CopperGolemOxidationLevels;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.BlockItemStateProperties;
import net.mayaan.world.level.block.state.BlockState;

public class CopperGolemRenderer
extends MobRenderer<CopperGolem, CopperGolemRenderState, CopperGolemModel> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelResolver blockModelResolver;

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel(context.bakeLayer(ModelLayers.COPPER_GOLEM)), 0.5f);
        this.blockModelResolver = context.getBlockModelResolver();
        this.addLayer(new LivingEntityEmissiveLayer<CopperGolemRenderState, CopperGolemModel>(this, CopperGolemRenderer.getEyeTextureLocationProvider(), (copperGolem, ageInTicks) -> 1.0f, new CopperGolemModel(context.bakeLayer(ModelLayers.COPPER_GOLEM)), RenderTypes::eyes, false));
        this.addLayer(new ItemInHandLayer<CopperGolemRenderState, CopperGolemModel>(this));
        this.addLayer(new BlockDecorationLayer<CopperGolemRenderState, CopperGolemModel>(this, s -> s.blockOnAntenna, ((CopperGolemModel)this.model)::applyBlockOnAntennaTransform));
        this.addLayer(new CustomHeadLayer<CopperGolemRenderState, CopperGolemModel>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
    }

    @Override
    public Identifier getTextureLocation(CopperGolemRenderState state) {
        return CopperGolemOxidationLevels.getOxidationLevel(state.weathering).texture();
    }

    private static Function<CopperGolemRenderState, Identifier> getEyeTextureLocationProvider() {
        return renderState -> CopperGolemOxidationLevels.getOxidationLevel(renderState.weathering).eyeTexture();
    }

    @Override
    public CopperGolemRenderState createRenderState() {
        return new CopperGolemRenderState();
    }

    @Override
    public void extractRenderState(CopperGolem entity, CopperGolemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTicks);
        state.weathering = entity.getWeatherState();
        state.copperGolemState = entity.getState();
        state.idleAnimationState.copyFrom(entity.getIdleAnimationState());
        state.interactionGetItem.copyFrom(entity.getInteractionGetItemAnimationState());
        state.interactionGetNoItem.copyFrom(entity.getInteractionGetNoItemAnimationState());
        state.interactionDropItem.copyFrom(entity.getInteractionDropItemAnimationState());
        state.interactionDropNoItem.copyFrom(entity.getInteractionDropNoItemAnimationState());
        ItemStack antennaItem = entity.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA);
        Item item = antennaItem.getItem();
        if (item instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)item;
            BlockItemStateProperties blockItemState = antennaItem.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            BlockState blockState = blockItemState.apply(blockItem.getBlock().defaultBlockState());
            this.blockModelResolver.update(state.blockOnAntenna, blockState, BLOCK_DISPLAY_CONTEXT);
        } else {
            state.blockOnAntenna.clear();
        }
    }
}

