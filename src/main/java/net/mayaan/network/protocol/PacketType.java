/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.resources.Identifier;

public record PacketType<T extends Packet<?>>(PacketFlow flow, Identifier id) {
    @Override
    public String toString() {
        return this.flow.id() + "/" + String.valueOf(this.id);
    }
}

