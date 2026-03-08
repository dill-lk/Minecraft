/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.layouts;

import java.util.function.Consumer;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.components.StringWidget;
import net.mayaan.client.gui.layouts.Layout;
import net.mayaan.client.gui.layouts.LayoutElement;
import net.mayaan.client.gui.layouts.LayoutSettings;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.network.chat.Component;

public class CommonLayouts {
    private static final int LABEL_SPACING = 4;

    private CommonLayouts() {
    }

    public static Layout labeledElement(Font font, LayoutElement element, Component label) {
        return CommonLayouts.labeledElement(font, element, label, s -> {});
    }

    public static Layout labeledElement(Font font, LayoutElement element, Component label, Consumer<LayoutSettings> settings) {
        LinearLayout layout = LinearLayout.vertical().spacing(4);
        layout.addChild(new StringWidget(label, font));
        layout.addChild(element, settings);
        return layout;
    }
}

