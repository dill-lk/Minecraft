/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.Options;
import net.mayaan.client.gui.components.AbstractSliderButton;
import net.mayaan.network.chat.CommonComponents;

public abstract class AbstractOptionSliderButton
extends AbstractSliderButton {
    protected final Options options;

    protected AbstractOptionSliderButton(Options options, int x, int y, int width, int height, double initialValue) {
        super(x, y, width, height, CommonComponents.EMPTY, initialValue);
        this.options = options;
    }
}

