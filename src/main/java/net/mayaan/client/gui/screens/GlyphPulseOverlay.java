package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.ARGB;

/**
 * Glyph Pulse Overlay — brief full-screen visual response when the Stone Shard's SEEK
 * glyph is activated by the player.
 *
 * <p>This screen is opened client-side when the player right-clicks the
 * {@link net.mayaan.game.item.StoneShard}. It renders a centred glyph sigil with a pulsing
 * teal glow, accompanied by a short flavour message describing the SEEK sensation.
 *
 * <p>The overlay auto-dismisses after {@link #AUTO_CLOSE_TICKS} ticks, or can be closed
 * immediately with the Close button or Escape key.
 *
 * <h2>Layout</h2>
 * <pre>
 *   ╔═══════════════════════════════════╗
 *   ║                                   ║
 *   ║           ◈  SEEK  ◈              ║  ← glyph name (centred)
 *   ║                                   ║
 *   ║   "A faint pull — northeast.      ║
 *   ║    Something is waiting           ║
 *   ║    to be found."                  ║  ← flavour text (word-wrapped, centred)
 *   ║                                   ║
 *   ║              [ Close ]            ║
 *   ╚═══════════════════════════════════╝
 * </pre>
 */
public final class GlyphPulseOverlay extends Screen {

    // ── Palette ───────────────────────────────────────────────────────────────

    private static final int BG_COLOUR = ARGB.color(200, 5, 5, 18);
    private static final int BORDER_COLOUR = 0xFF3AFFCC;
    private static final int GLYPH_NAME_COLOUR = 0xFF3AFFCC;
    private static final int BODY_COLOUR = 0xFFCCDDCC;

    // ── Auto-close ────────────────────────────────────────────────────────────

    /**
     * Number of ticks before the overlay auto-closes (5 seconds at 20 ticks/sec).
     */
    private static final int AUTO_CLOSE_TICKS = 100;

    private int ticksOpen = 0;

    // ── Construction ──────────────────────────────────────────────────────────

    public GlyphPulseOverlay() {
        super(Component.translatable("overlay.mayaan.glyph_pulse.title"));
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void init() {
        int closeX = this.width / 2 - 50;
        int closeY = (int) (this.height * 0.62f);
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CLOSE, btn -> this.onClose())
                        .bounds(closeX, closeY, 100, 20)
                        .build());
    }

    @Override
    public void tick() {
        ticksOpen++;
        if (ticksOpen >= AUTO_CLOSE_TICKS) {
            this.onClose();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int panelW = 320;
        int panelH = 160;
        int panelLeft = (this.width - panelW) / 2;
        int panelTop = (int) (this.height * 0.25f);

        // Pulsing alpha for the background
        float pulse = 0.7f + 0.3f * (float) Math.sin((ticksOpen + partialTick) * 0.15);
        int bgAlpha = (int) (200 * pulse);
        graphics.fill(panelLeft, panelTop, panelLeft + panelW, panelTop + panelH,
                ARGB.color(bgAlpha, 5, 5, 18));
        graphics.renderOutline(panelLeft, panelTop, panelW, panelH, BORDER_COLOUR);

        int cx = this.width / 2;
        int y = panelTop + 16;

        // Glyph name
        Component glyphLabel = Component.literal("\u25c8  SEEK  \u25c8");
        graphics.drawCenteredString(this.font, glyphLabel, cx, y, GLYPH_NAME_COLOUR);
        y += this.font.lineHeight + 6;

        // Separator
        graphics.fill(panelLeft + 24, y, panelLeft + panelW - 24, y + 1, BORDER_COLOUR);
        y += 10;

        // Flavour text (word-wrapped, drawn centered by using centered draw calls per line)
        Component body = Component.translatable("overlay.mayaan.glyph_pulse.body");
        graphics.drawWordWrap(this.font, body,
                panelLeft + 20, y, panelW - 40, BODY_COLOUR);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
