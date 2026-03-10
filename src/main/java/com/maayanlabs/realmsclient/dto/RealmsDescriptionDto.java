/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jspecify.annotations.Nullable
 */
package com.maayanlabs.realmsclient.dto;

import com.google.gson.annotations.SerializedName;
import com.maayanlabs.realmsclient.dto.ReflectionBasedSerialization;
import org.jspecify.annotations.Nullable;

public record RealmsDescriptionDto(@SerializedName(value="name") @Nullable String name, @SerializedName(value="description") String description) implements ReflectionBasedSerialization
{
}

