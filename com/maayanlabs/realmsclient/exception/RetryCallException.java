/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.exception;

import com.maayanlabs.realmsclient.client.RealmsError;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;

public class RetryCallException
extends RealmsServiceException {
    public static final int DEFAULT_DELAY = 5;
    public final int delaySeconds;

    public RetryCallException(int delaySeconds, int statusCode) {
        super(RealmsError.CustomError.retry(statusCode));
        this.delaySeconds = delaySeconds < 0 || delaySeconds > 120 ? 5 : delaySeconds;
    }
}

