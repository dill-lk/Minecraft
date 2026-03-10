/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.chat;

import net.mayaan.network.chat.MessageSignature;

public record LastSeenTrackedEntry(MessageSignature signature, boolean pending) {
    public LastSeenTrackedEntry acknowledge() {
        return this.pending ? new LastSeenTrackedEntry(this.signature, false) : this;
    }
}

