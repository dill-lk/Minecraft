/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.jfr;

import net.mayaan.server.MayaanServer;

public enum Environment {
    CLIENT("client"),
    SERVER("server");

    private final String description;

    private Environment(String description) {
        this.description = description;
    }

    public static Environment from(MayaanServer server) {
        return server.isDedicatedServer() ? SERVER : CLIENT;
    }

    public String getDescription() {
        return this.description;
    }
}

