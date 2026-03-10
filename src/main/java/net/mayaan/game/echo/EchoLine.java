package net.mayaan.game.echo;

/**
 * A single moment — one spoken line or narration beat — within a {@link TimelineEcho}.
 *
 * <p>An echo is composed of an ordered sequence of {@link EchoLine} objects that are
 * presented to the player one after another, like a cinematic sequence. Each line has
 * an optional speaker name (null or empty for unattributed narration) and the text itself.
 *
 * <p>Speaker names use in-world identifiers, not localization keys, because Timeline
 * Echoes originate in-world from preserved memories — the names are Mayaan names as
 * heard by the player, not engine strings.
 *
 * <h2>Example</h2>
 * <pre>
 *   new EchoLine("Ix-Channa",
 *       "If you are reading this — if the island's lock recognized you — " +
 *       "then you carry the Wanderer's Mark.")
 *
 *   new EchoLine(null,
 *       "The Scout looks directly at where your perspective is located, " +
 *       "as if she can see you across the centuries.")
 * </pre>
 */
public final class EchoLine {

    /**
     * Sentinel value used when the line is narration rather than dialogue.
     * Speaker-less lines are rendered differently in the UI (no name tag, italic text).
     */
    public static final String NARRATOR = null;

    private final String speaker;
    private final String text;

    /**
     * Creates a new echo line.
     *
     * @param speaker the name of the character speaking, or {@link #NARRATOR} for narration
     * @param text    the content of the line; must not be null or empty
     */
    public EchoLine(String speaker, String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("EchoLine text must not be null or empty");
        }
        this.speaker = speaker;
        this.text = text;
    }

    /**
     * Creates a narration line (no speaker attribution).
     *
     * @param text the narration text; must not be null or empty
     */
    public static EchoLine narration(String text) {
        return new EchoLine(NARRATOR, text);
    }

    /**
     * Creates a dialogue line attributed to the given speaker.
     *
     * @param speaker the character's name
     * @param text    the spoken text; must not be null or empty
     */
    public static EchoLine dialogue(String speaker, String text) {
        return new EchoLine(speaker, text);
    }

    /** Returns the speaker name, or {@code null} if this is a narration line. */
    public String getSpeaker() {
        return speaker;
    }

    /** Returns the line text. Never null or empty. */
    public String getText() {
        return text;
    }

    /** Returns {@code true} if this line has no attributed speaker (is narration). */
    public boolean isNarration() {
        return speaker == null || speaker.isEmpty();
    }

    @Override
    public String toString() {
        return isNarration() ? "[NARRATION] " + text : "[" + speaker + "] " + text;
    }
}
