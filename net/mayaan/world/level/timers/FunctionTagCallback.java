/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.timers;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.functions.CommandFunction;
import net.mayaan.resources.Identifier;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.ServerFunctionManager;
import net.mayaan.world.level.timers.TimerCallback;
import net.mayaan.world.level.timers.TimerQueue;

public record FunctionTagCallback(Identifier tagId) implements TimerCallback<MayaanServer>
{
    public static final MapCodec<FunctionTagCallback> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(FunctionTagCallback::tagId)).apply((Applicative)i, FunctionTagCallback::new));

    @Override
    public void handle(MayaanServer server, TimerQueue<MayaanServer> queue, long time) {
        ServerFunctionManager functionManager = server.getFunctions();
        List<CommandFunction<CommandSourceStack>> tag = functionManager.getTag(this.tagId);
        for (CommandFunction<CommandSourceStack> function : tag) {
            functionManager.execute(function, functionManager.getGameLoopSender());
        }
    }

    @Override
    public MapCodec<FunctionTagCallback> codec() {
        return CODEC;
    }
}

