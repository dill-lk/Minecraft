/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.multiplayer.prediction;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

@FunctionalInterface
public interface PredictiveAction {
    public Packet<ServerGamePacketListener> predict(int var1);
}

