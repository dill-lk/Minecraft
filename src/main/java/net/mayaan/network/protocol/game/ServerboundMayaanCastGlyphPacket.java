package net.mayaan.network.protocol.game;

import com.mojang.logging.LogUtils;
import net.mayaan.game.magic.GlyphCasting;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import org.slf4j.Logger;

/**
 * Sent by the client to the server when the player selects a glyph and cast tier
 * on the {@link net.mayaan.client.gui.screens.GlyphCastScreen}.
 *
 * <h2>Fields</h2>
 * <ul>
 *   <li>{@code glyphType} — the glyph the player wishes to cast</li>
 *   <li>{@code castTier} — BASIC or MAJOR (the player's chosen power level)</li>
 * </ul>
 *
 * <p>The server validates the cast via
 * {@link net.mayaan.game.magic.GlyphCasting#tryCast(java.util.UUID, GlyphType, GlyphCasting.CastTier)}
 * and sends back an updated anima sync packet and optional effect application.
 */
public final class ServerboundMayaanCastGlyphPacket
        implements Packet<ServerGamePacketListener> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final StreamCodec<FriendlyByteBuf, ServerboundMayaanCastGlyphPacket>
            STREAM_CODEC = Packet.codec(
                    ServerboundMayaanCastGlyphPacket::write,
                    ServerboundMayaanCastGlyphPacket::new);

    private final GlyphType glyphType;
    private final GlyphCasting.CastTier castTier;

    public ServerboundMayaanCastGlyphPacket(GlyphType glyphType, GlyphCasting.CastTier castTier) {
        this.glyphType = glyphType;
        this.castTier = castTier;
    }

    private ServerboundMayaanCastGlyphPacket(FriendlyByteBuf buf) {
        String glyphId = buf.readUtf();
        GlyphType type = GlyphType.byId(glyphId);
        if (type == null) {
            LOGGER.warn("ServerboundMayaanCastGlyphPacket: unknown glyph id '{}', defaulting to first type", glyphId);
            type = GlyphType.values()[0];
        }
        this.glyphType = type;

        String tierId = buf.readUtf();
        GlyphCasting.CastTier tier = GlyphCasting.CastTier.byId(tierId);
        if (tier == null) {
            LOGGER.warn("ServerboundMayaanCastGlyphPacket: unknown cast tier id '{}', defaulting to BASIC", tierId);
            tier = GlyphCasting.CastTier.BASIC;
        }
        this.castTier = tier;
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.glyphType.getId());
        buf.writeUtf(this.castTier.getId());
    }

    @Override
    public PacketType<ServerboundMayaanCastGlyphPacket> type() {
        return GamePacketTypes.SERVERBOUND_MAYAAN_CAST_GLYPH;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleMayaanCastGlyph(this);
    }

    /** Returns the glyph the player wants to cast. */
    public GlyphType getGlyphType() {
        return glyphType;
    }

    /** Returns the cast tier (BASIC or MAJOR) selected by the player. */
    public GlyphCasting.CastTier getCastTier() {
        return castTier;
    }
}
