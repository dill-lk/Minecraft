/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.gui.screens.configuration;

import com.maayanlabs.realmsclient.dto.RealmsServer;

public interface RealmsConfigurationTab {
    public void updateData(RealmsServer var1);

    default public void onSelected(RealmsServer serverData) {
    }

    default public void onDeselected(RealmsServer serverData) {
    }
}

