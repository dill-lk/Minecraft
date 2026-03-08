/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components.debug;

import net.mayaan.network.chat.Component;

public record DebugEntryCategory(Component label, float sortKey) {
    public static final DebugEntryCategory SCREEN_TEXT = new DebugEntryCategory(Component.translatable("debug.options.category.text"), 1.0f);
    public static final DebugEntryCategory RENDERER = new DebugEntryCategory(Component.translatable("debug.options.category.renderer"), 2.0f);
}

