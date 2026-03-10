/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.filefix;

import net.mayaan.util.filefix.FileFixException;
import net.mayaan.util.filefix.FileSystemCapabilities;

public class AtomicMoveNotSupportedFileFixException
extends FileFixException {
    public AtomicMoveNotSupportedFileFixException(FileSystemCapabilities fileSystemCapabilities) {
        super(null, fileSystemCapabilities);
    }
}

