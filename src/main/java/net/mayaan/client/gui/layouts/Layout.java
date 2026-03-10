/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.layouts;

import java.util.function.Consumer;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.layouts.LayoutElement;

public interface Layout
extends LayoutElement {
    public void visitChildren(Consumer<LayoutElement> var1);

    @Override
    default public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
        this.visitChildren(child -> child.visitWidgets(widgetVisitor));
    }

    default public void arrangeElements() {
        this.visitChildren(child -> {
            if (child instanceof Layout) {
                Layout layout = (Layout)child;
                layout.arrangeElements();
            }
        });
    }
}

