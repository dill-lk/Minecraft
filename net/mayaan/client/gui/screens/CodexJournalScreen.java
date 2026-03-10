package net.mayaan.client.gui.screens;

import net.mayaan.client.ClientMayaanData;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.ARGB;

/**
 * Codex Journal — an in-game reference screen (opened with the J keybind) that shows:
 *
 * <ul>
 *   <li><b>Glyph Knowledge</b> — score badge and a row of coloured pip indicators,
 *       one per {@link GlyphType}, shaded by mastery tier</li>
 *   <li><b>Anima Overview</b> — current pool / max and drought status read from
 *       the client-side {@link ClientMayaanData} cache</li>
 * </ul>
 *
 * <h2>Layout</h2>
 * <pre>
 *   ┌──────────────── CODEX JOURNAL ─────────────────┐
 *   │  GLYPH KNOWLEDGE                  Score: 4/7   │
 *   │  [■ SEEK] [■ ILLUMINATE] [░ BIND] [░ TRANSLATE]│  ← coloured pips per glyph
 *   │  [■ CHANNEL] [░ DREAM] [░ VOID]                │
 *   │                                                 │
 *   │  ANIMA POOL                                     │
 *   │  67 / 100     No drought active                 │
 *   │                                                 │
 *   │                         [ Close ]              │
 *   └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>All data is read from {@link ClientMayaanData#INSTANCE}; no network call is made
 * when opening this screen.
 */
public final class CodexJournalScreen extends Screen {

    // ── Palette ───────────────────────────────────────────────────────────────

    private static final int BACKGROUND_COLOUR = ARGB.color(220, 8, 6, 18);
    private static final int BORDER_COLOUR = 0xFF4A2D6A;
    private static final int TITLE_COLOUR = 0xFFDDBBFF;
    private static final int SECTION_HEADER_COLOUR = 0xFF9988CC;
    private static final int TEXT_COLOUR = 0xFFDDD8C8;

    // Mastery pip colours
    private static final int PIP_MASTERED = 0xFF5AFFE0;      // bright teal
    private static final int PIP_PRACTICED = 0xFF6080CC;     // muted blue
    private static final int PIP_FRAGMENTARY = 0xFF806040;   // amber-brown
    private static final int PIP_UNLEARNED = 0xFF303030;     // dark grey

    private static final int PIP_W = 16;
    private static final int PIP_H = 14;
    private static final int PIP_GAP = 2;

    private static final int PANEL_MARGIN = 25;

    private static final int GLYPH_ABBR_LENGTH = 3;

    // ── State ─────────────────────────────────────────────────────────────────

    public CodexJournalScreen() {
        super(Component.translatable("screen.mayaan.codex_journal"));
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void init() {
        int closeX = this.width - PANEL_MARGIN - 100;
        int closeY = this.height - PANEL_MARGIN - 20;
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CLOSE, button -> this.onClose())
                        .bounds(closeX, closeY, 100, 20)
                        .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int panelLeft = PANEL_MARGIN;
        int panelTop = PANEL_MARGIN;
        int panelRight = this.width - PANEL_MARGIN;
        int panelBottom = this.height - PANEL_MARGIN;

        // Background + border
        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, BACKGROUND_COLOUR);
        graphics.renderOutline(panelLeft, panelTop,
                panelRight - panelLeft, panelBottom - panelTop, BORDER_COLOUR);

        int cx = this.width / 2;
        int y = panelTop + 10;

        // ── Title ─────────────────────────────────────────────────────────────
        Component title = Component.translatable("screen.mayaan.codex_journal");
        graphics.drawCenteredString(this.font, title, cx, y, TITLE_COLOUR);
        y += this.font.lineHeight + 6;

        // Divider
        graphics.fill(panelLeft + 6, y, panelRight - 6, y + 1, BORDER_COLOUR);
        y += 8;

        // ── Glyph Knowledge section ───────────────────────────────────────────
        int score = ClientMayaanData.INSTANCE.getKnowledgeScore();
        String scoreLabel = "GLYPH KNOWLEDGE  —  Score: " + score + " / " + GlyphType.values().length;
        graphics.drawString(this.font, scoreLabel, panelLeft + 10, y, SECTION_HEADER_COLOUR, false);
        y += this.font.lineHeight + 4;

