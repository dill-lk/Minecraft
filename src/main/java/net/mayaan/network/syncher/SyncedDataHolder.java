/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.syncher;

import java.util.List;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.SynchedEntityData;

public interface SyncedDataHolder {
    public void onSyncedDataUpdated(EntityDataAccessor<?> var1);

    public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> var1);
}

