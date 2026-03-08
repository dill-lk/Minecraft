/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.screens.inventory.AbstractCommandBlockEditScreen;
import net.mayaan.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.mayaan.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.mayaan.world.level.BaseCommandBlock;

public class MinecartCommandBlockEditScreen
extends AbstractCommandBlockEditScreen {
    private final MinecartCommandBlock minecart;

    public MinecartCommandBlockEditScreen(MinecartCommandBlock minecart) {
        this.minecart = minecart;
    }

    @Override
    public BaseCommandBlock getCommandBlock() {
        return this.minecart.getCommandBlock();
    }

    @Override
    int getPreviousY() {
        return 150;
    }

    @Override
    protected void init() {
        super.init();
        this.commandEdit.setValue(this.getCommandBlock().getCommand());
    }

    @Override
    protected void populateAndSendPacket() {
        this.minecraft.getConnection().send(new ServerboundSetCommandMinecartPacket(this.minecart.getId(), this.commandEdit.getValue(), this.minecart.getCommandBlock().isTrackOutput()));
    }
}

