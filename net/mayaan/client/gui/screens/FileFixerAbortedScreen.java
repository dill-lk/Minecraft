/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.MultiLineTextWidget;
import net.mayaan.client.gui.components.StringWidget;
import net.mayaan.client.gui.layouts.FrameLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.ConfirmLinkScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.CommonLinks;
import org.jspecify.annotations.Nullable;

public class FileFixerAbortedScreen
extends Screen {
    protected final LinearLayout layout = LinearLayout.vertical().spacing(8);
    private final Component message;
    protected @Nullable Button backButton;
    protected @Nullable Button reportBugButton;
    protected final Runnable callback;

    public FileFixerAbortedScreen(Runnable callback, Component message) {
        super(Component.translatable("upgradeWorld.aborted.title"));
        this.callback = callback;
        this.message = message;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.message);
    }

    @Override
    protected void init() {
        super.init();
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        this.layout.addChild(new StringWidget(this.title, this.font));
        this.layout.addChild(new MultiLineTextWidget(this.message, this.font).setMaxWidth(this.width - 50).setMaxRows(15).setCentered(true));
        LinearLayout buttonLayout = this.layout.addChild(LinearLayout.horizontal().spacing(4));
        buttonLayout.defaultCellSetting().paddingTop(16);
        this.addButtons(buttonLayout);
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }

    protected void addButtons(LinearLayout buttonLayout) {
        this.backButton = buttonLayout.addChild(Button.builder(CommonComponents.GUI_BACK, button -> this.callback.run()).build());
        this.reportBugButton = buttonLayout.addChild(Button.builder(Component.translatable("upgradeWorld.aborted.reportBug"), ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.SNAPSHOT_BUGS_FEEDBACK, true)).build());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            this.callback.run();
            return true;
        }
        return super.keyPressed(event);
    }
}

