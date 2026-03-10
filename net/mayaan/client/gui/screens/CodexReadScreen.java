package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.game.item.CodexFragment;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.ARGB;

/**
 * Codex Read Screen — opened when the player right-clicks a
 * {@link net.mayaan.game.item.CodexFragment}.
 *
 * <p>Displays the fragment's lore text using translation keys of the form:
 * <pre>
 *   codex.mayaan.&lt;fragmentId&gt;.title
 *   codex.mayaan.&lt;fragmentId&gt;.body
 *   codex.mayaan.&lt;fragmentId&gt;.body.locked   (shown when knowledge is insufficient)
 * </pre>
 *
 * <p>The title is always shown; the body is either the full text or a locked placeholder,
 * depending on whether the player has the required glyph knowledge score.
 *
 * <h2>Layout</h2>
 * <pre>
 *   ┌─────────────────────────────────────────┐
 *   │  ≪ codex_category ≫                     │  ← category badge (top-left)
 *   │                                         │
 *   │         Fragment Title                  │  ← title (centred)
 *   │                                         │
 *   │  "Full body text that wraps across      │
 *   │   multiple lines if needed..."          │  ← body text (word-wrapped)
 *   │                                         │
 *   │                        [ Close ]        │
 *   └─────────────────────────────────────────┘
 * </pre>
 */
public final class CodexReadScreen extends Screen {

    // ── Palette ───────────────────────────────────────────────────────────────

    private static final int BACKGROUND = ARGB.color(225, 10, 8, 24);
    private static final int BORDER = 0xFF4A2D7A;
    private static final int TITLE_COLOUR = 0xFFDDC8FF;
    private static final int CATEGORY_COLOUR = 0xFF8070AA;
    private static final int BODY_COLOUR = 0xFFDDD8C8;
    private static final int LOCKED_COLOUR = 0xFF806060;

    private static final int PANEL_MARGIN = 28;
    private static final int TEXT_PADDING = 14;

    // ── State ─────────────────────────────────────────────────────────────────

    private final String fragmentId;
    private final CodexFragment.Category category;
    private final boolean canRead;

    public CodexReadScreen(String fragmentId, CodexFragment.Category category, boolean canRead) {
        super(Component.translatable("codex.mayaan." + fragmentId + ".title"));
        this.fragmentId = fragmentId;
        this.category = category;
        this.canRead = canRead;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void init() {
        int closeX = this.width - PANEL_MARGIN - 100;
        int closeY = this.height - PANEL_MARGIN - 20;
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CLOSE, btn -> this.onClose())
                        .bounds(closeX, closeY, 100, 20)
                        .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int panelLeft = PANEL_MARGIN;
        int panelTop = PANEL_MARGIN;
        int panelRight = this.width - PANEL_MARGIN;
        int panelBottom = this.height - PANEL_MARGIN;
        int textWidth = panelRight - panelLeft - TEXT_PADDING * 2;

        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, BACKGROUND);
        graphics.renderOutline(panelLeft, panelTop,
                panelRight - panelLeft, panelBottom - panelTop, BORDER);

        int y = panelTop + 8;

        // Category badge (e.g. "◈ Council Minutes")
        String categoryLabel = "\u25c8 ";
        Component categoryComp = Component.literal(categoryLabel)
                .append(Component.translatable("codex_category.mayaan." + category.getId()));
        graphics.drawString(this.font, categoryComp, panelLeft + TEXT_PADDING, y,
                CATEGORY_COLOUR, false);
        y += this.font.lineHeight + 8;

        // Fragment title (centred)
        Component title = Component.translatable("codex.mayaan." + fragmentId + ".title");
        graphics.drawCenteredString(this.font, title, this.width / 2, y, TITLE_COLOUR);
        y += this.font.lineHeight + 4;

        // Decorative separator
        graphics.fill(panelLeft + TEXT_PADDING, y, panelRight - TEXT_PADDING, y + 1, BORDER);
        y += 10;

        // Body text
        Component body = canRead
                ? Component.translatable("codex.mayaan." + fragmentId + ".body")
                : Component.translatable("codex.mayaan." + fragmentId + ".body.locked");
        int bodyColour = canRead ? BODY_COLOUR : LOCKED_COLOUR;
        graphics.drawWordWrap(this.font, body,
                panelLeft + TEXT_PADDING, y, textWidth, bodyColour);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
