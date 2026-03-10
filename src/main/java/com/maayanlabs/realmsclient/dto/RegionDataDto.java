/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.JsonAdapter
 *  com.google.gson.annotations.SerializedName
 */
package com.maayanlabs.realmsclient.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.maayanlabs.realmsclient.dto.RealmsRegion;
import com.maayanlabs.realmsclient.dto.ReflectionBasedSerialization;
import com.maayanlabs.realmsclient.dto.ServiceQuality;

public record RegionDataDto(@SerializedName(value="regionName") @JsonAdapter(value=RealmsRegion.RealmsRegionJsonAdapter.class) RealmsRegion region, @SerializedName(value="serviceQuality") @JsonAdapter(value=ServiceQuality.RealmsServiceQualityJsonAdapter.class) ServiceQuality serviceQuality) implements ReflectionBasedSerialization
{
}

