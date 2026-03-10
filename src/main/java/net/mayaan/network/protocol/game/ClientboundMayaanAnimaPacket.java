package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

/**
 * Sent by the server to sync a player's Anima pool state to their client.
 *
 * <p>The client stores the received values in {@link net.mayaan.client.ClientMayaanData} where
 * they are read by {@link net.mayaan.client.gui.contextualbar.AnimaBarHudRenderer} each frame.
 *
 * <p>This packet is sent:
 * <ul>
 *   <li>Once immediately after the player joins (from
 *       {@link net.mayaan.game.MayaanServerEvents#onPlayerJoin})</li>
 *   <li>Every {@code ANIMA_SYNC_INTERVAL} server ticks while the player is online
 *       (from {@link net.mayaan.game.MayaanServerEvents#onPlayerTick})</li>
 * </ul>
 *
 * <h2>Fields</h2>
 * <ul>
 *   <li>{@code currentAnima} — current anima value (float, [0, maxAnima])</li>
 *   <li>{@code maxAnima} — maximum anima capacity (int)</li>
 *   <li>{@code inDrought} — whether the player is currently in Anima Drought</li>
 * </ul>
 */
public final class ClientboundMayaanAnimaPacket
        implements Packet<ClientGamePacketListener> {

    public static final StreamCodec<FriendlyByteBuf, ClientboundMayaanAnimaPacket> STREAM_CODEC =
            Packet.codec(ClientboundMayaanAnimaPacket::write,
                    ClientboundMayaanAnimaPacket::new);

    private final float currentAnima;
    private final int maxAnima;
    private final boolean inDrought;

    public ClientboundMayaanAnimaPacket(float currentAnima, int maxAnima, boolean inDrought) {
        this.currentAnima = currentAnima;
        this.maxAnima = maxAnima;
        this.inDrought = inDrought;
    }

    private ClientboundMayaanAnimaPacket(FriendlyByteBuf buf) {
        this.currentAnima = buf.readFloat();
        this.maxAnima = buf.readVarInt();
        this.inDrought = buf.readBoolean();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.currentAnima);
        buf.writeVarInt(this.maxAnima);
        buf.writeBoolean(this.inDrought);
    }

    @Override
    public PacketType<ClientboundMayaanAnimaPacket> type() {
        return GamePacketTypes.CLIENTBOUND_MAYAAN_ANIMA;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleMayaanAnima(this);
    }

    /** Returns the player's current Anima value. */
    public float getCurrentAnima() {
        return currentAnima;
    }

    /** Returns the player's maximum Anima capacity. */
    public int getMaxAnima() {
        return maxAnima;
    }

    /** Returns whether the player is currently in Anima Drought. */
    public boolean isInDrought() {
        return inDrought;
    }
}
