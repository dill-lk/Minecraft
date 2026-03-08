/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.exception;

import com.maayanlabs.realmsclient.client.RealmsError;

public class RealmsServiceException
extends Exception {
    public final RealmsError realmsError;

    public RealmsServiceException(RealmsError error) {
        this.realmsError = error;
    }

    @Override
    public String getMessage() {
        return this.realmsError.logMessage();
    }
}

