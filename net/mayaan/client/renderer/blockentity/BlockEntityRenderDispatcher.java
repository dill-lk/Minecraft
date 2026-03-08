/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.function.Supplier;
import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.client.gui.Font;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.renderer.PlayerSkinRenderCache;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.block.BlockModelResolver;
import net.mayaan.client.renderer.block.BlockRenderDispatcher;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderers;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.entity.EntityRenderDispatcher;
import net.mayaan.client.renderer.entity.ItemRenderer;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.ResourceManagerReloadListener;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BlockEntityRenderDispatcher
implements ResourceManagerReloadListener {
    private Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>> renderers = ImmutableMap.of();
    private final Font font;
    private final Supplier<EntityModelSet> entityModelSet;
    private Vec3 cameraPos;
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;
    private final ItemRenderer itemRenderer;
    private final EntityRenderDispatcher entityRenderer;
    private final SpriteGetter sprites;
    private final PlayerSkinRenderCache playerSkinRenderCache;

    public BlockEntityRenderDispatcher(Font font, Supplier<EntityModelSet> entityModelSet, BlockRenderDispatcher blockRenderDispatcher, BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, ItemRenderer itemRenderer, EntityRenderDispatcher entityRenderer, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache) {
        this.itemRenderer = itemRenderer;
        this.blockModelResolver = blockModelResolver;
        this.itemModelResolver = itemModelResolver;
        this.entityRenderer = entityRenderer;
        this.font = font;
        this.entityModelSet = entityModelSet;
        this.blockRenderDispatcher = blockRenderDispatcher;
        this.sprites = sprites;
        this.playerSkinRenderCache = playerSkinRenderCache;
    }

    public <E extends BlockEntity, S extends BlockEntityRenderState> @Nullable BlockEntityRenderer<E, S> getRenderer(E blockEntity) {
        return this.renderers.get(blockEntity.getType());
    }

    public <E extends BlockEntity, S extends BlockEntityRenderState> @Nullable BlockEntityRenderer<E, S> getRenderer(S state) {
        return this.renderers.get(state.blockEntityType);
    }

    public void prepare(Vec3 cameraPos) {
        this.cameraPos = cameraPos;
    }

    public <E extends BlockEntity, S extends BlockEntityRenderState> @Nullable S tryExtractRenderState(E blockEntity, float partialTicks,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer<E, E> renderer = this.getRenderer((S)((Object)blockEntity));
        if (renderer == null) {
            return null;
        }
        if (!blockEntity.hasLevel() || !blockEntity.getType().isValid(blockEntity.getBlockState())) {
            return null;
        }
        if (!renderer.shouldRender(blockEntity, this.cameraPos)) {
            return null;
        }
        Vec3 cameraPosition = this.cameraPos;
        E state = renderer.createRenderState();
        renderer.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        return (S)state;
    }

    public <S extends BlockEntityRenderState> void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        BlockEntityRenderer renderer = this.getRenderer(state);
        if (renderer == null) {
            return;
        }
        try {
            renderer.submit(state, poseStack, submitNodeCollector, camera);
        }
        catch (Throwable t) {
            CrashReport report = CrashReport.forThrowable(t, "Rendering Block Entity");
            CrashReportCategory category = report.addCategory("Block Entity Details");
            state.fillCrashReportCategory(category);
            throw new ReportedException(report);
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        BlockEntityRendererProvider.Context context = new BlockEntityRendererProvider.Context(this, this.blockRenderDispatcher, this.blockModelResolver, this.itemModelResolver, this.itemRenderer, this.entityRenderer, this.entityModelSet.get(), this.font, this.sprites, this.playerSkinRenderCache);
        this.renderers = BlockEntityRenderers.createEntityRenderers(context);
    }
}

