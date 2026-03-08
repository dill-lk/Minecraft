/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.gui;

import java.util.Vector;
import javax.swing.JList;
import net.mayaan.server.MayaanServer;

public class PlayerListComponent
extends JList<String> {
    private final MayaanServer server;
    private int tickCount;

    public PlayerListComponent(MayaanServer server) {
        this.server = server;
        server.addTickable(this::tick);
    }

    public void tick() {
        if (this.tickCount++ % 20 == 0) {
            Vector<String> players = new Vector<String>();
            for (int i = 0; i < this.server.getPlayerList().getPlayers().size(); ++i) {
                players.add(this.server.getPlayerList().getPlayers().get(i).getGameProfile().name());
            }
            this.setListData(players);
        }
    }
}

