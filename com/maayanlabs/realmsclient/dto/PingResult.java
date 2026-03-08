/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.maayanlabs.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import com.maayanlabs.realmsclient.dto.ReflectionBasedSerialization;
import com.maayanlabs.realmsclient.dto.RegionPingResult;
import java.util.List;

public record PingResult(@SerializedName(value="pingResults") List<RegionPingResult> pingResults, @SerializedName(value="worldIds") List<Long> realmIds) implements ReflectionBasedSerialization
{
}

