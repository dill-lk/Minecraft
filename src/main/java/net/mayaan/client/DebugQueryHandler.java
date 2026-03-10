/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client;

import java.util.function.Consumer;
import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.core.BlockPos;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.protocol.game.ServerboundBlockEntityTagQueryPacket;
import net.mayaan.network.protocol.game.ServerboundEntityTagQueryPacket;
import org.jspecify.annotations.Nullable;

public class DebugQueryHandler {
    private final ClientPacketListener connection;
    private int transactionId = -1;
    private @Nullable Consumer<CompoundTag> callback;

    public DebugQueryHandler(ClientPacketListener connection) {
        this.connection = connection;
    }

    public boolean handleResponse(int transactionId, @Nullable CompoundTag tag) {
        if (this.transactionId == transactionId && this.callback != null) {
            this.callback.accept(tag);
            this.callback = null;
            return true;
        }
        return false;
    }

    private int startTransaction(Consumer<CompoundTag> callback) {
        this.callback = callback;
        return ++this.transactionId;
    }

    public void queryEntityTag(int entityId, Consumer<CompoundTag> callback) {
        int transactionId = this.startTransaction(callback);
        this.connection.send(new ServerboundEntityTagQueryPacket(transactionId, entityId));
    }

    public void queryBlockEntityTag(BlockPos blockPos, Consumer<CompoundTag> callback) {
        int transactionId = this.startTransaction(callback);
        this.connection.send(new ServerboundBlockEntityTagQueryPacket(transactionId, blockPos));
    }
}

