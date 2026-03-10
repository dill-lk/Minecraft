/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.jfr.stats;

import jdk.jfr.consumer.RecordedEvent;

public record FpsStat(int fps) {
    public static FpsStat from(RecordedEvent event, String field) {
        return new FpsStat(event.getInt(field));
    }
}

