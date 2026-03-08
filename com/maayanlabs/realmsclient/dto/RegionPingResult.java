/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.maayanlabs.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import com.maayanlabs.realmsclient.dto.ReflectionBasedSerialization;
import java.util.Locale;

public record RegionPingResult(@SerializedName(value="regionName") String regionName, @SerializedName(value="ping") int ping) implements ReflectionBasedSerialization
{
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s --> %.2f ms", this.regionName, Float.valueOf(this.ping));
    }
}

