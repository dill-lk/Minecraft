package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.game.ClientboundMayaanNpcDialoguePacket;
import net.mayaan.util.ARGB;

/**
 * Full-screen NPC dialogue reader.
 *
 * <p>Displays the server-resolved dialogue from a
 * {@link ClientboundMayaanNpcDialoguePacket}: the NPC's display name at the top,
 * then each dialogue line one at a time (paginated), with "Next" / "Close" buttons
 * at the bottom.
 *
 * <h2>Layout</h2>
 * <pre>
 *   ┌────────────────────────────────────────────────────────────────┐
 *   │                    ≪ Elder Cenote ≫                            │  ← NPC name (centred)
 *   │                                                                │
 *   │  "The first light did not come from above.                     │  ← speaker: text
 *   │   It rose from beneath the stone..."                           │
 *   │                                                                │
 *   │                     [ Next →  ]    [ Close ]                  │  ← buttons
 *   └────────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>The screen is opened by
 * {@link net.mayaan.client.multiplayer.ClientPacketListener#handleMayaanNpcDialogue}.
 */
public final class NpcDialogueScreen extends Screen {

    // ── Layout constants ──────────────────────────────────────────────────────

    private static final int BACKGROUND_COLOUR = ARGB.color(200, 10, 8, 20);
    private static final int BORDER_COLOUR = 0xFF5A3D6A;
    private static final int NPC_NAME_COLOUR = 0xFFDDC0FF;
    private static final int SPEAKER_COLOUR = 0xFF8FC0D8;
    private static final int TEXT_COLOUR = 0xFFE8E0D0;

    private static final int PANEL_MARGIN = 30;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 8;

    // ── State ─────────────────────────────────────────────────────────────────

    private final ClientboundMayaanNpcDialoguePacket packet;
    private int currentLine = 0;
    private Button nextButton;

    public NpcDialogueScreen(ClientboundMayaanNpcDialoguePacket packet) {
        super(Component.translatable(packet.getDisplayNameKey()));
        this.packet = packet;
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────────

    @Override
    protected void init() {
        int buttonY = this.height - PANEL_MARGIN - BUTTON_HEIGHT;
        int centerX = this.width / 2;

        // "Next →" button — visible while lines remain
        nextButton = this.addRenderableWidget(
                Button.builder(Component.translatable("npc.dialogue.button.next"),
                        button -> advance())
                        .bounds(centerX - BUTTON_WIDTH - BUTTON_GAP / 2, buttonY,
                                BUTTON_WIDTH, BUTTON_HEIGHT)
                        .build());

        // "Close" button
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CLOSE,
                        button -> this.onClose())
                        .bounds(centerX + BUTTON_GAP / 2, buttonY,
                                BUTTON_WIDTH, BUTTON_HEIGHT)
                        .build());

        updateButtons();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Background panel
        int panelLeft = PANEL_MARGIN;
        int panelTop = PANEL_MARGIN;
        int panelRight = this.width - PANEL_MARGIN;
        int panelBottom = this.height - PANEL_MARGIN;

        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, BACKGROUND_COLOUR);
        graphics.renderOutline(panelLeft, panelTop,
                panelRight - panelLeft, panelBottom - panelTop, BORDER_COLOUR);

        // NPC name header
        Component npcName = Component.translatable(packet.getDisplayNameKey());
        int nameX = this.width / 2 - this.font.width(npcName) / 2;
        int nameY = panelTop + 12;
        graphics.drawString(this.font, npcName, nameX, nameY, NPC_NAME_COLOUR, true);

        // Decorative separator
        int sepY = nameY + this.font.lineHeight + 6;
        graphics.fill(panelLeft + 8, sepY, panelRight - 8, sepY + 1, BORDER_COLOUR);

        // Dialogue line
        if (currentLine < packet.lineCount()) {
            String speaker = packet.getSpeaker(currentLine);
            String textKey = packet.getTextKey(currentLine);

            // Speaker label (e.g. "Elder Cenote:")
            int lineX = panelLeft + 12;
            int lineY = sepY + 10;

            Component speakerComp = Component.translatable("npc." + speaker + ".name")
                    .append(Component.literal(": "));
            graphics.drawString(this.font, speakerComp, lineX, lineY, SPEAKER_COLOUR, false);

            // Dialogue text, word-wrapped within panel width
            int textY = lineY + this.font.lineHeight + 4;
            int maxWidth = panelRight - panelLeft - 24;
            graphics.drawWordWrap(this.font,
                    Component.translatable(textKey),
                    lineX, textY, maxWidth, TEXT_COLOUR);

            // Line counter ("3 / 7") in bottom-right of panel
            String counter = (currentLine + 1) + " / " + packet.lineCount();
            int counterX = panelRight - this.font.width(counter) - 10;
            int counterY = panelBottom - BUTTON_HEIGHT - 12 - this.font.lineHeight;
            graphics.drawString(this.font, counter, counterX, counterY,
                    ARGB.color(160, 160, 150, 130), false);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        // Dialogue does not pause a singleplayer world
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        // Enter or Space advance/close dialogue; let Escape fall through to the base close handler
        if (event.key() == 257 || event.key() == 32) {
            advance();
            return true;
        }
        return super.keyPressed(event);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    private void advance() {
        currentLine++;
        if (currentLine >= packet.lineCount()) {
            this.onClose();
        } else {
            updateButtons();
        }
    }

    private void updateButtons() {
        if (nextButton != null) {
            boolean hasMore = currentLine < packet.lineCount() - 1;
            nextButton.setMessage(hasMore
                    ? Component.translatable("npc.dialogue.button.next")
                    : Component.translatable("npc.dialogue.button.done"));
        }
    }
}
