/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.util.task;

import com.maayanlabs.realmsclient.client.RealmsClient;
import com.maayanlabs.realmsclient.dto.WorldTemplate;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.util.task.ResettingWorldTask;
import net.mayaan.network.chat.Component;

public class ResettingTemplateWorldTask
extends ResettingWorldTask {
    private final WorldTemplate template;

    public ResettingTemplateWorldTask(WorldTemplate template, long serverId, Component title, Runnable callback) {
        super(serverId, title, callback);
        this.template = template;
    }

    @Override
    protected void sendResetRequest(RealmsClient client, long serverId) throws RealmsServiceException {
        client.resetWorldWithTemplate(serverId, this.template.id());
    }
}

