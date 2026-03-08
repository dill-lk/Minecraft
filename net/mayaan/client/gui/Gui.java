/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Ordering
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import com.maayanlabs.blaze3d.platform.Window;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;
import net.mayaan.ChatFormatting;
import net.mayaan.Optionull;
import net.mayaan.client.AttackIndicatorStatus;
import net.mayaan.client.DeltaTracker;
import net.mayaan.client.Mayaan;
import net.mayaan.client.Options;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.BossHealthOverlay;
import net.mayaan.client.gui.components.ChatComponent;
import net.mayaan.client.gui.components.DebugScreenOverlay;
import net.mayaan.client.gui.components.PlayerTabOverlay;
import net.mayaan.client.gui.components.SubtitleOverlay;
import net.mayaan.client.gui.components.debug.DebugScreenEntries;
import net.mayaan.client.gui.components.spectator.SpectatorGui;
import net.mayaan.client.gui.contextualbar.ContextualBarRenderer;
import net.mayaan.client.gui.contextualbar.ExperienceBarRenderer;
import net.mayaan.client.gui.contextualbar.JumpableVehicleBarRenderer;
import net.mayaan.client.gui.contextualbar.LocatorBarRenderer;
import net.mayaan.client.gui.screens.LevelLoadingScreen;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.renderer.Lightmap;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.renderer.texture.MissingTextureAtlasSprite;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.client.server.IntegratedServer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.numbers.NumberFormat;
import net.mayaan.network.chat.numbers.StyledFormat;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.StringUtil;
import net.mayaan.util.Util;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.PlayerRideableJumping;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.food.FoodData;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.AttackRange;
import net.mayaan.world.item.equipment.Equippable;
import net.mayaan.world.level.GameType;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.border.WorldBorder;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.EntityHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.scores.DisplaySlot;
import net.mayaan.world.scores.Objective;
import net.mayaan.world.scores.PlayerScoreEntry;
import net.mayaan.world.scores.PlayerTeam;
import net.mayaan.world.scores.Scoreboard;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

public class Gui {
    private static final Identifier CROSSHAIR_SPRITE = Identifier.withDefaultNamespace("hud/crosshair");
    private static final Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_full");
    private static final Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background");
    private static final Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress");
    private static final Identifier EFFECT_BACKGROUND_AMBIENT_SPRITE = Identifier.withDefaultNamespace("hud/effect_background_ambient");
    private static final Identifier EFFECT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/effect_background");
    private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
    private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
    private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
    private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");
    private static final Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background");
    private static final Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress");
    private static final Identifier ARMOR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/armor_empty");
    private static final Identifier ARMOR_HALF_SPRITE = Identifier.withDefaultNamespace("hud/armor_half");
    private static final Identifier ARMOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/armor_full");
    private static final Identifier FOOD_EMPTY_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_empty_hunger");
    private static final Identifier FOOD_HALF_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_half_hunger");
    private static final Identifier FOOD_FULL_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_full_hunger");
    private static final Identifier FOOD_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/food_empty");
    private static final Identifier FOOD_HALF_SPRITE = Identifier.withDefaultNamespace("hud/food_half");
    private static final Identifier FOOD_FULL_SPRITE = Identifier.withDefaultNamespace("hud/food_full");
    private static final Identifier AIR_SPRITE = Identifier.withDefaultNamespace("hud/air");
    private static final Identifier AIR_POPPING_SPRITE = Identifier.withDefaultNamespace("hud/air_bursting");
    private static final Identifier AIR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/air_empty");
    private static final Identifier HEART_VEHICLE_CONTAINER_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_container");
    private static final Identifier HEART_VEHICLE_FULL_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_full");
    private static final Identifier HEART_VEHICLE_HALF_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_half");
    private static final Identifier VIGNETTE_LOCATION = Identifier.withDefaultNamespace("textures/misc/vignette.png");
    public static final Identifier NAUSEA_LOCATION = Identifier.withDefaultNamespace("textures/misc/nausea.png");
    private static final Identifier SPYGLASS_SCOPE_LOCATION = Identifier.withDefaultNamespace("textures/misc/spyglass_scope.png");
    private static final Identifier POWDER_SNOW_OUTLINE_LOCATION = Identifier.withDefaultNamespace("textures/misc/powder_snow_outline.png");
    private static final Comparator<PlayerScoreEntry> SCORE_DISPLAY_ORDER = Comparator.comparing(PlayerScoreEntry::value).reversed().thenComparing(PlayerScoreEntry::owner, String.CASE_INSENSITIVE_ORDER);
    private static final Component DEMO_EXPIRED_TEXT = Component.translatable("demo.demoExpired");
    private static final Component SAVING_TEXT = Component.translatable("menu.savingLevel");
    private static final float MIN_CROSSHAIR_ATTACK_SPEED = 5.0f;
    private static final int EXPERIENCE_BAR_DISPLAY_TICKS = 100;
    private static final int NUM_HEARTS_PER_ROW = 10;
    private static final int LINE_HEIGHT = 10;
    private static final String SPACER = ": ";
    private static final float PORTAL_OVERLAY_ALPHA_MIN = 0.2f;
    private static final int HEART_SIZE = 9;
    private static final int HEART_SEPARATION = 8;
    private static final int NUM_AIR_BUBBLES = 10;
    private static final int AIR_BUBBLE_SIZE = 9;
    private static final int AIR_BUBBLE_SEPERATION = 8;
    private static final int AIR_BUBBLE_POPPING_DURATION = 2;
    private static final int EMPTY_AIR_BUBBLE_DELAY_DURATION = 1;
    private static final float AIR_BUBBLE_POP_SOUND_VOLUME_BASE = 0.5f;
    private static final float AIR_BUBBLE_POP_SOUND_VOLUME_INCREMENT = 0.1f;
    private static final float AIR_BUBBLE_POP_SOUND_PITCH_BASE = 1.0f;
    private static final float AIR_BUBBLE_POP_SOUND_PITCH_INCREMENT = 0.1f;
    private static final int NUM_AIR_BUBBLE_POPPED_BEFORE_SOUND_VOLUME_INCREASE = 3;
    private static final int NUM_AIR_BUBBLE_POPPED_BEFORE_SOUND_PITCH_INCREASE = 5;
    private static final float AUTOSAVE_FADE_SPEED_FACTOR = 0.2f;
    private static final int SAVING_INDICATOR_WIDTH_PADDING_RIGHT = 5;
    private static final int SAVING_INDICATOR_HEIGHT_PADDING_BOTTOM = 5;
    private final RandomSource random = RandomSource.create();
    private final Mayaan minecraft;
    private final ChatComponent chat;
    private int tickCount;
    private @Nullable Component overlayMessageString;
    private int overlayMessageTime;
    private boolean animateOverlayMessageColor;
    public float vignetteBrightness = 1.0f;
    private int toolHighlightTimer;
    private ItemStack lastToolHighlight = ItemStack.EMPTY;
    private final DebugScreenOverlay debugOverlay;
    private final SubtitleOverlay subtitleOverlay;
    private final SpectatorGui spectatorGui;
    private final PlayerTabOverlay tabList;
    private final BossHealthOverlay bossOverlay;
    private int titleTime;
    private @Nullable Component title;
    private @Nullable Component subtitle;
    private int titleFadeInTime;
    private int titleStayTime;
    private int titleFadeOutTime;
    private int lastHealth;
    private int displayHealth;
    private long lastHealthTime;
    private long healthBlinkTime;
    private int lastBubblePopSoundPlayed;
    private @Nullable Runnable deferredSubtitles;
    private float autosaveIndicatorValue;
    private float lastAutosaveIndicatorValue;
    private Pair<ContextualInfo, ContextualBarRenderer> contextualInfoBar = Pair.of((Object)((Object)ContextualInfo.EMPTY), (Object)ContextualBarRenderer.EMPTY);
    private final Map<ContextualInfo, Supplier<ContextualBarRenderer>> contextualInfoBarRenderers;
    private float scopeScale;

