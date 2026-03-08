/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import net.mayaan.network.protocol.common.ClientboundClearDialogPacket;
import net.mayaan.network.protocol.common.ClientboundCustomPayloadPacket;
import net.mayaan.network.protocol.common.ClientboundCustomReportDetailsPacket;
import net.mayaan.network.protocol.common.ClientboundDisconnectPacket;
import net.mayaan.network.protocol.common.ClientboundKeepAlivePacket;
import net.mayaan.network.protocol.common.ClientboundPingPacket;
import net.mayaan.network.protocol.common.ClientboundResourcePackPopPacket;
import net.mayaan.network.protocol.common.ClientboundResourcePackPushPacket;
import net.mayaan.network.protocol.common.ClientboundServerLinksPacket;
import net.mayaan.network.protocol.common.ClientboundShowDialogPacket;
import net.mayaan.network.protocol.common.ClientboundStoreCookiePacket;
import net.mayaan.network.protocol.common.ClientboundTransferPacket;
import net.mayaan.network.protocol.common.ClientboundUpdateTagsPacket;
import net.mayaan.network.protocol.cookie.ClientCookiePacketListener;

public interface ClientCommonPacketListener
extends ClientCookiePacketListener {
    public void handleKeepAlive(ClientboundKeepAlivePacket var1);

    public void handlePing(ClientboundPingPacket var1);

    public void handleCustomPayload(ClientboundCustomPayloadPacket var1);

    public void handleDisconnect(ClientboundDisconnectPacket var1);

    public void handleResourcePackPush(ClientboundResourcePackPushPacket var1);

    public void handleResourcePackPop(ClientboundResourcePackPopPacket var1);

    public void handleUpdateTags(ClientboundUpdateTagsPacket var1);

    public void handleStoreCookie(ClientboundStoreCookiePacket var1);

    public void handleTransfer(ClientboundTransferPacket var1);

    public void handleCustomReportDetails(ClientboundCustomReportDetailsPacket var1);

    public void handleServerLinks(ClientboundServerLinksPacket var1);

    public void handleClearDialog(ClientboundClearDialogPacket var1);

    public void handleShowDialog(ClientboundShowDialogPacket var1);
}

