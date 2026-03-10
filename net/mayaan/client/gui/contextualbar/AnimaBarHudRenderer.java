package net.mayaan.client.gui.contextualbar;

import com.maayanlabs.blaze3d.platform.Window;
import net.mayaan.client.ClientMayaanData;
import net.mayaan.client.DeltaTracker;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.contextualbar.ContextualBarRenderer;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ARGB;

/**
 * HUD renderer for the Anima bar — replaces the vanilla XP bar with a teal fill-bar
 * that shows current / max Anima and turns amber-red when the player is in Anima Drought.
 *
 * <p>Layout (above the hotbar, same position as the vanilla XP bar):
 * <pre>
 *   ┌──────────────────────────────────────────┐   ← background sprite (182×5)
 *   │█████████████████████░░░░░░░░░░░░░░░░░░░░│   ← fill (teal or amber-red if drought)
 *   └──────────────────────────────────────────┘
 *   [ANIMA 67/100]                      [⌘ 5/7]   ← text drawn in render()
 * </pre>
 *
 * <p>Sprite paths match the vanilla XP bar sprites. The fill colour is applied via ARGB
 * tinting:
 * <ul>
 *   <li>Normal:  {@code 0xFF 3DE0CF} — bright Anima teal</li>
 *   <li>Drought: {@code 0xFF E0853D} — warm amber (Anima Drought warning)</li>
 * </ul>
 *
 * <p>Registered in {@link net.mayaan.client.gui.Gui} as the {@code ANIMA} contextual bar.
 */
public final class AnimaBarHudRenderer implements ContextualBarRenderer {

    /** Sprite IDs reuse the vanilla XP bar assets. The bar texture is colour-tinted at runtime. */
    private static final Identifier BAR_BACKGROUND_SPRITE =
            Identifier.withDefaultNamespace("hud/experience_bar_background");
    private static final Identifier BAR_PROGRESS_SPRITE =
            Identifier.withDefaultNamespace("hud/experience_bar_progress");

    /** ARGB tint for normal Anima: bright teal. */
    private static final int TINT_NORMAL = 0xFF3DE0CF;

    /** ARGB tint for drought Anima: amber-orange. */
    private static final int TINT_DROUGHT = 0xFFE0853D;

    /**
     * Below this fill fraction the bar pulses in opacity to signal low Anima.
     * Set to 20 % (0.20).
     */
    private static final float LOW_ANIMA_PULSE_THRESHOLD = 0.20f;

    /** Minimum opacity factor during the low-anima pulse animation. */
    private static final float PULSE_MIN = 0.6f;

    /** Amplitude of the low-anima pulse animation (added to {@link #PULSE_MIN}). */
    private static final float PULSE_AMPLITUDE = 0.4f;

    /** Angular frequency of the low-anima pulse animation (radians per game tick). */
    private static final double PULSE_FREQUENCY = 0.25;

    /** Maximum possible Glyph Knowledge score (one point per {@link net.mayaan.game.magic.GlyphType}). */
    private static final int MAX_GLYPH_KNOWLEDGE_SCORE = 7;

    /** Unicode black-square symbol used as the drought indicator icon in HUD text. */
    private static final String DROUGHT_ICON = "\u25a0";

    private final Mayaan minecraft;

    public AnimaBarHudRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Window window = this.minecraft.getWindow();
        int left = this.left(window);
        int top = this.top(window);

        // Always draw the background track
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_BACKGROUND_SPRITE,
                left, top, ContextualBarRenderer.WIDTH, ContextualBarRenderer.HEIGHT);

        // Draw the fill bar
        ClientMayaanData data = ClientMayaanData.INSTANCE;
        float fill = data.getFillFraction();
        int filledPixels = (int) (fill * (float) ContextualBarRenderer.WIDTH);

        if (filledPixels > 0) {
            boolean drought = data.isInDrought();

            // Pulse alpha on low Anima (non-drought) to give visual feedback
            int tint;
            if (drought) {
                tint = TINT_DROUGHT;
            } else if (fill < LOW_ANIMA_PULSE_THRESHOLD) {
                float pulse = PULSE_MIN + PULSE_AMPLITUDE * (float) Math.sin(
                        minecraft.level != null
                                ? (double) minecraft.level.getGameTime() * PULSE_FREQUENCY
                                : 0.0);
                int alpha = (int) (255 * pulse);
                tint = ARGB.color(alpha,
                        (TINT_NORMAL >> 16) & 0xFF,
                        (TINT_NORMAL >> 8) & 0xFF,
                        TINT_NORMAL & 0xFF);
            } else {
                tint = TINT_NORMAL;
            }

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_PROGRESS_SPRITE,
                    ContextualBarRenderer.WIDTH, ContextualBarRenderer.HEIGHT,
                    0, 0,
                    left, top, filledPixels, ContextualBarRenderer.HEIGHT,
                    tint);
        }
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Font font = this.minecraft.font;
        ClientMayaanData data = ClientMayaanData.INSTANCE;
        Window window = this.minecraft.getWindow();
        int left = this.left(window);
        int top = this.top(window);

        // "ANIMA XX/YY" on the left side above the bar
        boolean drought = data.isInDrought();
        String animaText = "ANIMA " + (int) data.getCurrentAnima() + "/" + data.getMaxAnima()
                + (drought ? " " + DROUGHT_ICON + "DROUGHT" : "");
        int labelY = top - font.lineHeight - 1;
        graphics.drawString(font, animaText, left, labelY,
                drought ? TINT_DROUGHT : TINT_NORMAL, true);

        // Glyph knowledge score: "⌘ X/7" on the right
        int score = data.getKnowledgeScore();
        String glyphText = "\u2318 " + score + "/" + MAX_GLYPH_KNOWLEDGE_SCORE; // ⌘ X/7
        int glyphX = left + ContextualBarRenderer.WIDTH - font.width(glyphText);
        graphics.drawString(font, glyphText, glyphX, labelY, 0xFFB0B0FF, true);
    }
}
