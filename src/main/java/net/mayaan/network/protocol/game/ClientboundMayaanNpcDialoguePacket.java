package net.mayaan.network.protocol.game;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

/**
 * Sent by the server to open an NPC dialogue screen on the client.
 *
 * <p>The server resolves which dialogue script applies for the interacting player
 * (based on faction standing, story chapter, and glyph mastery) and sends only
 * the already-resolved translation keys — the client never evaluates game conditions.
 *
 * <h2>Fields</h2>
 * <ul>
 *   <li>{@code npcId} — the NPC's unique identifier (e.g., {@code "elder_cenote"})</li>
 *   <li>{@code displayNameKey} — translation key for the NPC's display name</li>
 *   <li>{@code speakerKeys} — resolved speaker ID per line (parallel array with text keys)</li>
 *   <li>{@code textKeys} — resolved translation key per dialogue line</li>
 * </ul>
 *
 * @see net.mayaan.client.gui.screens.NpcDialogueScreen
 */
public final class ClientboundMayaanNpcDialoguePacket
        implements Packet<ClientGamePacketListener> {

    public static final StreamCodec<FriendlyByteBuf, ClientboundMayaanNpcDialoguePacket>
            STREAM_CODEC = Packet.codec(
                    ClientboundMayaanNpcDialoguePacket::write,
                    ClientboundMayaanNpcDialoguePacket::new);

    private final String npcId;
    private final String displayNameKey;
    private final List<String> speakerKeys;
    private final List<String> textKeys;

    public ClientboundMayaanNpcDialoguePacket(
            String npcId,
            String displayNameKey,
            List<String> speakerKeys,
            List<String> textKeys) {
        this.npcId = npcId;
        this.displayNameKey = displayNameKey;
        this.speakerKeys = List.copyOf(speakerKeys);
        this.textKeys = List.copyOf(textKeys);
    }

    private ClientboundMayaanNpcDialoguePacket(FriendlyByteBuf buf) {
        this.npcId = buf.readUtf();
        this.displayNameKey = buf.readUtf();
        int count = buf.readVarInt();
        List<String> speakers = new ArrayList<>(count);
        List<String> texts = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            speakers.add(buf.readUtf());
            texts.add(buf.readUtf());
        }
        this.speakerKeys = List.copyOf(speakers);
        this.textKeys = List.copyOf(texts);
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.npcId);
        buf.writeUtf(this.displayNameKey);
        buf.writeVarInt(this.textKeys.size());
        for (int i = 0; i < this.textKeys.size(); i++) {
            buf.writeUtf(this.speakerKeys.get(i));
            buf.writeUtf(this.textKeys.get(i));
        }
    }

    @Override
    public PacketType<ClientboundMayaanNpcDialoguePacket> type() {
        return GamePacketTypes.CLIENTBOUND_MAYAAN_NPC_DIALOGUE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleMayaanNpcDialogue(this);
    }

    /** Returns the NPC's unique identifier. */
    public String getNpcId() {
        return npcId;
    }

    /** Returns the translation key for the NPC's display name. */
    public String getDisplayNameKey() {
        return displayNameKey;
    }

    /** Returns the resolved speaker ID for line {@code index}. */
    public String getSpeaker(int index) {
        return speakerKeys.get(index);
    }

    /** Returns the resolved translation key for line {@code index}. */
    public String getTextKey(int index) {
        return textKeys.get(index);
    }

    /** Returns the total number of dialogue lines. */
    public int lineCount() {
        return textKeys.size();
    }
}
