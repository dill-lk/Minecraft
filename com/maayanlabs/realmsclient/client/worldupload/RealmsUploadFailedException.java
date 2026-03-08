/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.client.worldupload;

import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadException;
import net.mayaan.network.chat.Component;

public class RealmsUploadFailedException
extends RealmsUploadException {
    private final Component errorMessage;

    public RealmsUploadFailedException(Component errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RealmsUploadFailedException(String errorMessage) {
        this(Component.literal(errorMessage));
    }

    @Override
    public Component getStatusMessage() {
        return Component.translatable("mco.upload.failed", this.errorMessage);
    }
}

