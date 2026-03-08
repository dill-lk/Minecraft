/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.client.worldupload;

import com.maayanlabs.realmsclient.Unit;
import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadException;
import net.mayaan.network.chat.Component;

public class RealmsUploadTooLargeException
extends RealmsUploadException {
    final long sizeLimit;

    public RealmsUploadTooLargeException(long sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    @Override
    public Component[] getErrorMessages() {
        return new Component[]{Component.translatable("mco.upload.failed.too_big.title"), Component.translatable("mco.upload.failed.too_big.description", Unit.humanReadable(this.sizeLimit, Unit.getLargest(this.sizeLimit)))};
    }
}

