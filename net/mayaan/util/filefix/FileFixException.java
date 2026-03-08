/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.util.filefix;

import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.util.filefix.FileSystemCapabilities;
import org.jspecify.annotations.Nullable;

public class FileFixException
extends RuntimeException {
    protected final @Nullable FileSystemCapabilities fileSystemCapabilities;

    public FileFixException(@Nullable Exception cause, @Nullable FileSystemCapabilities fileSystemCapabilities) {
        super(cause);
        this.fileSystemCapabilities = fileSystemCapabilities;
    }

    protected CrashReport createCrashReport() {
        CrashReport crashReport = CrashReport.forThrowable(this, "Upgrading world failed with errors");
        CrashReportCategory fsCapabilities = crashReport.addCategory("File system capabilities");
        fsCapabilities.setDetail("Hard Links", this.fileSystemCapabilities == null ? "null" : Boolean.valueOf(this.fileSystemCapabilities.hardLinks()));
        fsCapabilities.setDetail("Atomic Move", this.fileSystemCapabilities == null ? "null" : Boolean.valueOf(this.fileSystemCapabilities.atomicMove()));
        return crashReport;
    }

    public ReportedException makeReportedException() {
        return new ReportedException(this.createCrashReport());
    }
}

