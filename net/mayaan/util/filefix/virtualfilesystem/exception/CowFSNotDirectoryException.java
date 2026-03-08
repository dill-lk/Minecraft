/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.filefix.virtualfilesystem.exception;

import java.nio.file.NotDirectoryException;

public class CowFSNotDirectoryException
extends NotDirectoryException {
    public CowFSNotDirectoryException(String message) {
        super(message);
    }
}

