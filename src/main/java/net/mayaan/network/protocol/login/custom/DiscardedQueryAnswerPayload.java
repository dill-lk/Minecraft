/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login.custom;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.protocol.login.custom.CustomQueryAnswerPayload;

public record DiscardedQueryAnswerPayload() implements CustomQueryAnswerPayload
{
    public static final DiscardedQueryAnswerPayload INSTANCE = new DiscardedQueryAnswerPayload();

    @Override
    public void write(FriendlyByteBuf output) {
    }
}

