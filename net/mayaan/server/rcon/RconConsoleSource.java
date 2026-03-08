/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.rcon;

import net.mayaan.commands.CommandSource;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.network.chat.Component;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.permissions.LevelBasedPermissionSet;
import net.mayaan.world.phys.Vec2;
import net.mayaan.world.phys.Vec3;

public class RconConsoleSource
implements CommandSource {
    private static final String RCON = "Rcon";
    private static final Component RCON_COMPONENT = Component.literal("Rcon");
    private final StringBuffer buffer = new StringBuffer();
    private final MayaanServer server;

    public RconConsoleSource(MayaanServer server) {
        this.server = server;
    }

    public void prepareForCommand() {
        this.buffer.setLength(0);
    }

    public String getCommandResponse() {
        return this.buffer.toString();
    }

    public CommandSourceStack createCommandSourceStack() {
        ServerLevel level = this.server.overworld();
        return new CommandSourceStack(this, Vec3.atLowerCornerOf(level.getRespawnData().pos()), Vec2.ZERO, level, LevelBasedPermissionSet.OWNER, RCON, RCON_COMPONENT, this.server, null);
    }

    @Override
    public void sendSystemMessage(Component message) {
        this.buffer.append(message.getString());
    }

    @Override
    public boolean acceptsSuccess() {
        return true;
    }

    @Override
    public boolean acceptsFailure() {
        return true;
    }

    @Override
    public boolean shouldInformAdmins() {
        return this.server.shouldRconBroadcast();
    }
}

