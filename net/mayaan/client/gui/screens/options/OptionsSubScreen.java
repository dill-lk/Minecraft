/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.options;

import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.CycleButton;
import net.mayaan.client.gui.components.OptionsList;
import net.mayaan.client.gui.layouts.HeaderAndFooterLayout;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import org.jspecify.annotations.Nullable;

public abstract class OptionsSubScreen
extends Screen {
    protected final Screen lastScreen;
    protected final Options options;
    protected @Nullable OptionsList list;
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public OptionsSubScreen(Screen lastScreen, Options options, Component title) {
        super(title);
        this.lastScreen = lastScreen;
        this.options = options;
    }

    @Override
    protected void init() {
        this.addTitle();
        this.addContents();
        this.addFooter();
        OptionsSubScreen optionsSubScreen = this;
        this.layout.visitWidgets(x$0 -> optionsSubScreen.addRenderableWidget(x$0));
        this.repositionElements();
    }

    protected void addTitle() {
        this.layout.addTitleHeader(this.title, this.font);
    }

    protected void addContents() {
        this.list = this.layout.addToContents(new OptionsList(this.minecraft, this.width, this));
        this.addOptions();
        AbstractWidget abstractWidget = this.list.findOption(this.options.narrator());
        if (abstractWidget instanceof CycleButton) {
            CycleButton cycleButton;
            this.narratorButton = cycleButton = (CycleButton)abstractWidget;
            this.narratorButton.active = this.minecraft.getNarrator().isActive();
        }
    }

    protected abstract void addOptions();

    protected void addFooter() {
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        if (this.list != null) {
            this.list.updateSize(this.width, this.layout);
        }
    }

    @Override
    public void removed() {
        this.minecraft.options.save();
    }

    @Override
    public void onClose() {
        if (this.list != null) {
            this.list.applyUnsavedChanges();
        }
        this.minecraft.setScreen(this.lastScreen);
    }

    public void resetOption(OptionInstance<?> option) {
        if (this.list != null) {
            this.list.resetOption(option);
        }
    }
}

