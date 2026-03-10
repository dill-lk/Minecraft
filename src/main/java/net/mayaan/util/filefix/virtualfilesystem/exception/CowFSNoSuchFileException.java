/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.filefix.virtualfilesystem.exception;

import java.nio.file.NoSuchFileException;

public class CowFSNoSuchFileException
extends NoSuchFileException {
    public CowFSNoSuchFileException(String message) {
        super(message);
    }
}

