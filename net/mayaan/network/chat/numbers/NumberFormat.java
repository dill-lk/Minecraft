/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.chat.numbers;

import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.numbers.NumberFormatType;

public interface NumberFormat {
    public MutableComponent format(int var1);

    public NumberFormatType<? extends NumberFormat> type();
}