        // One pip per glyph type
        GlyphType[] types = GlyphType.values();
        int pipX = panelLeft + 10;
        int rowMaxX = panelRight - 10;
        for (GlyphType type : types) {
            GlyphMastery mastery = ClientMayaanData.INSTANCE.getMastery(type);
            int pipColour = pipColourFor(mastery);

            // Wrap to next row if needed
            if (pipX + PIP_W > rowMaxX) {
                pipX = panelLeft + 10;
                y += PIP_H + PIP_GAP + 2;
            }

            // Pip background
            graphics.fill(pipX, y, pipX + PIP_W, y + PIP_H, pipColour);
            graphics.renderOutline(pipX, y, PIP_W, PIP_H,
                    ARGB.color(200, 255, 255, 255));

            // Glyph label (abbreviated)
            String abbr = glyphAbbreviation(type);
            int textX = pipX + (PIP_W - this.font.width(abbr)) / 2;
            int textY = y + (PIP_H - this.font.lineHeight) / 2;
            graphics.drawString(this.font, abbr, textX, textY, 0xFFFFFFFF, false);

            pipX += PIP_W + PIP_GAP;
        }
        y += PIP_H + 14;

        // Mastery legend
        renderLegendEntry(graphics, panelLeft + 10, y, PIP_MASTERED, "Mastered");
        renderLegendEntry(graphics, panelLeft + 80, y, PIP_PRACTICED, "Practiced");
        renderLegendEntry(graphics, panelLeft + 160, y, PIP_FRAGMENTARY, "Fragmentary");
        renderLegendEntry(graphics, panelLeft + 250, y, PIP_UNLEARNED, "Unlearned");
        y += this.font.lineHeight + 16;

        // Divider
        graphics.fill(panelLeft + 6, y, panelRight - 6, y + 1, BORDER_COLOUR);
        y += 8;

        // ── Anima Overview section ────────────────────────────────────────────
        graphics.drawString(this.font, "ANIMA POOL", panelLeft + 10, y, SECTION_HEADER_COLOUR, false);
        y += this.font.lineHeight + 4;

        String animaLine = (int) ClientMayaanData.INSTANCE.getCurrentAnima() + " / " + ClientMayaanData.INSTANCE.getMaxAnima();
        graphics.drawString(this.font, animaLine, panelLeft + 10, y, TEXT_COLOUR, false);

        String droughtStatus = ClientMayaanData.INSTANCE.isInDrought()
                ? "\u25a0 Anima Drought active"
                : "\u25a1 No drought active";
        int droughtColour = ClientMayaanData.INSTANCE.isInDrought() ? 0xFFE0853D : 0xFF8AE090;
        graphics.drawString(this.font, droughtStatus,
                panelLeft + 10 + this.font.width(animaLine) + 20, y, droughtColour, false);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static int pipColourFor(GlyphMastery mastery) {
        return switch (mastery) {
            case MASTERED -> PIP_MASTERED;
            case PRACTICED -> PIP_PRACTICED;
            case FRAGMENTARY -> PIP_FRAGMENTARY;
            default -> PIP_UNLEARNED;
        };
    }

    /**
     * Returns a short uppercase abbreviation for the given glyph type, suitable for
     * use as a pip label in the Codex Journal.
     *
     * @param type the glyph type to abbreviate
     * @return up to {@link #GLYPH_ABBR_LENGTH} uppercase characters
     */
    private static String glyphAbbreviation(GlyphType type) {
        String id = type.getId();
        return id.substring(0, Math.min(GLYPH_ABBR_LENGTH, id.length())).toUpperCase();
    }

    private void renderLegendEntry(GuiGraphics graphics, int x, int y, int colour, String label) {
        graphics.fill(x, y, x + 8, y + 8, colour);
        graphics.drawString(this.font, label, x + 10, y, TEXT_COLOUR, false);
    }
}
