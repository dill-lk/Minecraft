package net.mayaan.client.gui.screens;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.ClientMayaanData;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.game.magic.GlyphCasting;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.game.ServerboundMayaanCastGlyphPacket;
import net.mayaan.util.ARGB;

/**
 * Glyph Cast Screen — opened when the player right-clicks a
 * {@link net.mayaan.game.block.LeylineConduitBlock}.
 *
 * <p>Shows a grid of available glyphs (those the player has learned at least
 * {@link GlyphMastery#AWARE} for BASIC, or {@link GlyphMastery#PRACTICED} for MAJOR).
 * Clicking a glyph button sends a
 * {@link ServerboundMayaanCastGlyphPacket} to the server.
 *
 * <h2>Layout</h2>
 * <pre>
 *   ╔══════════════ CAST GLYPH ═══════════════╗
 *   ║  [SEEK  ▸BASIC]  [SEEK  ▸MAJOR]         ║
 *   ║  [ILLUM ▸BASIC]  [ILLUM ▸MAJOR]         ║
 *   ║  ...                                     ║
 *   ║                            [ Cancel ]   ║
 *   ╚═════════════════════════════════════════╝
 * </pre>
 *
 * <p>Glyphs where the player has {@code UNLEARNED} mastery are hidden entirely.
 * Glyphs where MAJOR requires PRACTICED but the player is only AWARE are shown
 * BASIC-only.
 */
public final class GlyphCastScreen extends Screen {

    // ── Palette ───────────────────────────────────────────────────────────────

    private static final int BACKGROUND = ARGB.color(215, 10, 6, 22);
    private static final int BORDER = 0xFF5A2D8A;
    private static final int TITLE_COLOUR = 0xFFDDBBFF;
    private static final int BTN_BASIC = 0xFF2A5A8A;
    private static final int BTN_MAJOR = 0xFF6A2A8A;
    private static final int BTN_HOVER = 0xFFAAFFEE;

    private static final int PANEL_MARGIN = 30;
    private static final int BTN_W = 100;
    private static final int BTN_H = 20;
    private static final int BTN_GAP = 6;
    private static final int COL_GAP = 10;

    // ── State ─────────────────────────────────────────────────────────────────

    public GlyphCastScreen() {
        super(Component.translatable("screen.mayaan.cast_glyph"));
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void init() {
        int panelLeft = PANEL_MARGIN;
        int panelTop = PANEL_MARGIN + 28; // below title
        int x = panelLeft + 12;
        int y = panelTop;

        ClientMayaanData data = ClientMayaanData.INSTANCE;

        for (GlyphType type : GlyphType.values()) {
            GlyphMastery mastery = data.getMastery(type);
            if (mastery == GlyphMastery.UNLEARNED) {
                continue; // not yet encountered
            }

            // BASIC button (requires FRAGMENTARY or higher)
            if (mastery.isAtLeast(GlyphMastery.FRAGMENTARY)) {
                final GlyphType capturedType = type;
                Button basicBtn = Button.builder(
                        Component.translatable("glyph.mayaan." + type.getId())
                                .append(Component.literal(" ▸ BASIC")),
                        btn -> sendCast(capturedType, GlyphCasting.CastTier.BASIC))
                        .bounds(x, y, BTN_W, BTN_H)
                        .build();
                this.addRenderableWidget(basicBtn);
            }

            // MAJOR button (requires PRACTICED or higher)
            if (mastery.isAtLeast(GlyphMastery.PRACTICED)) {
                final GlyphType capturedType = type;
                Button majorBtn = Button.builder(
                        Component.translatable("glyph.mayaan." + type.getId())
                                .append(Component.literal(" ▸ MAJOR")),
                        btn -> sendCast(capturedType, GlyphCasting.CastTier.MAJOR))
                        .bounds(x + BTN_W + COL_GAP, y, BTN_W, BTN_H)
                        .build();
                this.addRenderableWidget(majorBtn);
            }

            y += BTN_H + BTN_GAP;
            // Wrap if we're running out of vertical space
            if (y + BTN_H > this.height - PANEL_MARGIN - 30) {
                y = panelTop;
                x += (BTN_W + COL_GAP) * 2 + COL_GAP;
            }
        }

        // Cancel button
        int closeX = this.width - PANEL_MARGIN - BTN_W;
        int closeY = this.height - PANEL_MARGIN - BTN_H;
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CANCEL, btn -> this.onClose())
                        .bounds(closeX, closeY, BTN_W, BTN_H)
                        .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int panelLeft = PANEL_MARGIN;
        int panelTop = PANEL_MARGIN;
        int panelRight = this.width - PANEL_MARGIN;
        int panelBottom = this.height - PANEL_MARGIN;

        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, BACKGROUND);
        graphics.renderOutline(panelLeft, panelTop,
                panelRight - panelLeft, panelBottom - panelTop, BORDER);

        graphics.drawCenteredString(this.font,
                Component.translatable("screen.mayaan.cast_glyph"),
                this.width / 2, panelTop + 8, TITLE_COLOUR);

        // Anima display
        String animaStr = "Anima: " + (int) ClientMayaanData.INSTANCE.getCurrentAnima()
                + " / " + ClientMayaanData.INSTANCE.getMaxAnima();
        graphics.drawString(this.font, animaStr,
                panelLeft + 12, panelTop + 20, 0xFF8AE0D8, false);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void sendCast(GlyphType type, GlyphCasting.CastTier tier) {
        if (this.minecraft.getConnection() != null) {
            this.minecraft.getConnection().send(
                    new ServerboundMayaanCastGlyphPacket(type, tier));
        }
        this.onClose();
    }
}
