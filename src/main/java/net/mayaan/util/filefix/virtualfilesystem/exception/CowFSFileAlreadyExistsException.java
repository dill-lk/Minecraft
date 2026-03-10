/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.filefix.virtualfilesystem.exception;

import java.nio.file.FileAlreadyExistsException;

public class CowFSFileAlreadyExistsException
extends FileAlreadyExistsException {
    public CowFSFileAlreadyExistsException(String message) {
        super(message);
    }
}

