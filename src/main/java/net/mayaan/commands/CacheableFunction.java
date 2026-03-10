/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.commands;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.functions.CommandFunction;
import net.mayaan.resources.Identifier;
import net.mayaan.server.ServerFunctionManager;

public class CacheableFunction {
    public static final Codec<CacheableFunction> CODEC = Identifier.CODEC.xmap(CacheableFunction::new, CacheableFunction::getId);
    private final Identifier id;
    private boolean resolved;
    private Optional<CommandFunction<CommandSourceStack>> function = Optional.empty();

    public CacheableFunction(Identifier id) {
        this.id = id;
    }

    public Optional<CommandFunction<CommandSourceStack>> get(ServerFunctionManager manager) {
        if (!this.resolved) {
            this.function = manager.get(this.id);
            this.resolved = true;
        }
        return this.function;
    }

    public Identifier getId() {
        return this.id;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CacheableFunction)) return false;
        CacheableFunction cacheableFunction = (CacheableFunction)obj;
        if (!this.getId().equals(cacheableFunction.getId())) return false;
        return true;
    }
}

