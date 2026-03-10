package net.mayaan.game.magic;

import com.mojang.serialization.Codec;

/**
 * The Glyph types known in the Mayaan civilization.
 *
 * Glyphs are the core interface between language and reality in Xibalkaal.
 * Each type corresponds to a domain of physical law that can be influenced.
 */
public enum GlyphType {
    /**
     * Seek — the first glyph. Calls the world toward the bearer. The starting glyph inscribed on the Stone Shard.
     */
    SEEK("seek", "Yaal"),

    /**
     * Bind — prevents crossing of thresholds or restricts movement.
     */
    BIND("bind", "Kaab"),

    /**
     * Mend — halts decay, heals wounds, preserves organic matter.
     */
    MEND("mend", "Ix"),

    /**
     * Illuminate — bends light, reveals hidden things, renders objects visible or invisible.
     */
    ILLUMINATE("illuminate", "Ek"),

    /**
     * Strengthen — hardens matter, increases structural integrity.
     */
    STRENGTHEN("strengthen", "Bak"),

    /**
     * Channel — directs Anima flow through structures and ley-lines.
     */
    CHANNEL("channel", "Kaan"),

    /**
     * Translate — opens passages between dimensions.
     */
    TRANSLATE("translate", "Xib");

    private final String id;
    /** The Mayaan script name for this glyph. */
    private final String scriptName;

    GlyphType(String id, String scriptName) {
        this.id = id;
        this.scriptName = scriptName;
    }

    public String getId() {
        return id;
    }

    /**
     * Returns the Mayaan script name for this glyph type.
     */
    public String getScriptName() {
        return scriptName;
    }

    /**
     * Finds a glyph type by its string ID. Returns {@code null} if not found.
     *
     * @param id the glyph identifier (e.g., {@code "seek"})
     */
    public static GlyphType byId(String id) {
        for (GlyphType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Codec that serializes/deserializes a {@link GlyphType} by its string ID
     * (e.g., {@code "seek"}, {@code "translate"}).
     */
    public static final Codec<GlyphType> CODEC =
            Codec.STRING.xmap(GlyphType::byId, GlyphType::getId);

    @Override
    public String toString() {
        return "mayaan:glyph/" + id;
    }
}
