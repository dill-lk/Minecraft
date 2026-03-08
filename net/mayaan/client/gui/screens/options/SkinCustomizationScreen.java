/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import java.util.ArrayList;
import net.mayaan.client.Options;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.components.CycleButton;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.player.PlayerModelPart;

public class SkinCustomizationScreen
extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("options.skinCustomisation.title");

    public SkinCustomizationScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
    }

    @Override
    protected void addOptions() {
        ArrayList<AbstractWidget> widgets = new ArrayList<AbstractWidget>();
        for (PlayerModelPart part : PlayerModelPart.values()) {
            widgets.add(CycleButton.onOffBuilder(this.options.isModelPartEnabled(part)).create(part.getName(), (button, value) -> this.options.setModelPart(part, (boolean)value)));
        }
        widgets.add(this.options.mainHand().createButton(this.options));
        this.list.addSmall(widgets);
    }
}

