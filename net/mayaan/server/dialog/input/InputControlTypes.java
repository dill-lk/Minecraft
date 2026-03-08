/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.server.dialog.input;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.resources.Identifier;
import net.mayaan.server.dialog.input.BooleanInput;
import net.mayaan.server.dialog.input.InputControl;
import net.mayaan.server.dialog.input.NumberRangeInput;
import net.mayaan.server.dialog.input.SingleOptionInput;
import net.mayaan.server.dialog.input.TextInput;

public class InputControlTypes {
    public static MapCodec<? extends InputControl> bootstrap(Registry<MapCodec<? extends InputControl>> registry) {
        Registry.register(registry, Identifier.withDefaultNamespace("boolean"), BooleanInput.MAP_CODEC);
        Registry.register(registry, Identifier.withDefaultNamespace("number_range"), NumberRangeInput.MAP_CODEC);
        Registry.register(registry, Identifier.withDefaultNamespace("single_option"), SingleOptionInput.MAP_CODEC);
        return Registry.register(registry, Identifier.withDefaultNamespace("text"), TextInput.MAP_CODEC);
    }
}

