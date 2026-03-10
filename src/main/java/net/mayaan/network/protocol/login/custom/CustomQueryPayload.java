/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login.custom;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.resources.Identifier;

public interface CustomQueryPayload {
    public Identifier id();

    public void write(FriendlyByteBuf var1);
}

