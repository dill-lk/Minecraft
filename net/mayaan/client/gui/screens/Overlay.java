/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.components.Renderable;

public abstract class Overlay
implements Renderable {
    public boolean isPauseScreen() {
        return true;
    }

    public void tick() {
    }
}

