/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.server.dialog.input;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.registries.BuiltInRegistries;

public interface InputControl {
    public static final MapCodec<InputControl> MAP_CODEC = BuiltInRegistries.INPUT_CONTROL_TYPE.byNameCodec().dispatchMap(InputControl::mapCodec, c -> c);

    public MapCodec<? extends InputControl> mapCodec();
}

