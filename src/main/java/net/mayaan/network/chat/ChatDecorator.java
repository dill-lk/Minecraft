/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.chat;

import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ChatDecorator {
    public static final ChatDecorator PLAIN = (player, plain) -> plain;

    public Component decorate(@Nullable ServerPlayer var1, Component var2);
}

