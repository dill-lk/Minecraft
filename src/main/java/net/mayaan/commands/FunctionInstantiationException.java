/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands;

import net.mayaan.network.chat.Component;

public class FunctionInstantiationException
extends Exception {
    private final Component messageComponent;

    public FunctionInstantiationException(Component messageComponent) {
        super(messageComponent.getString());
        this.messageComponent = messageComponent;
    }

    public Component messageComponent() {
        return this.messageComponent;
    }
}

