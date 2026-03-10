/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.metrics.profiling;

import net.mayaan.util.profiling.ProfilerFiller;

public interface MetricsRecorder {
    public void end();

    public void cancel();

    public void startTick();

    public boolean isRecording();

    public ProfilerFiller getProfiler();

    public void endTick();
}

