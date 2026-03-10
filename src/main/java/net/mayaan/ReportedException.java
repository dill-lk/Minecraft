/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan;

import net.mayaan.CrashReport;

public class ReportedException
extends RuntimeException {
    private final CrashReport report;

    public ReportedException(CrashReport report) {
        this.report = report;
    }

    public CrashReport getReport() {
        return this.report;
    }

    @Override
    public Throwable getCause() {
        return this.report.getException();
    }

    @Override
    public String getMessage() {
        return this.report.getTitle();
    }
}

