/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debugchart;

import net.mayaan.network.protocol.game.ClientboundDebugSamplePacket;
import net.mayaan.util.debug.ServerDebugSubscribers;
import net.mayaan.util.debugchart.AbstractSampleLogger;
import net.mayaan.util.debugchart.RemoteDebugSampleType;

public class RemoteSampleLogger
extends AbstractSampleLogger {
    private final ServerDebugSubscribers subscribers;
    private final RemoteDebugSampleType sampleType;

    public RemoteSampleLogger(int dimensions, ServerDebugSubscribers subscribers, RemoteDebugSampleType sampleType) {
        this(dimensions, subscribers, sampleType, new long[dimensions]);
    }

    public RemoteSampleLogger(int dimensions, ServerDebugSubscribers subscribers, RemoteDebugSampleType sampleType, long[] defaults) {
        super(dimensions, defaults);
        this.subscribers = subscribers;
        this.sampleType = sampleType;
    }

    @Override
    protected void useSample() {
        if (this.subscribers.hasAnySubscriberFor(this.sampleType.subscription())) {
            this.subscribers.broadcastToAll(this.sampleType.subscription(), new ClientboundDebugSamplePacket((long[])this.sample.clone(), this.sampleType));
        }
    }
}

