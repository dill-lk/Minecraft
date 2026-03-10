/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server;

import net.mayaan.server.ServerInfo;
import net.mayaan.server.dedicated.DedicatedServerProperties;

public interface ServerInterface
extends ServerInfo {
    public DedicatedServerProperties getProperties();

    public String getServerIp();

    public int getServerPort();

    public String getServerName();

    public String[] getPlayerNames();

    public String getLevelIdName();

    public String getPluginNames();

    public String runCommand(String var1);
}

