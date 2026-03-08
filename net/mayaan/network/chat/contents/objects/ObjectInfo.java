/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.contents.objects;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.chat.FontDescription;

public interface ObjectInfo {
    public FontDescription fontDescription();

    public String description();

    public MapCodec<? extends ObjectInfo> codec();
}

