/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.events;

import net.mayaan.client.gui.ComponentPath;
import net.mayaan.client.gui.components.TabOrderedElement;
import net.mayaan.client.gui.navigation.FocusNavigationEvent;
import net.mayaan.client.gui.navigation.ScreenDirection;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.input.CharacterEvent;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.input.PreeditEvent;
import org.jspecify.annotations.Nullable;

public interface GuiEventListener
extends TabOrderedElement {
    default public void mouseMoved(double x, double y) {
    }

    default public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }

    default public boolean mouseReleased(MouseButtonEvent event) {
        return false;
    }

    default public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        return false;
    }

    default public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        return false;
    }

    default public boolean keyPressed(KeyEvent event) {
        return false;
    }

    default public boolean keyReleased(KeyEvent event) {
        return false;
    }

    default public boolean charTyped(CharacterEvent event) {
        return false;
    }

    default public boolean preeditUpdated(@Nullable PreeditEvent event) {
        return false;
    }

    default public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
        return null;
    }

    default public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    public void setFocused(boolean var1);

    public boolean isFocused();

    default public boolean shouldTakeFocusAfterInteraction() {
        return true;
    }

    default public @Nullable ComponentPath getCurrentFocusPath() {
        if (this.isFocused()) {
            return ComponentPath.leaf(this);
        }
        return null;
    }

    default public ScreenRectangle getRectangle() {
        return ScreenRectangle.empty();
    }

    default public ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
        return this.getRectangle().getBorder(opposite);
    }
}

