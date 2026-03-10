/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login.custom;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.protocol.login.custom.CustomQueryPayload;
import net.mayaan.resources.Identifier;

public record DiscardedQueryPayload(Identifier id) implements CustomQueryPayload
{
    @Override
    public void write(FriendlyByteBuf output) {
    }
}