    public Gui(Mayaan minecraft) {
        this.minecraft = minecraft;
        this.debugOverlay = new DebugScreenOverlay(minecraft);
        this.spectatorGui = new SpectatorGui(minecraft);
        this.chat = new ChatComponent(minecraft);
        this.tabList = new PlayerTabOverlay(minecraft, this);
        this.bossOverlay = new BossHealthOverlay(minecraft);
        this.subtitleOverlay = new SubtitleOverlay(minecraft);
        this.contextualInfoBarRenderers = ImmutableMap.of((Object)((Object)ContextualInfo.EMPTY), () -> ContextualBarRenderer.EMPTY, (Object)((Object)ContextualInfo.EXPERIENCE), () -> new ExperienceBarRenderer(minecraft), (Object)((Object)ContextualInfo.LOCATOR), () -> new LocatorBarRenderer(minecraft), (Object)((Object)ContextualInfo.JUMPABLE_VEHICLE), () -> new JumpableVehicleBarRenderer(minecraft));
        this.resetTitleTimes();
    }

    public void resetTitleTimes() {
        this.titleFadeInTime = 10;
        this.titleStayTime = 70;
        this.titleFadeOutTime = 20;
    }

    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (this.minecraft.screen instanceof LevelLoadingScreen) {
            return;
        }
        if (!this.minecraft.options.hideGui) {
            this.renderCameraOverlays(graphics, deltaTracker);
            this.renderCrosshair(graphics, deltaTracker);
            graphics.nextStratum();
            this.renderHotbarAndDecorations(graphics, deltaTracker);
            this.renderEffects(graphics, deltaTracker);
            this.renderBossOverlay(graphics, deltaTracker);
        }
        this.renderSleepOverlay(graphics, deltaTracker);
        if (!this.minecraft.options.hideGui) {
            this.renderDemoOverlay(graphics, deltaTracker);
            this.renderScoreboardSidebar(graphics, deltaTracker);
            this.renderOverlayMessage(graphics, deltaTracker);
            this.renderTitle(graphics, deltaTracker);
            this.renderChat(graphics, deltaTracker);
            this.renderTabList(graphics, deltaTracker);
            this.renderSubtitleOverlay(graphics, this.minecraft.screen == null || this.minecraft.screen.isInGameUi());
        } else if (this.minecraft.screen != null && this.minecraft.screen.isInGameUi()) {
            this.renderSubtitleOverlay(graphics, true);
        }
    }

    private void renderBossOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
        this.bossOverlay.render(graphics);
    }

    public void renderDebugOverlay(GuiGraphics graphics) {
        this.debugOverlay.render(graphics);
    }

    private void renderSubtitleOverlay(GuiGraphics graphics, boolean deferRendering) {
        if (deferRendering) {
            this.deferredSubtitles = () -> this.subtitleOverlay.render(graphics);
        } else {
            this.deferredSubtitles = null;
            this.subtitleOverlay.render(graphics);
        }
    }

    public void renderDeferredSubtitles() {
        if (this.deferredSubtitles != null) {
            this.deferredSubtitles.run();
            this.deferredSubtitles = null;
        }
    }

    private void renderCameraOverlays(GuiGraphics graphics, DeltaTracker deltaTracker) {
        float screenEffectScale;
        if (this.minecraft.options.vignette().get().booleanValue()) {
            this.renderVignette(graphics, this.minecraft.getCameraEntity());
        }
        LocalPlayer player = this.minecraft.player;
        float gameTimeDeltaTicks = deltaTracker.getGameTimeDeltaTicks();
        this.scopeScale = Mth.lerp(0.5f * gameTimeDeltaTicks, this.scopeScale, 1.125f);
        if (this.minecraft.options.getCameraType().isFirstPerson()) {
            if (player.isScoping()) {
                this.renderSpyglassOverlay(graphics, this.scopeScale);
            } else {
                this.scopeScale = 0.5f;
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack item = player.getItemBySlot(slot);
                    Equippable equippable = item.get(DataComponents.EQUIPPABLE);
                    if (equippable == null || equippable.slot() != slot || !equippable.cameraOverlay().isPresent()) continue;
                    this.renderTextureOverlay(graphics, equippable.cameraOverlay().get().withPath(p -> "textures/" + p + ".png"), 1.0f);
                }
            }
        }
        if (player.getTicksFrozen() > 0) {
            this.renderTextureOverlay(graphics, POWDER_SNOW_OUTLINE_LOCATION, player.getPercentFrozen());
        }
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
        float portalIntensity = Mth.lerp(partialTicks, player.oPortalEffectIntensity, player.portalEffectIntensity);
        float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, partialTicks);
        if (portalIntensity > 0.0f) {
            this.renderPortalOverlay(graphics, portalIntensity);
        } else if (nauseaIntensity > 0.0f && (screenEffectScale = this.minecraft.options.screenEffectScale().get().floatValue()) < 1.0f) {
            float overlayStrength = nauseaIntensity * (1.0f - screenEffectScale);
            this.renderConfusionOverlay(graphics, overlayStrength);
        }
    }

    private void renderSleepOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (this.minecraft.player.getSleepTimer() <= 0) {
            return;
        }
        Profiler.get().push("sleep");
        graphics.nextStratum();
        float sleepTimer = this.minecraft.player.getSleepTimer();
        float amount = sleepTimer / 100.0f;
        if (amount > 1.0f) {
            amount = 1.0f - (sleepTimer - 100.0f) / 10.0f;
        }
        int color = (int)(220.0f * amount) << 24 | 0x101020;
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
        Profiler.get().pop();
    }

    private void renderOverlayMessage(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Font font = this.getFont();
        if (this.overlayMessageString == null || this.overlayMessageTime <= 0) {
            return;
        }
        Profiler.get().push("overlayMessage");
        float t = (float)this.overlayMessageTime - deltaTracker.getGameTimeDeltaPartialTick(false);
        int alpha = (int)(t * 255.0f / 20.0f);
        if (alpha > 255) {
            alpha = 255;
        }
        if (alpha > 0) {
            graphics.nextStratum();
            graphics.pose().pushMatrix();
            graphics.pose().translate((float)(graphics.guiWidth() / 2), (float)(graphics.guiHeight() - 68));
            int color = this.animateOverlayMessageColor ? Mth.hsvToArgb(t / 50.0f, 0.7f, 0.6f, alpha) : ARGB.white(alpha);
            int width = font.width(this.overlayMessageString);
            graphics.drawStringWithBackdrop(font, this.overlayMessageString, -width / 2, -4, width, color);
            graphics.pose().popMatrix();
        }
        Profiler.get().pop();
    }

    private void renderTitle(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (this.title == null || this.titleTime <= 0) {
            return;
        }
        Font font = this.getFont();
        Profiler.get().push("titleAndSubtitle");
        float t = (float)this.titleTime - deltaTracker.getGameTimeDeltaPartialTick(false);
        int alpha = 255;
        if (this.titleTime > this.titleFadeOutTime + this.titleStayTime) {
            float time = (float)(this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime) - t;
            alpha = (int)(time * 255.0f / (float)this.titleFadeInTime);
        }
        if (this.titleTime <= this.titleFadeOutTime) {
            alpha = (int)(t * 255.0f / (float)this.titleFadeOutTime);
        }
        if ((alpha = Mth.clamp(alpha, 0, 255)) > 0) {
            graphics.nextStratum();
            graphics.pose().pushMatrix();
            graphics.pose().translate((float)(graphics.guiWidth() / 2), (float)(graphics.guiHeight() / 2));
            graphics.pose().pushMatrix();
            graphics.pose().scale(4.0f, 4.0f);
            int titleWidth = font.width(this.title);
            int textColor = ARGB.white(alpha);
            graphics.drawStringWithBackdrop(font, this.title, -titleWidth / 2, -10, titleWidth, textColor);
            graphics.pose().popMatrix();
            if (this.subtitle != null) {
                graphics.pose().pushMatrix();
                graphics.pose().scale(2.0f, 2.0f);
                int subtitleWidth = font.width(this.subtitle);
                graphics.drawStringWithBackdrop(font, this.subtitle, -subtitleWidth / 2, 5, subtitleWidth, textColor);
                graphics.pose().popMatrix();
            }
            graphics.pose().popMatrix();
        }
        Profiler.get().pop();
    }

    private void renderChat(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (this.minecraft.player != null && !this.chat.isChatFocused()) {
            Window window = this.minecraft.getWindow();
            int mouseX = Mth.floor(this.minecraft.mouseHandler.getScaledXPos(window));
            int mouseY = Mth.floor(this.minecraft.mouseHandler.getScaledYPos(window));
            graphics.nextStratum();
            this.chat.render(graphics, this.getFont(), this.tickCount, mouseX, mouseY, ChatComponent.DisplayMode.BACKGROUND, false);
        }
    }

    private void renderScoreboardSidebar(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Objective displayObjective;
        DisplaySlot displaySlot;
        Scoreboard scoreboard = this.minecraft.level.getScoreboard();
        Objective teamObjective = null;
        PlayerTeam playerTeam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
        if (playerTeam != null && (displaySlot = DisplaySlot.teamColorToSlot(playerTeam.getColor())) != null) {
            teamObjective = scoreboard.getDisplayObjective(displaySlot);
        }
        Objective objective = displayObjective = teamObjective != null ? teamObjective : scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        if (displayObjective != null) {
            graphics.nextStratum();
            this.displayScoreboardSidebar(graphics, displayObjective);
        }
    }

    private void renderTabList(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Scoreboard scoreboard = this.minecraft.level.getScoreboard();
        Objective displayObjective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
        if (this.minecraft.options.keyPlayerList.isDown() && (!this.minecraft.isLocalServer() || this.minecraft.player.connection.getListedOnlinePlayers().size() > 1 || displayObjective != null)) {
            this.tabList.setVisible(true);
            graphics.nextStratum();
            this.tabList.render(graphics, graphics.guiWidth(), scoreboard, displayObjective);
        } else {
            this.tabList.setVisible(false);
        }
    }

    private void renderCrosshair(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Options options = this.minecraft.options;
        if (!options.getCameraType().isFirstPerson()) {
            return;
        }
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR && !this.canRenderCrosshairForSpectator(this.minecraft.hitResult)) {
            return;
        }
        if (!this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)) {
            graphics.nextStratum();
            int size = 15;
            graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_SPRITE, (graphics.guiWidth() - 15) / 2, (graphics.guiHeight() - 15) / 2, 15, 15);
            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
                float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0f);
                boolean renderMaxAttackIndicator = false;
                if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && attackStrengthScale >= 1.0f) {
                    renderMaxAttackIndicator = this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0f;
                    renderMaxAttackIndicator &= this.minecraft.crosshairPickEntity.isAlive();
                    AttackRange attackRange = this.minecraft.player.getActiveItem().get(DataComponents.ATTACK_RANGE);
                    renderMaxAttackIndicator &= attackRange == null || attackRange.isInRange(this.minecraft.player, this.minecraft.hitResult.getLocation());
                }
                int y = graphics.guiHeight() / 2 - 7 + 16;
                int x = graphics.guiWidth() / 2 - 8;
                if (renderMaxAttackIndicator) {
                    graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_FULL_SPRITE, x, y, 16, 16);
                } else if (attackStrengthScale < 1.0f) {
                    int progress = (int)(attackStrengthScale * 17.0f);
                    graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 16, 4);
                    graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE, 16, 4, 0, 0, x, y, progress, 4);
                }
            }
        }
    }

    private boolean canRenderCrosshairForSpectator(@Nullable HitResult hitResult) {
        if (hitResult == null) {
            return false;
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)hitResult).getEntity() instanceof MenuProvider;
        }
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            ClientLevel level = this.minecraft.level;
            BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
            return level.getBlockState(pos).getMenuProvider(level, pos) != null;
        }
        return false;
    }

    private void renderEffects(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Collection<MobEffectInstance> activeEffects = this.minecraft.player.getActiveEffects();
        if (activeEffects.isEmpty() || this.minecraft.screen != null && this.minecraft.screen.showsActiveEffects()) {
            return;
        }
        int beneficialCount = 0;
        int harmfulCount = 0;
        for (MobEffectInstance instance : Ordering.natural().reverse().sortedCopy(activeEffects)) {
            Holder<MobEffect> effect = instance.getEffect();
            if (!instance.showIcon()) continue;
            int x = graphics.guiWidth();
            int y = 1;
            if (this.minecraft.isDemo()) {
                y += 15;
            }
            if (effect.value().isBeneficial()) {
                x -= 25 * ++beneficialCount;
            } else {
                x -= 25 * ++harmfulCount;
                y += 26;
            }
            float alpha = 1.0f;
            if (instance.isAmbient()) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_AMBIENT_SPRITE, x, y, 24, 24);
            } else {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE, x, y, 24, 24);
                if (instance.endsWithin(200)) {
                    int remainingDuration = instance.getDuration();
                    int usedSeconds = 10 - remainingDuration / 20;
                    alpha = Mth.clamp((float)remainingDuration / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)remainingDuration * (float)Math.PI / 5.0f) * Mth.clamp((float)usedSeconds / 10.0f * 0.25f, 0.0f, 0.25f);
                    alpha = Mth.clamp(alpha, 0.0f, 1.0f);
                }
            }
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Gui.getMobEffectSprite(effect), x + 3, y + 3, 18, 18, ARGB.white(alpha));
        }
    }

    public static Identifier getMobEffectSprite(Holder<MobEffect> effect) {
        return effect.unwrapKey().map(ResourceKey::identifier).map(id -> id.withPrefix("mob_effect/")).orElseGet(MissingTextureAtlasSprite::getLocation);
    }

    private void renderHotbarAndDecorations(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            this.spectatorGui.renderHotbar(graphics);
        } else {
            this.renderItemHotbar(graphics, deltaTracker);
        }
        if (this.minecraft.gameMode.canHurtPlayer()) {
            this.renderPlayerHealth(graphics);
        }
        this.renderVehicleHealth(graphics);
        ContextualInfo nextContextualInfo = this.nextContextualInfoState();
        if (nextContextualInfo != this.contextualInfoBar.getKey()) {
            this.contextualInfoBar = Pair.of((Object)((Object)nextContextualInfo), (Object)this.contextualInfoBarRenderers.get((Object)nextContextualInfo).get());
        }
        ((ContextualBarRenderer)this.contextualInfoBar.getValue()).renderBackground(graphics, deltaTracker);
        if (this.minecraft.gameMode.hasExperience() && this.minecraft.player.experienceLevel > 0) {
            ContextualBarRenderer.renderExperienceLevel(graphics, this.minecraft.font, this.minecraft.player.experienceLevel);
        }
        ((ContextualBarRenderer)this.contextualInfoBar.getValue()).render(graphics, deltaTracker);
        if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            this.renderSelectedItemName(graphics);
        } else if (this.minecraft.player.isSpectator()) {
            this.spectatorGui.renderAction(graphics);
        }
    }

    private void renderItemHotbar(GuiGraphics graphics, DeltaTracker deltaTracker) {
        float attackStrengthScale;
        Player player = this.getCameraPlayer();
        if (player == null) {
            return;
        }
        ItemStack offhand = player.getOffhandItem();
        HumanoidArm offhandArm = player.getMainArm().getOpposite();
        int screenCenter = graphics.guiWidth() / 2;
        int hotbarWidth = 182;
        int halfHotbar = 91;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, screenCenter - 91, graphics.guiHeight() - 22, 182, 22);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, screenCenter - 91 - 1 + player.getInventory().getSelectedSlot() * 20, graphics.guiHeight() - 22 - 1, 24, 23);
        if (!offhand.isEmpty()) {
            if (offhandArm == HumanoidArm.LEFT) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, graphics.guiHeight() - 23, 29, 24);
            } else {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenCenter + 91, graphics.guiHeight() - 23, 29, 24);
            }
        }
        int seed = 1;
        for (int i = 0; i < 9; ++i) {
            int x = screenCenter - 90 + i * 20 + 2;
            int y = graphics.guiHeight() - 16 - 3;
            this.renderSlot(graphics, x, y, deltaTracker, player, player.getInventory().getItem(i), seed++);
        }
        if (!offhand.isEmpty()) {
            int y = graphics.guiHeight() - 16 - 3;
            if (offhandArm == HumanoidArm.LEFT) {
                this.renderSlot(graphics, screenCenter - 91 - 26, y, deltaTracker, player, offhand, seed++);
            } else {
                this.renderSlot(graphics, screenCenter + 91 + 10, y, deltaTracker, player, offhand, seed++);
            }
        }
        if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR && (attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0f)) < 1.0f) {
            int y = graphics.guiHeight() - 20;
            int x = screenCenter + 91 + 6;
            if (offhandArm == HumanoidArm.RIGHT) {
                x = screenCenter - 91 - 22;
            }
            int progress = (int)(attackStrengthScale * 19.0f);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 18, 18);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - progress, x, y + 18 - progress, 18, progress);
        }
    }

    private void renderSelectedItemName(GuiGraphics graphics) {
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            int alpha;
            MutableComponent str = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color());
            if (this.lastToolHighlight.has(DataComponents.CUSTOM_NAME)) {
                str.withStyle(ChatFormatting.ITALIC);
            }
            int strWidth = this.getFont().width(str);
            int x = (graphics.guiWidth() - strWidth) / 2;
            int y = graphics.guiHeight() - 59;
            if (!this.minecraft.gameMode.canHurtPlayer()) {
                y += 14;
            }
            if ((alpha = (int)((float)this.toolHighlightTimer * 256.0f / 10.0f)) > 255) {
                alpha = 255;
            }
            if (alpha > 0) {
                graphics.drawStringWithBackdrop(this.getFont(), str, x, y, strWidth, ARGB.white(alpha));
            }
        }
    }

    private void renderDemoOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!this.minecraft.isDemo()) {
            return;
        }
        Profiler.get().push("demo");
        graphics.nextStratum();
        Component msg = this.minecraft.level.getGameTime() >= 120500L ? DEMO_EXPIRED_TEXT : Component.translatable("demo.remainingTime", StringUtil.formatTickDuration((int)(120500L - this.minecraft.level.getGameTime()), this.minecraft.level.tickRateManager().tickrate()));
        int width = this.getFont().width(msg);
        int textX = graphics.guiWidth() - width - 10;
        int textY = 5;
        graphics.drawStringWithBackdrop(this.getFont(), msg, textX, 5, width, -1);
        Profiler.get().pop();
    }

    private void displayScoreboardSidebar(GuiGraphics graphics, Objective objective) {
        int objectiveDisplayNameWidth;
        Scoreboard scoreboard = objective.getScoreboard();
        NumberFormat objectiveScoreFormat = objective.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT);
        record DisplayEntry(Component name, Component score, int scoreWidth) {
        }
        DisplayEntry[] entriesToDisplay = (DisplayEntry[])scoreboard.listPlayerScores(objective).stream().filter(input -> !input.isHidden()).sorted(SCORE_DISPLAY_ORDER).limit(15L).map(score -> {
            PlayerTeam team = scoreboard.getPlayersTeam(score.owner());
            Component ownerName = score.ownerName();
            MutableComponent name = PlayerTeam.formatNameForTeam(team, ownerName);
            MutableComponent scoreString = score.formatValue(objectiveScoreFormat);
            int scoreWidth = this.getFont().width(scoreString);
            return new DisplayEntry(name, scoreString, scoreWidth);
        }).toArray(x$0 -> new DisplayEntry[x$0]);
        Component objectiveDisplayName = objective.getDisplayName();
        int biggestWidth = objectiveDisplayNameWidth = this.getFont().width(objectiveDisplayName);
        int spacerWidth = this.getFont().width(SPACER);
        for (DisplayEntry entry : entriesToDisplay) {
            biggestWidth = Math.max(biggestWidth, this.getFont().width(entry.name) + (entry.scoreWidth > 0 ? spacerWidth + entry.scoreWidth : 0));
        }
        int width = biggestWidth;
        int entriesCount = entriesToDisplay.length;
        int height = entriesCount * this.getFont().lineHeight;
        int bottom = graphics.guiHeight() / 2 + height / 3;
        int rightPadding = 3;
        int left = graphics.guiWidth() - width - 3;
        int right = graphics.guiWidth() - 3 + 2;
        int backgroundColor = this.minecraft.options.getBackgroundColor(0.3f);
        int headerBackgroundColor = this.minecraft.options.getBackgroundColor(0.4f);
        int headerY = bottom - entriesCount * this.getFont().lineHeight;
        graphics.fill(left - 2, headerY - this.getFont().lineHeight - 1, right, headerY - 1, headerBackgroundColor);
        graphics.fill(left - 2, headerY - 1, right, bottom, backgroundColor);
        graphics.drawString(this.getFont(), objectiveDisplayName, left + width / 2 - objectiveDisplayNameWidth / 2, headerY - this.getFont().lineHeight, -1, false);
        for (int i = 0; i < entriesCount; ++i) {
            DisplayEntry e = entriesToDisplay[i];
            int y = bottom - (entriesCount - i) * this.getFont().lineHeight;
            graphics.drawString(this.getFont(), e.name, left, y, -1, false);
            graphics.drawString(this.getFont(), e.score, right - e.scoreWidth, y, -1, false);
        }
    }

    private @Nullable Player getCameraPlayer() {
        Player player;
        Entity entity = this.minecraft.getCameraEntity();
        return entity instanceof Player ? (player = (Player)entity) : null;
    }

    private @Nullable LivingEntity getPlayerVehicleWithHealth() {
        Player player = this.getCameraPlayer();
        if (player != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle == null) {
                return null;
            }
            if (vehicle instanceof LivingEntity) {
                return (LivingEntity)vehicle;
            }
        }
        return null;
    }

    private int getVehicleMaxHearts(@Nullable LivingEntity vehicle) {
        if (vehicle == null || !vehicle.showVehicleHealth()) {
            return 0;
        }
        float maxVehicleHealth = vehicle.getMaxHealth();
        int hearts = (int)(maxVehicleHealth + 0.5f) / 2;
        if (hearts > 30) {
            hearts = 30;
        }
        return hearts;
    }

    private int getVisibleVehicleHeartRows(int hearts) {
        return (int)Math.ceil((double)hearts / 10.0);
    }

    private void renderPlayerHealth(GuiGraphics graphics) {
        Player player = this.getCameraPlayer();
        if (player == null) {
            return;
        }
        int currentHealth = Mth.ceil(player.getHealth());
        boolean blink = this.healthBlinkTime > (long)this.tickCount && (this.healthBlinkTime - (long)this.tickCount) / 3L % 2L == 1L;
        long timeMillis = Util.getMillis();
        if (currentHealth < this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = timeMillis;
            this.healthBlinkTime = this.tickCount + 20;
        } else if (currentHealth > this.lastHealth && player.invulnerableTime > 0) {
            this.lastHealthTime = timeMillis;
            this.healthBlinkTime = this.tickCount + 10;
        }
        if (timeMillis - this.lastHealthTime > 1000L) {
            this.displayHealth = currentHealth;
            this.lastHealthTime = timeMillis;
        }
        this.lastHealth = currentHealth;
        int oldHealth = this.displayHealth;
        this.random.setSeed(this.tickCount * 312871);
        int xLeft = graphics.guiWidth() / 2 - 91;
        int xRight = graphics.guiWidth() / 2 + 91;
        int yLineBase = graphics.guiHeight() - 39;
        float maxHealth = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(oldHealth, currentHealth));
        int totalAbsorption = Mth.ceil(player.getAbsorptionAmount());
        int numHealthRows = Mth.ceil((maxHealth + (float)totalAbsorption) / 2.0f / 10.0f);
        int healthRowHeight = Math.max(10 - (numHealthRows - 2), 3);
        int yLineAir = yLineBase - 10;
        int heartOffsetIndex = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            heartOffsetIndex = this.tickCount % Mth.ceil(maxHealth + 5.0f);
        }
        Profiler.get().push("armor");
        Gui.renderArmor(graphics, player, yLineBase, numHealthRows, healthRowHeight, xLeft);
        Profiler.get().popPush("health");
        this.renderHearts(graphics, player, xLeft, yLineBase, healthRowHeight, heartOffsetIndex, maxHealth, currentHealth, oldHealth, totalAbsorption, blink);
        LivingEntity vehicleWithHearts = this.getPlayerVehicleWithHealth();
        int vehicleHearts = this.getVehicleMaxHearts(vehicleWithHearts);
        if (vehicleHearts == 0) {
            Profiler.get().popPush("food");
            this.renderFood(graphics, player, yLineBase, xRight);
            yLineAir -= 10;
        }
        Profiler.get().popPush("air");
        this.renderAirBubbles(graphics, player, vehicleHearts, yLineAir, xRight);
        Profiler.get().pop();
    }

    private static void renderArmor(GuiGraphics graphics, Player player, int yLineBase, int numHealthRows, int healthRowHeight, int xLeft) {
        int armor = player.getArmorValue();
        if (armor <= 0) {
            return;
        }
        int yLineArmor = yLineBase - (numHealthRows - 1) * healthRowHeight - 10;
        for (int i = 0; i < 10; ++i) {
            int xo = xLeft + i * 8;
            if (i * 2 + 1 < armor) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_SPRITE, xo, yLineArmor, 9, 9);
            }
            if (i * 2 + 1 == armor) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF_SPRITE, xo, yLineArmor, 9, 9);
            }
            if (i * 2 + 1 <= armor) continue;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_SPRITE, xo, yLineArmor, 9, 9);
        }
    }

    private void renderHearts(GuiGraphics graphics, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink) {
        HeartType type = HeartType.forPlayer(player);
        boolean isHardcore = player.level().getLevelData().isHardcore();
        int healthContainerCount = Mth.ceil((double)maxHealth / 2.0);
        int absorptionContainerCount = Mth.ceil((double)absorption / 2.0);
        int maxHealthHalvesCount = healthContainerCount * 2;
        for (int containerIndex = healthContainerCount + absorptionContainerCount - 1; containerIndex >= 0; --containerIndex) {
            boolean halfHeart;
            int absorptionHalves;
            boolean isAbsorptionHeart;
            int row = containerIndex / 10;
            int column = containerIndex % 10;
            int xo = xLeft + column * 8;
            int yo = yLineBase - row * healthRowHeight;
            if (currentHealth + absorption <= 4) {
                yo += this.random.nextInt(2);
            }
            if (containerIndex < healthContainerCount && containerIndex == heartOffsetIndex) {
                yo -= 2;
            }
            this.renderHeart(graphics, HeartType.CONTAINER, xo, yo, isHardcore, blink, false);
            int halves = containerIndex * 2;
            boolean bl = isAbsorptionHeart = containerIndex >= healthContainerCount;
            if (isAbsorptionHeart && (absorptionHalves = halves - maxHealthHalvesCount) < absorption) {
                boolean halfHeart2 = absorptionHalves + 1 == absorption;
                this.renderHeart(graphics, type == HeartType.WITHERED ? type : HeartType.ABSORBING, xo, yo, isHardcore, false, halfHeart2);
            }
            if (blink && halves < oldHealth) {
                halfHeart = halves + 1 == oldHealth;
                this.renderHeart(graphics, type, xo, yo, isHardcore, true, halfHeart);
            }
            if (halves >= currentHealth) continue;
            halfHeart = halves + 1 == currentHealth;
            this.renderHeart(graphics, type, xo, yo, isHardcore, false, halfHeart);
        }
    }

    private void renderHeart(GuiGraphics graphics, HeartType type, int xo, int yo, boolean isHardcore, boolean blinks, boolean half) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, type.getSprite(isHardcore, half, blinks), xo, yo, 9, 9);
    }

    private void renderAirBubbles(GuiGraphics graphics, Player player, int vehicleHearts, int yLineAir, int xRight) {
        int maxAirSupplyTicks = player.getMaxAirSupply();
        int currentAirSupplyTicks = Math.clamp((long)player.getAirSupply(), (int)0, (int)maxAirSupplyTicks);
        boolean isUnderWater = player.isEyeInFluid(FluidTags.WATER);
        if (isUnderWater || currentAirSupplyTicks < maxAirSupplyTicks) {
            boolean isPoppingBubble;
            yLineAir = this.getAirBubbleYLine(vehicleHearts, yLineAir);
            int fullAirBubbles = Gui.getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, -2);
            int poppingAirBubblePosition = Gui.getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, 0);
            int emptyAirBubbles = 10 - Gui.getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, Gui.getEmptyBubbleDelayDuration(currentAirSupplyTicks, isUnderWater));
            boolean bl = isPoppingBubble = fullAirBubbles != poppingAirBubblePosition;
            if (!isUnderWater) {
                this.lastBubblePopSoundPlayed = 0;
            }
            for (int airBubble = 1; airBubble <= 10; ++airBubble) {
                int airBubbleXPos = xRight - (airBubble - 1) * 8 - 9;
                if (airBubble <= fullAirBubbles) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_SPRITE, airBubbleXPos, yLineAir, 9, 9);
                    continue;
                }
                if (isPoppingBubble && airBubble == poppingAirBubblePosition && isUnderWater) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_POPPING_SPRITE, airBubbleXPos, yLineAir, 9, 9);
                    this.playAirBubblePoppedSound(airBubble, player, emptyAirBubbles);
                    continue;
                }
                if (airBubble <= 10 - emptyAirBubbles) continue;
                int wobbleYOffset = emptyAirBubbles == 10 && this.tickCount % 2 == 0 ? this.random.nextInt(2) : 0;
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_EMPTY_SPRITE, airBubbleXPos, yLineAir + wobbleYOffset, 9, 9);
            }
        }
    }

    private int getAirBubbleYLine(int vehicleHearts, int yLineAir) {
        int rowOffset = this.getVisibleVehicleHeartRows(vehicleHearts) - 1;
        return yLineAir -= rowOffset * 10;
    }

    private static int getCurrentAirSupplyBubble(int currentAirSupplyTicks, int maxAirSupplyTicks, int tickOffset) {
        return Mth.ceil((float)((currentAirSupplyTicks + tickOffset) * 10) / (float)maxAirSupplyTicks);
    }

    private static int getEmptyBubbleDelayDuration(int currentAirSupplyTicks, boolean isUnderWater) {
        return currentAirSupplyTicks == 0 || !isUnderWater ? 0 : 1;
    }

    private void playAirBubblePoppedSound(int bubble, Player player, int emptyAirBubbles) {
        if (this.lastBubblePopSoundPlayed != bubble) {
            float soundVolume = 0.5f + 0.1f * (float)Math.max(0, emptyAirBubbles - 3 + 1);
            float soundPitch = 1.0f + 0.1f * (float)Math.max(0, emptyAirBubbles - 5 + 1);
            player.playSound(SoundEvents.BUBBLE_POP, soundVolume, soundPitch);
            this.lastBubblePopSoundPlayed = bubble;
        }
    }

    private void renderFood(GuiGraphics graphics, Player player, int yLineBase, int xRight) {
        FoodData foodData = player.getFoodData();
        int food = foodData.getFoodLevel();
        for (int i = 0; i < 10; ++i) {
            Identifier full;
            Identifier half;
            Identifier empty;
            int yo = yLineBase;
            if (player.hasEffect(MobEffects.HUNGER)) {
                empty = FOOD_EMPTY_HUNGER_SPRITE;
                half = FOOD_HALF_HUNGER_SPRITE;
                full = FOOD_FULL_HUNGER_SPRITE;
            } else {
                empty = FOOD_EMPTY_SPRITE;
                half = FOOD_HALF_SPRITE;
                full = FOOD_FULL_SPRITE;
            }
            if (player.getFoodData().getSaturationLevel() <= 0.0f && this.tickCount % (food * 3 + 1) == 0) {
                yo += this.random.nextInt(3) - 1;
            }
            int xo = xRight - i * 8 - 9;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, xo, yo, 9, 9);
            if (i * 2 + 1 < food) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, xo, yo, 9, 9);
            }
            if (i * 2 + 1 != food) continue;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, half, xo, yo, 9, 9);
        }
    }

    private void renderVehicleHealth(GuiGraphics graphics) {
        LivingEntity vehicleWithHearts = this.getPlayerVehicleWithHealth();
        if (vehicleWithHearts == null) {
            return;
        }
        int hearts = this.getVehicleMaxHearts(vehicleWithHearts);
        if (hearts == 0) {
            return;
        }
        int currentHealth = (int)Math.ceil(vehicleWithHearts.getHealth());
        Profiler.get().popPush("mountHealth");
        int yLine1 = graphics.guiHeight() - 39;
        int xRight = graphics.guiWidth() / 2 + 91;
        int yo = yLine1;
        int baseHealth = 0;
        while (hearts > 0) {
            int rowHearts = Math.min(hearts, 10);
            hearts -= rowHearts;
            for (int i = 0; i < rowHearts; ++i) {
                int xo = xRight - i * 8 - 9;
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_CONTAINER_SPRITE, xo, yo, 9, 9);
                if (i * 2 + 1 + baseHealth < currentHealth) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_FULL_SPRITE, xo, yo, 9, 9);
                }
                if (i * 2 + 1 + baseHealth != currentHealth) continue;
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_HALF_SPRITE, xo, yo, 9, 9);
            }
            yo -= 10;
            baseHealth += 20;
        }
    }

    private void renderTextureOverlay(GuiGraphics graphics, Identifier texture, float alpha) {
        int color = ARGB.white(alpha);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0.0f, 0.0f, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), color);
    }

    private void renderSpyglassOverlay(GuiGraphics graphics, float scale) {
        float srcWidth;
        float srcHeight = srcWidth = (float)Math.min(graphics.guiWidth(), graphics.guiHeight());
        float ratio = Math.min((float)graphics.guiWidth() / srcWidth, (float)graphics.guiHeight() / srcHeight) * scale;
        int width = Mth.floor(srcWidth * ratio);
        int height = Mth.floor(srcHeight * ratio);
        int left = (graphics.guiWidth() - width) / 2;
        int top = (graphics.guiHeight() - height) / 2;
        int right = left + width;
        int bottom = top + height;
        graphics.blit(RenderPipelines.GUI_TEXTURED, SPYGLASS_SCOPE_LOCATION, left, top, 0.0f, 0.0f, width, height, width, height);
        graphics.fill(RenderPipelines.GUI, 0, bottom, graphics.guiWidth(), graphics.guiHeight(), -16777216);
        graphics.fill(RenderPipelines.GUI, 0, 0, graphics.guiWidth(), top, -16777216);
        graphics.fill(RenderPipelines.GUI, 0, top, left, bottom, -16777216);
        graphics.fill(RenderPipelines.GUI, right, top, graphics.guiWidth(), bottom, -16777216);
    }

    private void updateVignetteBrightness(Entity camera) {
        BlockPos blockPos = BlockPos.containing(camera.getX(), camera.getEyeY(), camera.getZ());
        float levelBrightness = Lightmap.getBrightness(camera.level().dimensionType(), camera.level().getMaxLocalRawBrightness(blockPos));
        float brightness = Mth.clamp(1.0f - levelBrightness, 0.0f, 1.0f);
        this.vignetteBrightness += (brightness - this.vignetteBrightness) * 0.01f;
    }

    private void renderVignette(GuiGraphics graphics, @Nullable Entity camera) {
        int color;
        WorldBorder worldBorder = this.minecraft.level.getWorldBorder();
        float borderWarningStrength = 0.0f;
        if (camera != null) {
            float distToBorder = (float)worldBorder.getDistanceToBorder(camera);
            double movingBlocksThreshold = Math.min(worldBorder.getLerpSpeed() * (double)worldBorder.getWarningTime(), Math.abs(worldBorder.getLerpTarget() - worldBorder.getSize()));
            double warningDistance = Math.max((double)worldBorder.getWarningBlocks(), movingBlocksThreshold);
            if ((double)distToBorder < warningDistance) {
                borderWarningStrength = 1.0f - (float)((double)distToBorder / warningDistance);
            }
        }
        float brightness = Mth.clamp(this.vignetteBrightness, 0.0f, 1.0f);
        if (borderWarningStrength > 0.0f) {
            borderWarningStrength = Mth.clamp(borderWarningStrength, 0.0f, 1.0f);
            float red = brightness * (1.0f - borderWarningStrength);
            float greenBlue = brightness + (1.0f - brightness) * borderWarningStrength;
            color = ARGB.colorFromFloat(1.0f, red, greenBlue, greenBlue);
        } else {
            color = ARGB.colorFromFloat(1.0f, brightness, brightness, brightness);
        }
        graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, 0.0f, 0.0f, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), color);
    }

    private void renderPortalOverlay(GuiGraphics graphics, float alpha) {
        if (alpha < 1.0f) {
            alpha *= alpha;
            alpha *= alpha;
            alpha = alpha * 0.8f + 0.2f;
        }
        int color = ARGB.white(alpha);
        TextureAtlasSprite slot = this.minecraft.getModelManager().getBlockStateModelSet().getParticleMaterial(Blocks.NETHER_PORTAL.defaultBlockState()).sprite();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slot, 0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
    }

    private void renderConfusionOverlay(GuiGraphics graphics, float strength) {
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();
        graphics.pose().pushMatrix();
        float size = Mth.lerp(strength, 2.0f, 1.0f);
        graphics.pose().translate((float)screenWidth / 2.0f, (float)screenHeight / 2.0f);
        graphics.pose().scale(size, size);
        graphics.pose().translate((float)(-screenWidth) / 2.0f, (float)(-screenHeight) / 2.0f);
        float red = 0.2f * strength;
        float green = 0.4f * strength;
        float blue = 0.2f * strength;
        graphics.blit(RenderPipelines.GUI_NAUSEA_OVERLAY, NAUSEA_LOCATION, 0, 0, 0.0f, 0.0f, screenWidth, screenHeight, screenWidth, screenHeight, ARGB.colorFromFloat(1.0f, red, green, blue));
        graphics.pose().popMatrix();
    }

    private void renderSlot(GuiGraphics graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed) {
        if (itemStack.isEmpty()) {
            return;
        }
        float pop = (float)itemStack.getPopTime() - deltaTracker.getGameTimeDeltaPartialTick(false);
        if (pop > 0.0f) {
            float squeeze = 1.0f + pop / 5.0f;
            graphics.pose().pushMatrix();
            graphics.pose().translate((float)(x + 8), (float)(y + 12));
            graphics.pose().scale(1.0f / squeeze, (squeeze + 1.0f) / 2.0f);
            graphics.pose().translate((float)(-(x + 8)), (float)(-(y + 12)));
        }
        graphics.renderItem(player, itemStack, x, y, seed);
        if (pop > 0.0f) {
            graphics.pose().popMatrix();
        }
        graphics.renderItemDecorations(this.minecraft.font, itemStack, x, y);
    }

    public void tick(boolean pause) {
        this.tickAutosaveIndicator();
        if (!pause) {
            this.tick();
        }
    }

    private void tick() {
        if (this.overlayMessageTime > 0) {
            --this.overlayMessageTime;
        }
        if (this.titleTime > 0) {
            --this.titleTime;
            if (this.titleTime <= 0) {
                this.title = null;
                this.subtitle = null;
            }
        }
        ++this.tickCount;
        Entity camera = this.minecraft.getCameraEntity();
        if (camera != null) {
            this.updateVignetteBrightness(camera);
        }
        if (this.minecraft.player != null) {
            ItemStack selected = this.minecraft.player.getInventory().getSelectedItem();
            if (selected.isEmpty()) {
                this.toolHighlightTimer = 0;
            } else if (this.lastToolHighlight.isEmpty() || !selected.is(this.lastToolHighlight.getItem()) || !selected.getHoverName().equals(this.lastToolHighlight.getHoverName())) {
                this.toolHighlightTimer = (int)(40.0 * this.minecraft.options.notificationDisplayTime().get());
            } else if (this.toolHighlightTimer > 0) {
                --this.toolHighlightTimer;
            }
            this.lastToolHighlight = selected;
        }
        this.chat.tick();
    }

    private void tickAutosaveIndicator() {
        IntegratedServer server = this.minecraft.getSingleplayerServer();
        boolean isAutosaving = server != null && server.isCurrentlySaving();
        this.lastAutosaveIndicatorValue = this.autosaveIndicatorValue;
        this.autosaveIndicatorValue = Mth.lerp(0.2f, this.autosaveIndicatorValue, isAutosaving ? 1.0f : 0.0f);
    }

    public void setNowPlaying(Component string) {
        MutableComponent message = Component.translatable("record.nowPlaying", string);
        this.setOverlayMessage(message, true);
        this.minecraft.getNarrator().saySystemNow(message);
    }

    public void setOverlayMessage(Component string, boolean animate) {
        this.overlayMessageString = string;
        this.overlayMessageTime = 60;
        this.animateOverlayMessageColor = animate;
    }

    public void setTimes(int fadeInTime, int stayTime, int fadeOutTime) {
        if (fadeInTime >= 0) {
            this.titleFadeInTime = fadeInTime;
        }
        if (stayTime >= 0) {
            this.titleStayTime = stayTime;
        }
        if (fadeOutTime >= 0) {
            this.titleFadeOutTime = fadeOutTime;
        }
        if (this.titleTime > 0) {
            this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
        }
    }

    public void setSubtitle(Component subtitle) {
        this.subtitle = subtitle;
    }

    public void setTitle(Component title) {
        this.title = title;
        this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
    }

    public void clearTitles() {
        this.title = null;
        this.subtitle = null;
        this.titleTime = 0;
    }

    public ChatComponent getChat() {
        return this.chat;
    }

    public int getGuiTicks() {
        return this.tickCount;
    }

    public Font getFont() {
        return this.minecraft.font;
    }

    public SpectatorGui getSpectatorGui() {
        return this.spectatorGui;
    }

    public PlayerTabOverlay getTabList() {
        return this.tabList;
    }

    public void onDisconnected() {
        this.tabList.reset();
        this.bossOverlay.reset();
        this.minecraft.getToastManager().clear();
        this.debugOverlay.reset();
        this.chat.clearMessages(true);
        this.clearTitles();
        this.resetTitleTimes();
    }

    public BossHealthOverlay getBossOverlay() {
        return this.bossOverlay;
    }

    public DebugScreenOverlay getDebugOverlay() {
        return this.debugOverlay;
    }

    public void clearCache() {
        this.debugOverlay.clearChunkCache();
    }

    public void renderSavingIndicator(GuiGraphics graphics, DeltaTracker deltaTracker) {
        int alpha;
        if (this.minecraft.options.showAutosaveIndicator().get().booleanValue() && (this.autosaveIndicatorValue > 0.0f || this.lastAutosaveIndicatorValue > 0.0f) && (alpha = Mth.floor(255.0f * Mth.clamp(Mth.lerp(deltaTracker.getRealtimeDeltaTicks(), this.lastAutosaveIndicatorValue, this.autosaveIndicatorValue), 0.0f, 1.0f))) > 0) {
            Font font = this.getFont();
            int width = font.width(SAVING_TEXT);
            int color = ARGB.color(alpha, -1);
            int textX = graphics.guiWidth() - width - 5;
            int textY = graphics.guiHeight() - font.lineHeight - 5;
            graphics.nextStratum();
            graphics.drawStringWithBackdrop(font, SAVING_TEXT, textX, textY, width, color);
        }
    }

    private boolean willPrioritizeExperienceInfo() {
        return this.minecraft.player.experienceDisplayStartTick + 100 > this.minecraft.player.tickCount;
    }

    private boolean willPrioritizeJumpInfo() {
        return this.minecraft.player.getJumpRidingScale() > 0.0f || Optionull.mapOrDefault(this.minecraft.player.jumpableVehicle(), PlayerRideableJumping::getJumpCooldown, 0) > 0;
    }

    private ContextualInfo nextContextualInfoState() {
        boolean canShowLocatorInfo = this.minecraft.player.connection.getWaypointManager().hasWaypoints();
        boolean canShowVehicleJumpInfo = this.minecraft.player.jumpableVehicle() != null;
        boolean canShowExperienceInfo = this.minecraft.gameMode.hasExperience();
        if (canShowLocatorInfo) {
            if (canShowVehicleJumpInfo && this.willPrioritizeJumpInfo()) {
                return ContextualInfo.JUMPABLE_VEHICLE;
            }
            if (canShowExperienceInfo && this.willPrioritizeExperienceInfo()) {
                return ContextualInfo.EXPERIENCE;
            }
            return ContextualInfo.LOCATOR;
        }
        if (canShowVehicleJumpInfo) {
            return ContextualInfo.JUMPABLE_VEHICLE;
        }
        if (canShowExperienceInfo) {
            return ContextualInfo.EXPERIENCE;
        }
        return ContextualInfo.EMPTY;
    }

    static enum ContextualInfo {
        EMPTY,
        EXPERIENCE,
        LOCATOR,
        JUMPABLE_VEHICLE;

    }

    private static enum HeartType {
        CONTAINER(Identifier.withDefaultNamespace("hud/heart/container"), Identifier.withDefaultNamespace("hud/heart/container_blinking"), Identifier.withDefaultNamespace("hud/heart/container"), Identifier.withDefaultNamespace("hud/heart/container_blinking"), Identifier.withDefaultNamespace("hud/heart/container_hardcore"), Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking"), Identifier.withDefaultNamespace("hud/heart/container_hardcore"), Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking")),
        NORMAL(Identifier.withDefaultNamespace("hud/heart/full"), Identifier.withDefaultNamespace("hud/heart/full_blinking"), Identifier.withDefaultNamespace("hud/heart/half"), Identifier.withDefaultNamespace("hud/heart/half_blinking"), Identifier.withDefaultNamespace("hud/heart/hardcore_full"), Identifier.withDefaultNamespace("hud/heart/hardcore_full_blinking"), Identifier.withDefaultNamespace("hud/heart/hardcore_half"), Identifier.withDefaultNamespace("hud/heart/hardcore_half_blinking")),
        POISIONED(Identifier.withDefaultNamespace("hud/heart/poisoned_full"), Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking"), Identifier.withDefaultNamespace("hud/heart/poisoned_half"), Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking"), Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full"), Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking"), Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half"), Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking")),
        WITHERED(Identifier.withDefaultNamespace("hud/heart/withered_full"), Identifier.withDefaultNamespace("hud/heart/withered_full_blinking"), Identifier.withDefaultNamespace("hud/heart/withered_half"), Identifier.withDefaultNamespace("hud/heart/withered_half_blinking"), Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full"), Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking"), Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half"), Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking")),
        ABSORBING(Identifier.withDefaultNamespace("hud/heart/absorbing_full"), Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking"), Identifier.withDefaultNamespace("hud/heart/absorbing_half"), Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking"), Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full"), Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full_blinking"), Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half"), Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half_blinking")),
        FROZEN(Identifier.withDefaultNamespace("hud/heart/frozen_full"), Identifier.withDefaultNamespace("hud/heart/frozen_full_blinking"), Identifier.withDefaultNamespace("hud/heart/frozen_half"), Identifier.withDefaultNamespace("hud/heart/frozen_half_blinking"), Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full"), Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"), Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half"), Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking"));

        private final Identifier full;
        private final Identifier fullBlinking;
        private final Identifier half;
        private final Identifier halfBlinking;
        private final Identifier hardcoreFull;
        private final Identifier hardcoreFullBlinking;
        private final Identifier hardcoreHalf;
        private final Identifier hardcoreHalfBlinking;

        private HeartType(Identifier full, Identifier fullBlinking, Identifier half, Identifier halfBlinking, Identifier hardcoreFull, Identifier hardcoreFullBlinking, Identifier hardcoreHalf, Identifier hardcoreHalfBlinking) {
            this.full = full;
            this.fullBlinking = fullBlinking;
            this.half = half;
            this.halfBlinking = halfBlinking;
            this.hardcoreFull = hardcoreFull;
            this.hardcoreFullBlinking = hardcoreFullBlinking;
            this.hardcoreHalf = hardcoreHalf;
            this.hardcoreHalfBlinking = hardcoreHalfBlinking;
        }

        public Identifier getSprite(boolean isHardcore, boolean isHalf, boolean isBlink) {
            if (!isHardcore) {
                if (isHalf) {
                    return isBlink ? this.halfBlinking : this.half;
                }
                return isBlink ? this.fullBlinking : this.full;
            }
            if (isHalf) {
                return isBlink ? this.hardcoreHalfBlinking : this.hardcoreHalf;
            }
            return isBlink ? this.hardcoreFullBlinking : this.hardcoreFull;
        }

        private static HeartType forPlayer(Player player) {
            HeartType type = player.hasEffect(MobEffects.POISON) ? POISIONED : (player.hasEffect(MobEffects.WITHER) ? WITHERED : (player.isFullyFrozen() ? FROZEN : NORMAL));
            return type;
        }
    }

    public static interface RenderFunction {
        public void render(GuiGraphics var1, DeltaTracker var2);
    }
}

