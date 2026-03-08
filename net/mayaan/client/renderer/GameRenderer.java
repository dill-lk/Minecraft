/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.jtracy.TracyClient
 *  com.mojang.logging.LogUtils
 *  org.apache.commons.io.IOUtils
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fStack
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.client.renderer;

import com.maayanlabs.blaze3d.ProjectionType;
import com.maayanlabs.blaze3d.buffers.GpuBufferSlice;
import com.maayanlabs.blaze3d.pipeline.RenderTarget;
import com.maayanlabs.blaze3d.platform.Lighting;
import com.maayanlabs.blaze3d.platform.NativeImage;
import com.maayanlabs.blaze3d.platform.Window;
import com.maayanlabs.blaze3d.resource.CrossFrameResourcePool;
import com.maayanlabs.blaze3d.shaders.ShaderSource;
import com.maayanlabs.blaze3d.systems.GpuDevice;
import com.maayanlabs.blaze3d.systems.RenderSystem;
import com.maayanlabs.blaze3d.textures.GpuTextureView;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.jtracy.TracyClient;
import com.mojang.logging.LogUtils;
import com.maayanlabs.math.Axis;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.runtime.SwitchBootstraps;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.SharedConstants;
import net.mayaan.client.Camera;
import net.mayaan.client.DeltaTracker;
import net.mayaan.client.Mayaan;
import net.mayaan.client.Options;
import net.mayaan.client.Screenshot;
import net.mayaan.client.TextureFilteringMethod;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.debug.DebugScreenEntries;
import net.mayaan.client.gui.font.ActiveArea;
import net.mayaan.client.gui.font.EmptyArea;
import net.mayaan.client.gui.font.TextRenderable;
import net.mayaan.client.gui.render.GuiRenderer;
import net.mayaan.client.gui.render.TextureSetup;
import net.mayaan.client.gui.render.pip.GuiBannerResultRenderer;
import net.mayaan.client.gui.render.pip.GuiBookModelRenderer;
import net.mayaan.client.gui.render.pip.GuiEntityRenderer;
import net.mayaan.client.gui.render.pip.GuiProfilerChartRenderer;
import net.mayaan.client.gui.render.pip.GuiSignRenderer;
import net.mayaan.client.gui.render.pip.GuiSkinRenderer;
import net.mayaan.client.gui.screens.debug.DebugOptionsScreen;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.renderer.GlobalSettingsUniform;
import net.mayaan.client.renderer.ItemInHandRenderer;
import net.mayaan.client.renderer.LevelTargetBundle;
import net.mayaan.client.renderer.Lightmap;
import net.mayaan.client.renderer.LightmapRenderStateExtractor;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.Panorama;
import net.mayaan.client.renderer.PostChain;
import net.mayaan.client.renderer.Projection;
import net.mayaan.client.renderer.ProjectionMatrixBuffer;
import net.mayaan.client.renderer.RenderBuffers;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.renderer.ScreenEffectRenderer;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.UiLightmap;
import net.mayaan.client.renderer.block.BlockRenderDispatcher;
import net.mayaan.client.renderer.feature.FeatureRenderDispatcher;
import net.mayaan.client.renderer.fog.FogRenderer;
import net.mayaan.client.renderer.state.GameRenderState;
import net.mayaan.client.renderer.state.OptionsRenderState;
import net.mayaan.client.renderer.state.WindowRenderState;
import net.mayaan.client.renderer.state.gui.ColoredRectangleRenderState;
import net.mayaan.client.renderer.state.gui.GuiRenderState;
import net.mayaan.client.renderer.state.gui.GuiTextRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.renderer.texture.TextureManager;
import net.mayaan.client.resources.model.sprite.AtlasManager;
import net.mayaan.client.server.IntegratedServer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.chat.Style;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.ResourceProvider;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.util.profiling.Zone;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.monster.Creeper;
import net.mayaan.world.entity.monster.EnderMan;
import net.mayaan.world.entity.monster.spider.Spider;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.GameType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.pattern.BlockInWorld;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.waypoints.TrackedWaypoint;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class GameRenderer
implements AutoCloseable,
TrackedWaypoint.Projector {
    private static final Identifier BLUR_POST_CHAIN_ID = Identifier.withDefaultNamespace("blur");
    public static final int MAX_BLUR_RADIUS = 10;
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final float PROJECTION_3D_HUD_Z_FAR = 100.0f;
    private static final float PORTAL_SPINNING_SPEED = 20.0f;
    private static final float NAUSEA_SPINNING_SPEED = 7.0f;
    private final Mayaan minecraft;
    private final GameRenderState gameRenderState = new GameRenderState();
    private final RandomSource random = RandomSource.create();
    public final ItemInHandRenderer itemInHandRenderer;
    private final ScreenEffectRenderer screenEffectRenderer;
    private final RenderBuffers renderBuffers;
    private float spinningEffectTime;
    private float spinningEffectSpeed;
    private float bossOverlayWorldDarkening;
    private float bossOverlayWorldDarkeningO;
    private boolean renderBlockOutline = true;
    private long lastScreenshotAttempt;
    private boolean hasWorldScreenshot;
    private final Lightmap lightmap = new Lightmap();
    private final LightmapRenderStateExtractor lightmapRenderStateExtractor;
    private final UiLightmap uiLightmap = new UiLightmap();
    private boolean useUiLightmap;
    private final OverlayTexture overlayTexture = new OverlayTexture();
    protected final Panorama panorama = new Panorama();
    private final CrossFrameResourcePool resourcePool = new CrossFrameResourcePool(3);
    private final FogRenderer fogRenderer = new FogRenderer();
    private final GuiRenderer guiRenderer;
    private final SubmitNodeStorage submitNodeStorage;
    private final FeatureRenderDispatcher featureRenderDispatcher;
    private @Nullable Identifier postEffectId;
    private boolean effectActive;
    private final Camera mainCamera = new Camera();
    private final Projection hudProjection = new Projection();
    private final Lighting lighting = new Lighting();
    private final GlobalSettingsUniform globalSettingsUniform = new GlobalSettingsUniform();
    private final ProjectionMatrixBuffer levelProjectionMatrixBuffer = new ProjectionMatrixBuffer("level");
    private final ProjectionMatrixBuffer hud3dProjectionMatrixBuffer = new ProjectionMatrixBuffer("3d hud");

    public GameRenderer(Mayaan minecraft, ItemInHandRenderer itemInHandRenderer, RenderBuffers renderBuffers, BlockRenderDispatcher blockRenderer) {
        this.minecraft = minecraft;
        this.itemInHandRenderer = itemInHandRenderer;
        this.lightmapRenderStateExtractor = new LightmapRenderStateExtractor(this, minecraft);
        this.renderBuffers = renderBuffers;
        MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
        AtlasManager atlasManager = minecraft.getAtlasManager();
        this.submitNodeStorage = new SubmitNodeStorage();
        this.featureRenderDispatcher = new FeatureRenderDispatcher(this.submitNodeStorage, blockRenderer, bufferSource, atlasManager, renderBuffers.outlineBufferSource(), renderBuffers.crumblingBufferSource(), minecraft.font, this.gameRenderState);
        this.guiRenderer = new GuiRenderer(this.gameRenderState.guiRenderState, bufferSource, this.submitNodeStorage, this.featureRenderDispatcher, List.of(new GuiEntityRenderer(bufferSource, minecraft.getEntityRenderDispatcher()), new GuiSkinRenderer(bufferSource), new GuiBookModelRenderer(bufferSource), new GuiBannerResultRenderer(bufferSource, atlasManager), new GuiSignRenderer(bufferSource, atlasManager), new GuiProfilerChartRenderer(bufferSource)));
        this.screenEffectRenderer = new ScreenEffectRenderer(minecraft, atlasManager, bufferSource);
    }

    @Override
    public void close() {
        this.globalSettingsUniform.close();
        this.lightmap.close();
        this.overlayTexture.close();
        this.uiLightmap.close();
        this.resourcePool.close();
        this.guiRenderer.close();
        this.levelProjectionMatrixBuffer.close();
        this.hud3dProjectionMatrixBuffer.close();
        this.lighting.close();
        this.fogRenderer.close();
        this.featureRenderDispatcher.close();
    }

    public SubmitNodeStorage getSubmitNodeStorage() {
        return this.submitNodeStorage;
    }

    public FeatureRenderDispatcher getFeatureRenderDispatcher() {
        return this.featureRenderDispatcher;
    }

    public GameRenderState getGameRenderState() {
        return this.gameRenderState;
    }

    public void setRenderBlockOutline(boolean renderBlockOutline) {
        this.renderBlockOutline = renderBlockOutline;
    }

    public void clearPostEffect() {
        this.postEffectId = null;
        this.effectActive = false;
    }

    public void togglePostEffect() {
        this.effectActive = !this.effectActive;
    }

    public void checkEntityPostEffect(@Nullable Entity cameraEntity) {
        Entity entity = cameraEntity;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{Creeper.class, Spider.class, EnderMan.class}, (Entity)entity, n)) {
            case 0: {
                Creeper ignored = (Creeper)entity;
                this.setPostEffect(Identifier.withDefaultNamespace("creeper"));
                break;
            }
            case 1: {
                Spider ignored = (Spider)entity;
                this.setPostEffect(Identifier.withDefaultNamespace("spider"));
                break;
            }
            case 2: {
                EnderMan ignored = (EnderMan)entity;
                this.setPostEffect(Identifier.withDefaultNamespace("invert"));
                break;
            }
            default: {
                this.clearPostEffect();
            }
        }
    }

    private void setPostEffect(Identifier id) {
        this.postEffectId = id;
        this.effectActive = true;
    }

    public void processBlurEffect() {
        PostChain postChain = this.minecraft.getShaderManager().getPostChain(BLUR_POST_CHAIN_ID, LevelTargetBundle.MAIN_TARGETS);
        if (postChain != null) {
            postChain.process(this.minecraft.getMainRenderTarget(), this.resourcePool);
        }
    }

    public void preloadUiShader(ResourceProvider resourceProvider) {
        GpuDevice device = RenderSystem.getDevice();
        ShaderSource shaderSource = (id, type) -> {
            String string;
            block8: {
                Identifier location = type.idConverter().idToFile(id);
                BufferedReader reader = resourceProvider.getResourceOrThrow(location).openAsReader();
                try {
                    string = IOUtils.toString((Reader)reader);
                    if (reader == null) break block8;
                }
                catch (Throwable t$) {
                    try {
                        if (reader != null) {
                            try {
                                ((Reader)reader).close();
                            }
                            catch (Throwable x2) {
                                t$.addSuppressed(x2);
                            }
                        }
                        throw t$;
                    }
                    catch (IOException exception) {
                        LOGGER.error("Coudln't preload {} shader {}: {}", new Object[]{type, id, exception});
                        return null;
                    }
                }
                ((Reader)reader).close();
            }
            return string;
        };
        device.precompilePipeline(RenderPipelines.GUI, shaderSource);
        device.precompilePipeline(RenderPipelines.GUI_TEXTURED, shaderSource);
        if (TracyClient.isAvailable()) {
            device.precompilePipeline(RenderPipelines.TRACY_BLIT, shaderSource);
        }
    }

    public void tick() {
        this.lightmapRenderStateExtractor.tick();
        LocalPlayer player = this.minecraft.player;
        if (this.mainCamera.entity() == null) {
            this.mainCamera.setEntity(player);
        }
        this.mainCamera.tick();
        this.itemInHandRenderer.tick();
        float portalIntensity = player.portalEffectIntensity;
        float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, 1.0f);
        if (portalIntensity > 0.0f || nauseaIntensity > 0.0f) {
            this.spinningEffectSpeed = (portalIntensity * 20.0f + nauseaIntensity * 7.0f) / (portalIntensity + nauseaIntensity);
            this.spinningEffectTime += this.spinningEffectSpeed;
        } else {
            this.spinningEffectSpeed = 0.0f;
        }
        if (!this.minecraft.level.tickRateManager().runsNormally()) {
            return;
        }
        this.bossOverlayWorldDarkeningO = this.bossOverlayWorldDarkening;
        if (this.minecraft.gui.getBossOverlay().shouldDarkenScreen()) {
            this.bossOverlayWorldDarkening += 0.05f;
            if (this.bossOverlayWorldDarkening > 1.0f) {
                this.bossOverlayWorldDarkening = 1.0f;
            }
        } else if (this.bossOverlayWorldDarkening > 0.0f) {
            this.bossOverlayWorldDarkening -= 0.0125f;
        }
        this.screenEffectRenderer.tick();
        ProfilerFiller profiler = Profiler.get();
        profiler.push("levelRenderer");
        this.minecraft.levelRenderer.tick(this.mainCamera);
        profiler.pop();
    }

    public @Nullable Identifier currentPostEffect() {
        return this.postEffectId;
    }

    public void resize(int width, int height) {
        this.resourcePool.clear();
        RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();
        mainRenderTarget.resize(width, height);
        this.minecraft.levelRenderer.resize(width, height);
    }

    private void bobHurt(CameraRenderState cameraState, PoseStack poseStack) {
        if (cameraState.entityRenderState.isLiving) {
            float hurt = cameraState.entityRenderState.hurtTime;
            if (cameraState.entityRenderState.isDeadOrDying) {
                float duration = Math.min(cameraState.entityRenderState.deathTime, 20.0f);
                poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(40.0f - 8000.0f / (duration + 200.0f)));
            }
            if (hurt < 0.0f) {
                return;
            }
            hurt /= (float)cameraState.entityRenderState.hurtDuration;
            hurt = Mth.sin(hurt * hurt * hurt * hurt * (float)Math.PI);
            float rr = cameraState.entityRenderState.hurtDir;
            poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-rr));
            float tiltAmount = (float)((double)(-hurt) * 14.0 * this.gameRenderState.optionsRenderState.damageTiltStrength);
            poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(tiltAmount));
            poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(rr));
        }
    }

    private void bobView(CameraRenderState cameraState, PoseStack poseStack) {
        if (!cameraState.entityRenderState.isPlayer) {
            return;
        }
        float backwardsInterpolatedWalkDistance = cameraState.entityRenderState.backwardsInterpolatedWalkDistance;
        float bob = cameraState.entityRenderState.bob;
        poseStack.translate(Mth.sin(backwardsInterpolatedWalkDistance * (float)Math.PI) * bob * 0.5f, -Math.abs(Mth.cos(backwardsInterpolatedWalkDistance * (float)Math.PI) * bob), 0.0f);
        poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(Mth.sin(backwardsInterpolatedWalkDistance * (float)Math.PI) * bob * 3.0f));
        poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Math.abs(Mth.cos(backwardsInterpolatedWalkDistance * (float)Math.PI - 0.2f) * bob) * 5.0f));
    }

    private void renderItemInHand(CameraRenderState cameraState, float deltaPartialTick, Matrix4f modelViewMatrix) {
        if (cameraState.isPanoramicMode) {
            return;
        }
        this.featureRenderDispatcher.renderAllFeatures();
        this.renderBuffers.bufferSource().endBatch();
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPose((Matrix4fc)modelViewMatrix.invert(new Matrix4f()));
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix().mul((Matrix4fc)modelViewMatrix);
        this.bobHurt(cameraState, poseStack);
        if (this.gameRenderState.optionsRenderState.bobView) {
            this.bobView(cameraState, poseStack);
        }
        if (this.gameRenderState.optionsRenderState.cameraType.isFirstPerson() && !cameraState.entityRenderState.isSleeping && !this.gameRenderState.optionsRenderState.hideGui && this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            this.itemInHandRenderer.renderHandsWithItems(deltaPartialTick, poseStack, this.minecraft.gameRenderer.getSubmitNodeStorage(), this.minecraft.player, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(this.minecraft.player, deltaPartialTick));
        }
        modelViewStack.popMatrix();
        poseStack.popPose();
    }

    public static float getNightVisionScale(LivingEntity camera, float a) {
        MobEffectInstance nightVision = camera.getEffect(MobEffects.NIGHT_VISION);
        if (!nightVision.endsWithin(200)) {
            return 1.0f;
        }
        return 0.7f + Mth.sin(((float)nightVision.getDuration() - a) * (float)Math.PI * 0.2f) * 0.3f;
    }

    public void update(DeltaTracker deltaTracker, boolean advanceGameTime) {
        boolean shouldRenderLevel;
        ProfilerFiller profiler = Profiler.get();
        profiler.push("camera");
        this.mainCamera.update(deltaTracker);
        profiler.pop();
        boolean resourcesLoaded = this.minecraft.isGameLoadFinished();
        boolean bl = shouldRenderLevel = resourcesLoaded && advanceGameTime && this.minecraft.level != null;
        if (shouldRenderLevel) {
            this.minecraft.levelRenderer.update(this.mainCamera);
        }
    }

    public void extract(DeltaTracker deltaTracker, boolean advanceGameTime) {
        boolean resourcesLoaded = this.minecraft.isGameLoadFinished();
        boolean shouldRenderLevel = resourcesLoaded && advanceGameTime && this.minecraft.level != null;
        float worldPartialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
        this.extractWindow();
        this.extractOptions();
        if (shouldRenderLevel) {
            this.lightmapRenderStateExtractor.extract(this.gameRenderState.lightmapRenderState, 1.0f);
            float cameraEntityPartialTicks = this.mainCamera.getCameraEntityPartialTicks(deltaTracker);
            this.extractCamera(deltaTracker, worldPartialTicks, cameraEntityPartialTicks);
            this.minecraft.levelRenderer.extractLevel(deltaTracker, this.mainCamera, worldPartialTicks);
        }
        this.extractGui(deltaTracker, shouldRenderLevel, resourcesLoaded);
    }

    public void render(DeltaTracker deltaTracker, boolean advanceGameTime) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("render");
        if (this.gameRenderState.windowRenderState.isResized) {
            this.resize(this.gameRenderState.windowRenderState.width, this.gameRenderState.windowRenderState.height);
        }
        RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(mainRenderTarget.getColorTexture(), this.gameRenderState.guiRenderState.clearColorOverride, mainRenderTarget.getDepthTexture(), 1.0);
        boolean resourcesLoaded = this.minecraft.isGameLoadFinished();
        boolean shouldRenderLevel = resourcesLoaded && advanceGameTime && this.minecraft.level != null;
        this.globalSettingsUniform.update(this.gameRenderState.windowRenderState.width, this.gameRenderState.windowRenderState.height, this.gameRenderState.optionsRenderState.glintStrength, this.minecraft.level == null ? 0L : this.minecraft.level.getGameTime(), deltaTracker, this.gameRenderState.optionsRenderState.menuBackgroundBlurriness, this.gameRenderState.levelRenderState.cameraRenderState.pos, this.gameRenderState.optionsRenderState.textureFiltering == TextureFilteringMethod.RGSS);
        if (shouldRenderLevel) {
            PostChain postChain;
            this.lightmap.render(this.gameRenderState.lightmapRenderState);
            profiler.push("world");
            this.renderLevel(deltaTracker);
            this.tryTakeScreenshotIfNeeded();
            this.minecraft.levelRenderer.doEntityOutline();
            if (this.postEffectId != null && this.effectActive && (postChain = this.minecraft.getShaderManager().getPostChain(this.postEffectId, LevelTargetBundle.MAIN_TARGETS)) != null) {
                postChain.process(this.minecraft.getMainRenderTarget(), this.resourcePool);
            }
            profiler.pop();
        }
        this.fogRenderer.endFrame();
        RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mainRenderTarget.getDepthTexture(), 1.0);
        this.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        this.useUiLightmap = true;
        profiler.push("gui");
        this.guiRenderer.render(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
        this.guiRenderer.endFrame();
        profiler.pop();
        this.useUiLightmap = false;
        this.submitNodeStorage.endFrame();
        this.featureRenderDispatcher.endFrame();
        this.resourcePool.endFrame();
        profiler.pop();
    }

    private void extractGui(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded) {
        ProfilerFiller profiler = Profiler.get();
        int xMouse = (int)this.minecraft.mouseHandler.getScaledXPos(this.minecraft.getWindow());
        int yMouse = (int)this.minecraft.mouseHandler.getScaledYPos(this.minecraft.getWindow());
        profiler.push("gui");
        this.gameRenderState.guiRenderState.reset();
        GuiGraphics graphics = new GuiGraphics(this.minecraft, this.gameRenderState.guiRenderState, xMouse, yMouse);
        if (shouldRenderLevel) {
            profiler.push("inGameGui");
            this.minecraft.gui.render(graphics, deltaTracker);
            profiler.pop();
        }
        if (this.minecraft.getOverlay() != null) {
            profiler.push("overlay");
            try {
                this.minecraft.getOverlay().render(graphics, xMouse, yMouse, deltaTracker.getGameTimeDeltaTicks());
            }
            catch (Throwable t) {
                CrashReport report = CrashReport.forThrowable(t, "Rendering overlay");
                CrashReportCategory category = report.addCategory("Overlay render details");
                category.setDetail("Overlay name", () -> this.minecraft.getOverlay().getClass().getCanonicalName());
                throw new ReportedException(report);
            }
            profiler.pop();
        } else if (resourcesLoaded && this.minecraft.screen != null) {
            profiler.push("screen");
            try {
                this.minecraft.screen.renderWithTooltipAndSubtitles(graphics, xMouse, yMouse, deltaTracker.getGameTimeDeltaTicks());
            }
            catch (Throwable t) {
                CrashReport report = CrashReport.forThrowable(t, "Rendering screen");
                CrashReportCategory category = report.addCategory("Screen render details");
                category.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
                this.minecraft.mouseHandler.fillMousePositionDetails(category, this.minecraft.getWindow());
                throw new ReportedException(report);
            }
            if (SharedConstants.DEBUG_CURSOR_POS) {
                this.minecraft.mouseHandler.drawDebugMouseInfo(this.minecraft.font, graphics);
            }
            try {
                if (this.minecraft.screen != null) {
                    this.minecraft.screen.handleDelayedNarration();
                }
            }
            catch (Throwable t) {
                CrashReport report = CrashReport.forThrowable(t, "Narrating screen");
                CrashReportCategory category = report.addCategory("Screen details");
                category.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
                throw new ReportedException(report);
            }
            profiler.pop();
        }
        if (shouldRenderLevel) {
            this.minecraft.gui.renderSavingIndicator(graphics, deltaTracker);
        }
        if (resourcesLoaded) {
            try (Zone ignored = profiler.zone("toasts");){
                this.minecraft.getToastManager().render(graphics);
            }
        }
        if (!(this.minecraft.screen instanceof DebugOptionsScreen)) {
            this.minecraft.gui.renderDebugOverlay(graphics);
        }
        this.minecraft.gui.renderDeferredSubtitles();
        if (SharedConstants.DEBUG_ACTIVE_TEXT_AREAS) {
            this.renderActiveTextDebug();
        }
        profiler.pop();
        graphics.applyCursor(this.minecraft.getWindow());
    }

    private void renderActiveTextDebug() {
        final GuiRenderState guiRenderState = this.gameRenderState.guiRenderState;
        guiRenderState.nextStratum();
        guiRenderState.forEachText(text -> text.ensurePrepared().visit(new Font.GlyphVisitor(){
            private int index;
            final /* synthetic */ GuiTextRenderState val$text;
            {
                this.val$text = guiTextRenderState;
                Objects.requireNonNull(this$0);
            }

            @Override
            public void acceptGlyph(TextRenderable.Styled glyph) {
                this.renderDebugMarkers(glyph, false);
            }

            @Override
            public void acceptEmptyArea(EmptyArea empty) {
                this.renderDebugMarkers(empty, true);
            }

            private void renderDebugMarkers(ActiveArea glyph, boolean isEmpty) {
                int intensity = (isEmpty ? 128 : 255) - (this.index++ & 1) * 64;
                Style style = glyph.style();
                int red = style.getClickEvent() != null ? intensity : 0;
                int green = style.getHoverEvent() != null ? intensity : 0;
                int blue = red == 0 || green == 0 ? intensity : 0;
                int color = ARGB.color(128, red, green, blue);
                guiRenderState.submitGuiElement(new ColoredRectangleRenderState(RenderPipelines.GUI, TextureSetup.noTexture(), this.val$text.pose, (int)glyph.activeLeft(), (int)glyph.activeTop(), (int)glyph.activeRight(), (int)glyph.activeBottom(), color, color, this.val$text.scissor));
            }
        }));
    }

    private void tryTakeScreenshotIfNeeded() {
        if (this.hasWorldScreenshot || !this.minecraft.isLocalServer()) {
            return;
        }
        long time = Util.getMillis();
        if (time - this.lastScreenshotAttempt < 1000L) {
            return;
        }
        this.lastScreenshotAttempt = time;
        IntegratedServer server = this.minecraft.getSingleplayerServer();
        if (server == null || server.isStopped()) {
            return;
        }
        server.getWorldScreenshotFile().ifPresent(path -> {
            if (Files.isRegularFile(path, new LinkOption[0])) {
                this.hasWorldScreenshot = true;
            } else {
                this.takeAutoScreenshot((Path)path);
            }
        });
    }

    private void takeAutoScreenshot(Path screenshotFile) {
        if (this.minecraft.levelRenderer.countRenderedSections() > 10 && this.minecraft.levelRenderer.hasRenderedAllSections()) {
            Screenshot.takeScreenshot(this.minecraft.getMainRenderTarget(), screenshot -> Util.ioPool().execute(() -> {
                int width = screenshot.getWidth();
                int height = screenshot.getHeight();
                int x = 0;
                int y = 0;
                if (width > height) {
                    x = (width - height) / 2;
                    width = height;
                } else {
                    y = (height - width) / 2;
                    height = width;
                }
                try (NativeImage scaled = new NativeImage(64, 64, false);){
                    screenshot.resizeSubRectTo(x, y, width, height, scaled);
                    scaled.writeToFile(screenshotFile);
                }
                catch (IOException e) {
                    LOGGER.warn("Couldn't save auto screenshot", (Throwable)e);
                }
                finally {
                    screenshot.close();
                }
            }));
        }
    }

    private boolean shouldRenderBlockOutline() {
        boolean renderOutline;
        if (!this.renderBlockOutline) {
            return false;
        }
        Entity cameraEntity = this.minecraft.getCameraEntity();
        boolean bl = renderOutline = cameraEntity instanceof Player && !this.minecraft.options.hideGui;
        if (renderOutline && !((Player)cameraEntity).getAbilities().mayBuild) {
            ItemStack itemStack = ((LivingEntity)cameraEntity).getMainHandItem();
            HitResult hitResult = this.minecraft.hitResult;
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
                BlockState blockState = this.minecraft.level.getBlockState(pos);
                if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
                    renderOutline = blockState.getMenuProvider(this.minecraft.level, pos) != null;
                } else {
                    BlockInWorld blockInWorld = new BlockInWorld(this.minecraft.level, pos, false);
                    HolderLookup.RegistryLookup blockRegistry = this.minecraft.level.registryAccess().lookupOrThrow(Registries.BLOCK);
                    renderOutline = !itemStack.isEmpty() && (itemStack.canBreakBlockInAdventureMode(blockInWorld) || itemStack.canPlaceOnBlockInAdventureMode(blockInWorld));
                }
            }
        }
        return renderOutline;
    }

    public void renderLevel(DeltaTracker deltaTracker) {
        float worldPartialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
        float cameraEntityPartialTicks = this.mainCamera.getCameraEntityPartialTicks(deltaTracker);
        LocalPlayer player = this.minecraft.player;
        ProfilerFiller profiler = Profiler.get();
        boolean renderOutline = this.shouldRenderBlockOutline();
        OptionsRenderState optionsState = this.gameRenderState.optionsRenderState;
        CameraRenderState cameraState = this.gameRenderState.levelRenderState.cameraRenderState;
        Matrix4f modelViewMatrix = cameraState.viewRotationMatrix;
        profiler.push("matrices");
        Matrix4f projectionMatrix = new Matrix4f((Matrix4fc)cameraState.projectionMatrix);
        PoseStack bobStack = new PoseStack();
        this.bobHurt(cameraState, bobStack);
        if (optionsState.bobView) {
            this.bobView(cameraState, bobStack);
        }
        projectionMatrix.mul((Matrix4fc)bobStack.last().pose());
        float screenEffectScale = optionsState.screenEffectScale;
        float portalIntensity = Mth.lerp(worldPartialTicks, player.oPortalEffectIntensity, player.portalEffectIntensity);
        float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, worldPartialTicks);
        float spinningEffectIntensity = Math.max(portalIntensity, nauseaIntensity) * (screenEffectScale * screenEffectScale);
        if (spinningEffectIntensity > 0.0f) {
            float skew = 5.0f / (spinningEffectIntensity * spinningEffectIntensity + 5.0f) - spinningEffectIntensity * 0.04f;
            skew *= skew;
            Vector3f axis = new Vector3f(0.0f, Mth.SQRT_OF_TWO / 2.0f, Mth.SQRT_OF_TWO / 2.0f);
            float angle = (this.spinningEffectTime + worldPartialTicks * this.spinningEffectSpeed) * ((float)Math.PI / 180);
            projectionMatrix.rotate(angle, (Vector3fc)axis);
            projectionMatrix.scale(1.0f / skew, 1.0f, 1.0f);
            projectionMatrix.rotate(-angle, (Vector3fc)axis);
        }
        RenderSystem.setProjectionMatrix(this.levelProjectionMatrixBuffer.getBuffer(projectionMatrix), ProjectionType.PERSPECTIVE);
        profiler.popPush("fog");
        this.fogRenderer.updateBuffer(cameraState.fogData);
        GpuBufferSlice terrainFog = this.fogRenderer.getBuffer(FogRenderer.FogMode.WORLD);
        profiler.popPush("level");
        boolean shouldCreateBossFog = this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        this.minecraft.levelRenderer.renderLevel(this.resourcePool, deltaTracker, renderOutline, cameraState, modelViewMatrix, terrainFog, cameraState.fogData.color, !shouldCreateBossFog, this.gameRenderState.levelRenderState.chunkSectionsToRender);
        profiler.popPush("hand");
        boolean isSleeping = cameraState.entityRenderState.isSleeping;
        this.hudProjection.setupPerspective(0.05f, 100.0f, cameraState.hudFov, this.gameRenderState.windowRenderState.width, this.gameRenderState.windowRenderState.height);
        RenderSystem.setProjectionMatrix(this.hud3dProjectionMatrixBuffer.getBuffer(this.hudProjection), ProjectionType.PERSPECTIVE);
        RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.minecraft.getMainRenderTarget().getDepthTexture(), 1.0);
        this.renderItemInHand(cameraState, cameraEntityPartialTicks, modelViewMatrix);
        profiler.popPush("screenEffects");
        MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
        this.screenEffectRenderer.renderScreenEffect(optionsState.cameraType.isFirstPerson(), isSleeping, worldPartialTicks, this.submitNodeStorage, optionsState.hideGui);
        this.featureRenderDispatcher.renderAllFeatures();
        bufferSource.endBatch();
        profiler.pop();
        RenderSystem.setShaderFog(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
        if (this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR) && optionsState.cameraType.isFirstPerson() && !optionsState.hideGui) {
            this.minecraft.getDebugOverlay().render3dCrosshair(cameraState, this.gameRenderState.windowRenderState.guiScale);
        }
    }

    private void extractWindow() {
        WindowRenderState windowState = this.gameRenderState.windowRenderState;
        Window window = this.minecraft.getWindow();
        windowState.width = window.getWidth();
        windowState.height = window.getHeight();
        windowState.guiScale = window.getGuiScale();
        windowState.appropriateLineWidth = window.getAppropriateLineWidth();
        windowState.isMinimized = window.isMinimized();
        windowState.isResized = window.isResized();
    }

    private void extractOptions() {
        OptionsRenderState optionsState = this.gameRenderState.optionsRenderState;
        Options options = this.minecraft.options;
        optionsState.cloudRange = options.cloudRange().get();
        optionsState.cutoutLeaves = options.cutoutLeaves().get();
        optionsState.improvedTransparency = options.improvedTransparency().get();
        optionsState.ambientOcclusion = options.ambientOcclusion().get();
        optionsState.menuBackgroundBlurriness = options.getMenuBackgroundBlurriness();
        optionsState.panoramaSpeed = options.panoramaSpeed().get();
        optionsState.maxAnisotropyValue = options.maxAnisotropyValue();
        optionsState.textureFiltering = options.textureFiltering().get();
        optionsState.bobView = options.bobView().get();
        optionsState.hideGui = options.hideGui;
        optionsState.screenEffectScale = options.screenEffectScale().get().floatValue();
        optionsState.glintSpeed = options.glintSpeed().get();
        optionsState.glintStrength = options.glintStrength().get();
        optionsState.damageTiltStrength = options.damageTiltStrength().get();
        optionsState.backgroundForChatOnly = options.backgroundForChatOnly().get();
        optionsState.textBackgroundOpacity = options.textBackgroundOpacity().get().floatValue();
        optionsState.cloudStatus = options.getCloudStatus();
        optionsState.cameraType = options.getCameraType();
        optionsState.renderDistance = options.getEffectiveRenderDistance();
    }

    private void extractCamera(DeltaTracker deltaTracker, float worldPartialTicks, float cameraEntityPartialTicks) {
        CameraRenderState cameraState = this.gameRenderState.levelRenderState.cameraRenderState;
        this.mainCamera.extractRenderState(cameraState, cameraEntityPartialTicks);
        cameraState.fogType = this.mainCamera.getFluidInCamera();
        cameraState.fogData = this.fogRenderer.setupFog(this.mainCamera, this.minecraft.options.getEffectiveRenderDistance(), deltaTracker, this.getBossOverlayWorldDarkening(worldPartialTicks), this.minecraft.level);
    }

    public void resetData() {
        this.screenEffectRenderer.resetItemActivation();
        this.minecraft.getMapTextureManager().resetData();
        this.mainCamera.reset();
        this.hasWorldScreenshot = false;
    }

    public void displayItemActivation(ItemStack itemStack) {
        this.screenEffectRenderer.displayItemActivation(itemStack, this.random);
    }

    public Mayaan getMinecraft() {
        return this.minecraft;
    }

    public float getBossOverlayWorldDarkening(float a) {
        return Mth.lerp(a, this.bossOverlayWorldDarkeningO, this.bossOverlayWorldDarkening);
    }

    public Camera getMainCamera() {
        return this.mainCamera;
    }

    public GpuTextureView lightmap() {
        return this.useUiLightmap ? this.uiLightmap.getTextureView() : this.lightmap.getTextureView();
    }

    public GpuTextureView levelLightmap() {
        return this.lightmap.getTextureView();
    }

    public OverlayTexture overlayTexture() {
        return this.overlayTexture;
    }

    @Override
    public Vec3 projectPointToScreen(Vec3 point) {
        Matrix4f mvp = this.mainCamera.getViewRotationProjectionMatrix(new Matrix4f());
        Vec3 camPos = this.mainCamera.position();
        Vec3 offset = point.subtract(camPos);
        Vector3f vector3f = mvp.transformProject(offset.toVector3f());
        return new Vec3((Vector3fc)vector3f);
    }

    @Override
    public double projectHorizonToScreen() {
        float xRot = this.mainCamera.xRot();
        if (xRot <= -90.0f) {
            return Double.NEGATIVE_INFINITY;
        }
        if (xRot >= 90.0f) {
            return Double.POSITIVE_INFINITY;
        }
        float fov = this.mainCamera.getFov();
        return Math.tan(xRot * ((float)Math.PI / 180)) / Math.tan(fov / 2.0f * ((float)Math.PI / 180));
    }

    public GlobalSettingsUniform getGlobalSettingsUniform() {
        return this.globalSettingsUniform;
    }

    public Lighting getLighting() {
        return this.lighting;
    }

    public void setLevel(@Nullable ClientLevel level) {
        if (level != null) {
            this.lighting.updateLevel(level.dimensionType().cardinalLightType());
        }
        this.mainCamera.setLevel(level);
    }

    public Panorama getPanorama() {
        return this.panorama;
    }

    public void registerPanoramaTextures(TextureManager textureManager) {
        this.guiRenderer.registerPanoramaTextures(textureManager);
    }
}

