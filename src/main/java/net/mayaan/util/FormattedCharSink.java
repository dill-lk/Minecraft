/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util;

import net.mayaan.network.chat.Style;

@FunctionalInterface
public interface FormattedCharSink {
    public boolean accept(int var1, Style var2, int var3);
}

