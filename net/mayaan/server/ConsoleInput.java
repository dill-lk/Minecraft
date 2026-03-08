/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server;

import net.mayaan.commands.CommandSourceStack;

public class ConsoleInput {
    public final String msg;
    public final CommandSourceStack source;

    public ConsoleInput(String msg, CommandSourceStack source) {
        this.msg = msg;
        this.source = source;
    }
}

