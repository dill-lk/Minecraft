/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.server.dialog.action;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.resources.Identifier;
import net.mayaan.server.dialog.action.Action;
import net.mayaan.server.dialog.action.CommandTemplate;
import net.mayaan.server.dialog.action.CustomAll;
import net.mayaan.server.dialog.action.StaticAction;

public class ActionTypes {
    public static MapCodec<? extends Action> bootstrap(Registry<MapCodec<? extends Action>> registry) {
        StaticAction.WRAPPED_CODECS.forEach((action, codec) -> Registry.register(registry, Identifier.withDefaultNamespace(action.getSerializedName()), codec));
        Registry.register(registry, Identifier.withDefaultNamespace("dynamic/run_command"), CommandTemplate.MAP_CODEC);
        return Registry.register(registry, Identifier.withDefaultNamespace("dynamic/custom"), CustomAll.MAP_CODEC);
    }
}

