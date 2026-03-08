/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import java.util.List;
import net.mayaan.commands.FunctionInstantiationException;
import net.mayaan.commands.execution.UnboundEntryAction;
import net.mayaan.commands.functions.CommandFunction;
import net.mayaan.commands.functions.InstantiatedFunction;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public record PlainTextFunction<T>(Identifier id, List<UnboundEntryAction<T>> entries) implements CommandFunction<T>,
InstantiatedFunction<T>
{
    @Override
    public InstantiatedFunction<T> instantiate(@Nullable CompoundTag arguments, CommandDispatcher<T> dispatcher) throws FunctionInstantiationException {
        return this;
    }
}

