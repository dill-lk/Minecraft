/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.client.worldupload;

import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadException;
import net.mayaan.network.chat.Component;

public class RealmsUploadWorldNotClosedException
extends RealmsUploadException {
    @Override
    public Component getStatusMessage() {
        return Component.translatable("mco.upload.close.failure");
    }
}

