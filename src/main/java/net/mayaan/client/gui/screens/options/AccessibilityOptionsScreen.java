/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import java.util.Arrays;
import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.Tooltip;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.AccessibilityOnboardingScreen;
import net.mayaan.client.gui.screens.ConfirmLinkScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.OptionsScreen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.client.gui.screens.options.controls.ControlsScreen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.CommonLinks;
import net.mayaan.world.flag.FeatureFlags;

public class AccessibilityOptionsScreen
extends OptionsSubScreen {
    public static final Component TITLE = Component.translatable("options.accessibility.title");

    private static OptionInstance<?>[] options(Options options) {
        return new OptionInstance[]{options.narrator(), options.showSubtitles(), options.highContrast(), options.menuBackgroundBlurriness(), options.textBackgroundOpacity(), options.backgroundForChatOnly(), options.chatOpacity(), options.chatLineSpacing(), options.chatDelay(), options.notificationDisplayTime(), options.bobView(), options.screenEffectScale(), options.fovEffectScale(), options.darknessEffectScale(), options.damageTiltStrength(), options.glintSpeed(), options.glintStrength(), options.hideLightningFlash(), options.darkMojangStudiosBackground(), options.panoramaSpeed(), options.hideSplashTexts(), options.narratorHotkey(), options.rotateWithMinecart(), options.highContrastBlockOutline()};
    }

    public AccessibilityOptionsScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
    }

    @Override
    protected void init() {
        AbstractWidget rotateWithMinecart;
        super.init();
        AbstractWidget highContrast = this.list.findOption(this.options.highContrast());
        if (highContrast != null && !this.minecraft.getResourcePackRepository().getAvailableIds().contains("high_contrast")) {
            highContrast.active = false;
            highContrast.setTooltip(Tooltip.create(Component.translatable("options.accessibility.high_contrast.error.tooltip")));
        }
        if ((rotateWithMinecart = this.list.findOption(this.options.rotateWithMinecart())) != null) {
            rotateWithMinecart.active = this.isMinecartOptionEnabled();
        }
    }

    @Override
    protected void addOptions() {
        OptionInstance<?>[] optionsInstances = AccessibilityOptionsScreen.options(this.options);
        Button controlsLink = Button.builder(OptionsScreen.CONTROLS, button -> this.minecraft.setScreen(new ControlsScreen(this, this.options))).build();
        OptionInstance<?> firstOptionInstance = optionsInstances[0];
        this.list.addSmall(firstOptionInstance.createButton(this.options), this.options.narrator(), controlsLink);
        this.list.addSmall((OptionInstance[])Arrays.stream(optionsInstances).filter(instance -> instance != firstOptionInstance).toArray(OptionInstance[]::new));
    }

    @Override
    protected void addFooter() {
        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(Component.translatable("options.accessibility.link"), ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.ACCESSIBILITY_HELP)).build());
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.lastScreen)).build());
    }

    @Override
    protected boolean panoramaShouldSpin() {
        return !(this.lastScreen instanceof AccessibilityOnboardingScreen);
    }

    private boolean isMinecartOptionEnabled() {
        return this.minecraft.level != null && this.minecraft.level.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS);
    }
}

