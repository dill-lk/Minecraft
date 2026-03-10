/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.Maps
 */
package net.mayaan.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.mayaan.client.renderer.blockentity.BannerRenderer;
import net.mayaan.client.renderer.blockentity.BeaconRenderer;
import net.mayaan.client.renderer.blockentity.BedRenderer;
import net.mayaan.client.renderer.blockentity.BellRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRenderer;
import net.mayaan.client.renderer.blockentity.BlockEntityRendererProvider;
import net.mayaan.client.renderer.blockentity.BlockEntityWithBoundingBoxRenderer;
import net.mayaan.client.renderer.blockentity.BrushableBlockRenderer;
import net.mayaan.client.renderer.blockentity.CampfireRenderer;
import net.mayaan.client.renderer.blockentity.ChestRenderer;
import net.mayaan.client.renderer.blockentity.ConduitRenderer;
import net.mayaan.client.renderer.blockentity.CopperGolemStatueBlockRenderer;
import net.mayaan.client.renderer.blockentity.DecoratedPotRenderer;
import net.mayaan.client.renderer.blockentity.EnchantTableRenderer;
import net.mayaan.client.renderer.blockentity.HangingSignRenderer;
import net.mayaan.client.renderer.blockentity.LecternRenderer;
import net.mayaan.client.renderer.blockentity.PistonHeadRenderer;
import net.mayaan.client.renderer.blockentity.ShelfRenderer;
import net.mayaan.client.renderer.blockentity.ShulkerBoxRenderer;
import net.mayaan.client.renderer.blockentity.SkullBlockRenderer;
import net.mayaan.client.renderer.blockentity.SpawnerRenderer;
import net.mayaan.client.renderer.blockentity.StandingSignRenderer;
import net.mayaan.client.renderer.blockentity.TestInstanceRenderer;
import net.mayaan.client.renderer.blockentity.TheEndGatewayRenderer;
import net.mayaan.client.renderer.blockentity.TheEndPortalRenderer;
import net.mayaan.client.renderer.blockentity.TrialSpawnerRenderer;
import net.mayaan.client.renderer.blockentity.VaultRenderer;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;

public class BlockEntityRenderers {
    private static final Map<BlockEntityType<?>, BlockEntityRendererProvider<?, ?>> PROVIDERS = Maps.newHashMap();

    private static <T extends BlockEntity, S extends BlockEntityRenderState> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T, S> renderer) {
        PROVIDERS.put(type, renderer);
    }

    public static Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>> createEntityRenderers(BlockEntityRendererProvider.Context context) {
        ImmutableMap.Builder result = ImmutableMap.builder();
        PROVIDERS.forEach((type, provider) -> {
            try {
                result.put(type, provider.create(context));
            }
            catch (Exception e) {
                throw new IllegalStateException("Failed to create model for " + String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey((BlockEntityType<?>)type)), e);
            }
        });
        return result.build();
    }

    static {
        BlockEntityRenderers.register(BlockEntityType.SIGN, StandingSignRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.HANGING_SIGN, HangingSignRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.MOB_SPAWNER, SpawnerRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.PISTON, context -> new PistonHeadRenderer());
        BlockEntityRenderers.register(BlockEntityType.CHEST, ChestRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.ENDER_CHEST, ChestRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.TRAPPED_CHEST, ChestRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.ENCHANTING_TABLE, EnchantTableRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.LECTERN, LecternRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.END_PORTAL, context -> new TheEndPortalRenderer());
        BlockEntityRenderers.register(BlockEntityType.END_GATEWAY, context -> new TheEndGatewayRenderer());
        BlockEntityRenderers.register(BlockEntityType.BEACON, context -> new BeaconRenderer());
        BlockEntityRenderers.register(BlockEntityType.SKULL, SkullBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.BANNER, BannerRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.STRUCTURE_BLOCK, context -> new BlockEntityWithBoundingBoxRenderer());
        BlockEntityRenderers.register(BlockEntityType.TEST_INSTANCE_BLOCK, context -> new TestInstanceRenderer());
        BlockEntityRenderers.register(BlockEntityType.SHULKER_BOX, ShulkerBoxRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.BED, BedRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.CONDUIT, ConduitRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.BELL, BellRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.CAMPFIRE, CampfireRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.BRUSHABLE_BLOCK, BrushableBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.DECORATED_POT, DecoratedPotRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.TRIAL_SPAWNER, TrialSpawnerRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.VAULT, VaultRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.COPPER_GOLEM_STATUE, CopperGolemStatueBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.SHELF, ShelfRenderer::new);
    }
}

