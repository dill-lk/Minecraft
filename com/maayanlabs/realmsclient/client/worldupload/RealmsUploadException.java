/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package com.maayanlabs.realmsclient.client.worldupload;

import net.mayaan.network.chat.Component;
import org.jspecify.annotations.Nullable;

public abstract class RealmsUploadException
extends RuntimeException {
    public @Nullable Component getStatusMessage() {
        return null;
    }

    public Component @Nullable [] getErrorMessages() {
        return null;
    }
}

